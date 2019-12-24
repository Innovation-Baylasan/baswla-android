package org.baylasan.sudanmap.ui.main

import android.content.IntentSender
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fish4fun.likegooglemaps.bottomsheet.CustomBottomSheetBehavior
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.domain.entity.model.Entity
import org.baylasan.sudanmap.ui.layers.MapLayersFragment
import org.baylasan.sudanmap.ui.profile.CompanyProfileFragment
import org.baylasan.sudanmap.ui.search.SearchFragment
import org.baylasan.sudanmap.utils.newFragmentInstance
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var entities: ArrayList<Entity>

    private lateinit var entityEntitiesListAdapter: EntitiesListAdapter
    lateinit var adapter: EntityListAdapter

    private val entityViewModel: EntityViewModel by viewModel()

    private var googleMap: GoogleMap? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapFragment?.getMapAsync { googleMap ->
            this.googleMap = googleMap
            initMap()
            getCurrentLocation()
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

    private fun initMap() {
        googleMap?.isMyLocationEnabled = true
      /*  googleMap?.setOnMyLocationButtonClickListener(this)
        googleMap?.setOnMyLocationClickListener(this)*/

        googleMap?.uiSettings?.isMyLocationButtonEnabled = true

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // for (location in location..locations){
                updateMapLocation(location)
                // }
            }
    }


    private fun getCurrentLocation() {

        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (10 * 1000).toLong()
        locationRequest.fastestInterval = 2000

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()

        val result = LocationServices.getSettingsClient(this)
            .checkLocationSettings(locationSettingsRequest)
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                if (response!!.locationSettingsStates.isLocationPresent) {
                    getLastLocation()
                }
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvable = exception as ResolvableApiException
                        resolvable.startResolutionForResult(this,
                            REQUEST_CHECK_SETTINGS
                        )
                    } catch (e: IntentSender.SendIntentException) {
                    } catch (e: ClassCastException) {
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }
    }

    private fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    val mLastLocation = task.result
                    Log.d("getLastLocation", mLastLocation.toString())
                    var address = "No known address"

                    val gcd = Geocoder(this, Locale.getDefault())
                    val addresses: List<Address>
                    try {
                        addresses = gcd.getFromLocation(
                            mLastLocation!!.latitude,
                            mLastLocation.longitude,
                            1
                        )
                        if (addresses.isNotEmpty()) {
                            address = addresses[0].getAddressLine(0)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

              /*      val icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(
                            this.resources,
                            R.drawable.ic_pin
                        )
                    )
                    googleMap?.addMarker(
                        MarkerOptions()
                            .position(LatLng(mLastLocation!!.latitude, mLastLocation.longitude))
                            .title("Current Location")
                            .snippet(address)
                            .icon(icon)
                    )*/

                    val cameraPosition = CameraPosition.Builder()
                        .target(mLastLocation?.latitude?.let {
                            LatLng(
                                it,
                                mLastLocation.longitude
                            )
                        })
                        .zoom(17f)
                        .build()
                    googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                } else {
                    Toast.makeText(this, "No current location found", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }


    private fun updateMapLocation(location: Location?) {
        googleMap?.moveCamera(
            CameraUpdateFactory.newLatLng(
                LatLng(
                    location?.latitude ?: 0.0,
                    location?.longitude ?: 0.0
                )
            )
        )

        googleMap?.moveCamera(CameraUpdateFactory.zoomTo(15.0f))
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


    companion object {
        private const val LOCATION_PERMISSION = 42
        private const val REQUEST_CHECK_SETTINGS = 40
    }
}
