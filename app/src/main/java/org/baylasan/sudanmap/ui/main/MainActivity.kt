package org.baylasan.sudanmap.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.entity_loading_layout.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.ui.layers.MapLayersFragment
import org.baylasan.sudanmap.ui.profile.CompanyProfileActivity
import org.baylasan.sudanmap.ui.search.SearchFragment
import org.baylasan.sudanmap.utils.gone
import org.baylasan.sudanmap.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {

    private lateinit var entities: ArrayList<Entity>

    private lateinit var entityEntitiesListAdapter: EntitiesListAdapter
    lateinit var adapter: EntityListAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>

    private val entityViewModel: EntityViewModel by viewModel()

    private var googleMap: GoogleMap? = null

    private val permissions =
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationUpdatesSubject = PublishSubject.create<LatLng>()
    /*  lateinit var clusterManager: ClusterManager<Place>
      lateinit var clusterRenderer: MarkerClusterRenderer*/

    private val compositeDisposable = CompositeDisposable()

    override fun onBackPressed() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            super.onBackPressed()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setNestedScrollingEnabled(recyclerViewsLayout, true)

        if (!canAccessLocation()) requestPermissions()

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapFragment?.getMapAsync { googleMap ->
            this.googleMap = googleMap
            if (permissionGiven()) {
                initMap()
                getCurrentLocation()
            } else {
                requestPermissions()
            }

            googleMap.setOnCameraMoveListener {
                val latLng = googleMap.cameraPosition.target
                locationUpdatesSubject.onNext(latLng)
            }
        }
        searchField.setOnClickListener {
            openSearchFragment()
        }

        val locationUpdatesDisposable = locationUpdatesSubject
            .debounce(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                entityViewModel.loadNearby(it.latitude, it.longitude)
            }, {})

        compositeDisposable.add(locationUpdatesDisposable)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.peekHeight = 250
        entities = ArrayList()
        entityEntitiesListAdapter = EntitiesListAdapter(entities,
            object : EntitiesListAdapter.OnItemClick {
                override fun onItemClick(entityDtoDto: Entity) {
                    bundleOf("entity" to entityDtoDto)
                    val profileIntent =
                        Intent(applicationContext, CompanyProfileActivity::class.java)
                    profileIntent.putExtra("entity", entityDtoDto)
                    startActivity(profileIntent)

                    //  bottomSheetBehavior.state = CustomBottomSheetBehavior.STATE_COLLAPSED
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
            openSearchFragment()
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)


        recyclerView.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

        recyclerView.adapter = entityEntitiesListAdapter
        entityViewModel.filterLiveData.observe(this, Observer {
            val entityFilterAdapter = EntityFilterAdapter(this, it) { category ->
                entityViewModel.filterEntities(category)
            }
            filterChipRecyclerView.layoutManager =
                LinearLayoutManager(
                    applicationContext,
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
                searchField.isEnabled = newState != BottomSheetBehavior.STATE_EXPANDED

                this@MainActivity.bottomSheet.radius =
                    if (newState == BottomSheetBehavior.STATE_EXPANDED)
                        0f
                    else 20f
            }
        })

        entityViewModel.loadEntity()
        observeViewModel()
    }

    private fun openSearchFragment() {

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentLayout,
                SearchFragment.newInstance(),
                "search"
            )
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION)
    }

    private fun permissionGiven(): Boolean {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
    }


    private fun canAccessLocation(): Boolean {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION))
    }

    private fun hasPermission(perm: String): Boolean {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION) {
            if (permissions.isNotEmpty() &&
                permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                initMap()
                getCurrentLocation()
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }


    private fun initMap() {
        if (canAccessLocation()) {
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
        } else {
            requestPermissions()
        }
    }


    private fun getCurrentLocation() {

        val builder = LocationSettingsRequest.Builder()
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
                        resolvable.startResolutionForResult(
                            this,
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

                    val cameraPosition = CameraPosition.Builder()
                        .target(mLastLocation?.latitude?.let {
                            LatLng(
                                it,
                                mLastLocation.longitude
                            )
                        })
                        .zoom(6f)
                        .build()
                    googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    entityViewModel.loadNearby(mLastLocation?.latitude!!, mLastLocation.longitude)
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
                    Log.d("MEGA", "Data loaded")
                    entityFilterLoading.visibility = View.GONE
                    entityLoading.visibility = View.GONE

                    val data = event.entityList
                    if (data.isNotEmpty()) {
                        entities.clear()
                        entities.addAll(data)
                        Log.d("KLD", toString())
                        entityEntitiesListAdapter.notifyDataSetChanged()
                    }
                }
                is LoadingEvent -> {
                    entityFilterLoading.visibility = View.VISIBLE
                    entityLoading.visibility = View.VISIBLE
                }
                is ErrorEvent -> {
                    Toast.makeText(applicationContext, event.errorMessage, Toast.LENGTH_LONG).show()
                    entityFilterLoading.visibility = View.GONE
                    entityLoading.visibility = View.GONE
                }
                else -> {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
                    entityFilterLoading.visibility = View.GONE
                    entityLoading.visibility = View.GONE
                }
            }

        })

        myLocationFab.setOnClickListener {
            getCurrentLocation()
        }
        entityViewModel.nearbyEvents.observe(this, Observer { event ->

            when (event) {

                is NearbyDataEvent -> {
                    Log.d("MEGA", "Data loaded")
                    loadingCard.gone()
                    val data = event.nearByEntity
                    if (data.isNotEmpty()) {
                        googleMap?.clear()
                        data.forEach { entity ->
                            googleMap?.let {
                                it.addMarker(
                                    MarkerOptions().position(
                                        LatLng(
                                            entity.location.lat,
                                            entity.location.long
                                        )
                                    ).icon(
                                        bitmapDescriptorFromVector(
                                            this,
                                            R.drawable.ic_marker
                                        )
                                    ).title(entity.name)
                                    //).icon(BitmapDescriptorFactory.defaultMarker()).title("KLD")
                                )
                            }
                        }


                    } else {
                        Toast.makeText(
                            applicationContext,
                            "No data in this area",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                is NearbyLoadingEvent -> {
                    Log.d("KLD", "Loading")
                    loadingCard.show()
                }
                is NearbyErrorEvent -> {
                    Toast.makeText(applicationContext, event.errorMessage, Toast.LENGTH_LONG).show()
                    loadingCard.gone()
                }
                else -> {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
                    loadingCard.gone()

                }
            }
        })


    }

    private fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable?.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable?.intrinsicWidth!!,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onMyLocationButtonClick(): Boolean {
        return true
    }

    override fun onMyLocationClick(p0: Location) {

    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    companion object {
        private const val LOCATION_PERMISSION = 42
        private const val REQUEST_CHECK_SETTINGS = 40
    }
}
