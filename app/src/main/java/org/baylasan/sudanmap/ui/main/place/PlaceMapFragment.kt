package org.baylasan.sudanmap.ui.main.place


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.entity_loading_layout.*
import kotlinx.android.synthetic.main.fragment_place_map.*
import kotlinx.android.synthetic.main.network_error_layout.*
import kotlinx.android.synthetic.main.search_bar_layout.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.ui.main.MainActivity
import org.baylasan.sudanmap.ui.placesearch.PlaceSearchFragment
import org.baylasan.sudanmap.ui.profile.CompanyProfileActivity
import org.baylasan.sudanmap.ui.profile.CompanyProfileSheetDialog
import org.baylasan.sudanmap.utils.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class PlaceMapFragment : Fragment(R.layout.fragment_place_map) {

    private val viewModel: EntityViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()
    private lateinit var entityEntitiesListAdapter: EntitiesListAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>
    private var googleMap: GoogleMap? = null
    private val locationUpdatesSubject = PublishSubject.create<LatLng>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var activity: MainActivity

    private lateinit var entities: ArrayList<Entity>
    private val picasso: Picasso by inject()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MEGA","view recreated")

        openDrawerMenu.setOnClickListener {
            activity.openDrawer()
        }
        searchField.setOnClickListener {
            openSearchPage()
        }
        searchButton.setOnClickListener {
            openSearchPage()
        }
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        mapFragment?.getMapAsync { googleMap ->
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity,R.raw.style))
            this.googleMap = googleMap
            googleMap.isMyLocationEnabled = ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            getLocation()
            googleMap.setOnMarkerClickListener {
                val entity = (it.tag as Entity)
                Log.d("MEGA", "$entity")
                CompanyProfileSheetDialog.newInstance(entity).show(fragmentManager!!, "")
                true
            }

            googleMap.setOnCameraMoveListener {
                val latLng = googleMap.cameraPosition.target

                locationUpdatesSubject.onNext(latLng)
            }
        }
        val locationUpdatesDisposable = locationUpdatesSubject
            .debounce(700, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewModel.loadNearby(it.latitude, it.longitude)
            }, {

            })

        compositeDisposable.add(locationUpdatesDisposable)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.peekHeight = 200  // height + bottom navigation height
        entities = ArrayList()
        entityEntitiesListAdapter = EntitiesListAdapter(entities,
            object : EntitiesListAdapter.OnItemClick {
                override fun onItemClick(entity: Entity) {
                    val profileIntent =
                        Intent(activity, CompanyProfileActivity::class.java)
                    profileIntent.putExtra("entity", entity)
                    startActivity(profileIntent)

                    //  bottomSheetBehavior.state = CustomBottomSheetBehavior.STATE_COLLAPSED
                }

            })


        recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        recyclerView.adapter = entityEntitiesListAdapter
        viewModel.filterLiveData.observe(this, Observer {
            val entityFilterAdapter = EntityFilterAdapter(activity, it) { category ->
                viewModel.filterEntities(category)
            }
            filterChipRecyclerView.layoutManager =
                LinearLayoutManager(
                    activity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            filterChipRecyclerView.adapter = entityFilterAdapter

        })
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                activity.bottomSheet.radius =
                    if (newState == BottomSheetBehavior.STATE_EXPANDED)
                        0f
                    else TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        20f,
                        resources.displayMetrics
                    )
            }
        })

        viewModel.loadEntity()
        observeViewModel()

    }

    private fun openSearchPage() {
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentLayout, PlaceSearchFragment(), "")
            .addToBackStack("")
            .commit()
    }

    private fun observeViewModel() {

        viewModel.events.observe(this, Observer { event ->
            when (event) {

                is DataEvent -> {
                    Log.d("MEGA", "Data loaded")
                    entityFilterLoading.gone()
                    entityLoading.gone()
                    errorLayout.gone()

                    val data = event.entityList
                    if (data.isNotEmpty()) {
                        entities.clear()
                        entities.addAll(data)
                        Log.d("KLD", toString())
                        entityEntitiesListAdapter.notifyDataSetChanged()
                    }
                }
                is LoadingEvent -> {
                    entityFilterLoading.show()
                    entityLoading.show()
                    errorLayout.gone()
                }
                is ErrorEvent, TimeoutEvent, NetworkErrorEvent -> {
                    errorLayout.show()
                    entityFilterLoading.gone()
                    entityLoading.gone()
                    retryButton.setOnClickListener {
                        viewModel.loadEntity()
                    }

                }

            }

        })
        errorCard.setOnClickListener {
            loadNearbyPlacesFromCurrentLocation()

        }
        myLocationFab.setOnClickListener {
            getLocation()

        }
        viewModel.nearbyEvents.observe(this, Observer { event ->

            when (event) {
                is NearbyEmptyEvent -> {
                    loadingCard.hide()
                    emptyCard.show()
                    errorCard.hide()

                }
                is NearbyDataEvent -> {
                    Log.d("MEGA", "Data loaded")
                    loadingCard.hide()
                    emptyCard.hide()
                    errorCard.hide()

                    val data = event.nearByEntity
                    googleMap?.clear()

                    data.forEach { entity ->
                        val latLng = LatLng(
                            entity.location.lat,
                            entity.location.long
                        )
                        val marker = googleMap?.addMarker(
                            MarkerOptions().position(
                                latLng
                            ).title(entity.name)
                            //).icon(BitmapDescriptorFactory.defaultMarker()).title("KLD")
                        )
                        marker?.tag = entity


                        picasso.load(entity.category.iconPng)
                            .placeholder(R.drawable.ic_marker_placeholder)
                            .error(R.drawable.ic_marker_placeholder)
                            .into(PicassoMarker(marker))
                    }


                }
                is NearbyLoadingEvent -> {
                    Log.d("KLD", "Loading")
                    loadingCard.show()
                    emptyCard.hide()
                    errorCard.hide()

                }
                else -> {
                    loadingCard.hide()
                    emptyCard.hide()
                    errorCard.show()


                }
            }
        })


    }


    private fun loadNearbyPlacesFromCurrentLocation() {
        val target = googleMap?.cameraPosition?.target
        if (target != null)
            viewModel.loadNearby(target.latitude, target.longitude)
    }


    private fun zoomToMyLocation(location: Location?) {
        val cameraPosition = CameraPosition.Builder()
            .target(location?.latitude?.let {
                LatLng(
                    it,
                    location.longitude
                )
            })
            .zoom(12f)
            .build()
        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()

    }

    private fun getLocation() {
        LocationLiveData(activity).observe(this, Observer {
            zoomToMyLocation(it.toLocation())
        })
    }

}
