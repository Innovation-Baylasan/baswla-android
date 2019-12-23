package org.baylasan.sudanmap.ui.main

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fish4fun.likegooglemaps.bottomsheet.CustomBottomSheetBehavior
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.domain.entity.model.Entity
import org.baylasan.sudanmap.ui.layers.MapLayersFragment
import org.baylasan.sudanmap.ui.profile.CompanyProfileFragment
import org.baylasan.sudanmap.ui.search.SearchFragment
import org.baylasan.sudanmap.utils.newFragmentInstance
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var entities: ArrayList<Entity>

    private lateinit var entityEntitiesListAdapter: EntitiesListAdapter
    lateinit var adapter: EntityListAdapter

    private val entityViewModel: EntityViewModel by viewModel()

    private var map: GoogleMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?

        mapFragment?.getMapAsync { googleMap ->
            map = googleMap

        }

        //    map =supportFragmentManager.findFragmentById(R.id.mapFragment) as GoogleMap

        val bottomSheet = findViewById<View>(R.id.bottomSheet)
        val layoutParams = bottomSheet.layoutParams as CoordinatorLayout.LayoutParams
        val bottomSheetBehavior = layoutParams.behavior as CustomBottomSheetBehavior<*>

        bottomSheetBehavior.peekHeight = 80
        entities = ArrayList()
        entityEntitiesListAdapter = EntitiesListAdapter(entities,
            object : EntitiesListAdapter.OnItemClick {
                override fun onItemClick(entityDto: Entity) {
                    //   moveCameraToClickedItem(entityDto.location.lat, entityDto.location.long)
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragmentLayout,
                            newFragmentInstance<CompanyProfileFragment>("entity" to entityDto),
                            "profile"
                        )
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack("main")
                        .setReorderingAllowed(true)
                        .commit()

                    newFragmentInstance<CompanyProfileFragment>("entity" to entityDto)
                    bottomSheetBehavior.state = CustomBottomSheetBehavior.STATE_COLLAPSED
                }

            })

        layersButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentLayout, MapLayersFragment.newInstance(), "layers")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit()
        }
        searchButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentLayout, SearchFragment.newInstance(), "search")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit()
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)


        recyclerView.adapter = entityEntitiesListAdapter

        bottomSheetBehavior.state
        bottomSheetBehavior.setBottomSheetCallback(object :
            CustomBottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == CustomBottomSheetBehavior.STATE_EXPANDED) {
                    searchBar.setBackgroundColor(Color.WHITE)

                } else {

                    searchBar.setBackgroundColor(Color.TRANSPARENT)

                }
            }
        })

        entityViewModel.loadEntity()
        observeViewModel()
    }

    private fun moveCameraToClickedItem(lat: Double, lng: Double) {
        val firstLatLng = LatLng(entities[0].location.lat, entities[0].location.long)

        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 15f))
        map?.animateCamera(CameraUpdateFactory.zoomIn())
        map?.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)
    }

    private fun observeViewModel() {

        entityViewModel.events.observe(this, Observer { event ->
            when (event) {
                is DataEvent -> {
                    val data = event.entityResponseDto.data
                    if (data.isNotEmpty()) {
                        entities.addAll(data)
                        Log.d("KLD", toString())
                        //  drawMarkerInMap(data)
                        entityEntitiesListAdapter.notifyDataSetChanged()
                    }
                }
            }

        })

    }

    private fun drawMarkerInMap(entities: List<Entity>) {
        entities.forEach { entity ->
            if (map != null) {
                val latLng = LatLng(entity.location.lat, entity.location.long)
                val markerOptions = MarkerOptions().position(latLng)
                    .icon((bitmapDescriptorFromVector(this, R.drawable.ic_business)))

                map?.addMarker(markerOptions)
            }
        }

        //  moveCameraToClickedItem(entities[0].location.lat, entities[0].location.long)

    }

    private fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor? {
        val background =
            ContextCompat.getDrawable(context, R.drawable.ic_business)
        background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
        val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        vectorDrawable!!.setBounds(
            40,
            20,
            vectorDrawable.intrinsicWidth + 40,
            vectorDrawable.intrinsicHeight + 20
        )
        val bitmap = Bitmap.createBitmap(
            background.intrinsicWidth,
            background.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        background.draw(canvas)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}
