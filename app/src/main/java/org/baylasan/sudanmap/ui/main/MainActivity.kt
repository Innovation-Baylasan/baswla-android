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
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
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
import org.baylasan.sudanmap.ui.profile.CompanyProfileSheetDialog
import org.baylasan.sudanmap.ui.search.SearchFragment
import org.baylasan.sudanmap.utils.hide
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
        openDrawerMenu.setOnClickListener {
            fragmentLayout.openDrawer(navigationView, true)
        }
        if (doseNotHaveLocationPermission()
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Light_Dialog)
                    .setTitle(R.string.permission_denied_settings_title)
                    .setCancelable(false)
                    .setMessage(R.string.permission_denied_settings_subtitle)
                    .setPositiveButton(R.string.grant) { dialog, _ ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            LOCATION_PERMISSION
                        )
                        dialog.dismiss()

                    }.setNegativeButton(android.R.string.cancel) { dialog, _ ->
                        showSettingsFragment()
                        dialog.dismiss()
                    }
                    .create().show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION
                )

            }
        } else {
            getCurrentLocation()
        }
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapFragment?.getMapAsync { googleMap ->
            this.googleMap = googleMap

            googleMap.setOnMarkerClickListener {
                val entity = (it.tag as Entity)
                Log.d("MEGA", "$entity")
                CompanyProfileSheetDialog.newInstance(entity).show(supportFragmentManager, "")
                true
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
            .debounce(700, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                entityViewModel.loadNearby(it.latitude, it.longitude)
            }, {

            })

        compositeDisposable.add(locationUpdatesDisposable)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.peekHeight = 250
        entities = ArrayList()
        entityEntitiesListAdapter = EntitiesListAdapter(entities,
            object : EntitiesListAdapter.OnItemClick {
                override fun onItemClick(entityDtoDto: Entity) {
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
                    else TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        20f,
                        resources.displayMetrics
                    )
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


    private fun doseNotHaveLocationPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
                != PackageManager.PERMISSION_GRANTED)
    }

    override fun onResume() {
        super.onResume()
        val fragment = supportFragmentManager.findFragmentByTag("perm")
        if (fragment != null) {
            if (!doseNotHaveLocationPermission()) {
                getCurrentLocation()
                initMap()
                supportFragmentManager.beginTransaction().remove(fragment).commit()
            }
        }
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
                showSettingsFragment()
            }
        }
    }

    private fun showSettingsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentLayout, PermissionDeniedFragment(), "perm")
            .commit()
    }


    private fun initMap() {

        googleMap?.isMyLocationEnabled = true


        googleMap?.uiSettings?.isMyLocationButtonEnabled = true

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                updateMapLocation(location)
                // }
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
                        .zoom(12f)
                        .build()
                    googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
//                    entityViewModel.loadNearby(mLastLocation?.latitude!!, mLastLocation.longitude)
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
        errorCard.setOnClickListener {
            val lastLatLng = locationUpdatesSubject.lastOrError()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    entityViewModel.loadNearby(it.latitude, it.longitude)

                }, {

                })

        }
        myLocationFab.setOnClickListener {
            if (canAccessLocation())
                getCurrentLocation()
            else
                requestPermissions()
        }
        entityViewModel.nearbyEvents.observe(this, Observer { event ->

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
                            ).icon(
                                bitmapDescriptorFromVector(
                                    this,
                                    R.drawable.ic_marker
                                )
                            ).title(entity.name)
                            //).icon(BitmapDescriptorFactory.defaultMarker()).title("KLD")
                        )
                        marker?.tag = entity


                    }


                }
                is NearbyLoadingEvent -> {
                    Log.d("KLD", "Loading")
                    loadingCard.show()
                    emptyCard.hide()
                    errorCard.hide()

                }
                else -> {
//                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
                    loadingCard.hide()
                    emptyCard.hide()
                    errorCard.show()


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
