package org.baylasan.sudanmap.ui.main.entity


import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
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
import kotlinx.android.synthetic.main.fragment_entity_map.*
import kotlinx.android.synthetic.main.network_error_layout.*
import kotlinx.android.synthetic.main.search_bar_layout.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.*
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.domain.LocationViewModel
import org.baylasan.sudanmap.ui.entitydetails.EntityDetailsActivity
import org.baylasan.sudanmap.ui.entitydetails.EntityDetailsSheetDialog
import org.baylasan.sudanmap.ui.entitysearch.EntitySearchFragment
import org.baylasan.sudanmap.ui.main.MainActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class EntityMapFragment : Fragment(R.layout.fragment_entity_map) {

    private val viewModel: EntityViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()
    private lateinit var entityEntitiesListAdapter: EntitiesListAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>
    private var googleMap: GoogleMap? = null
    private val locationUpdatesSubject = PublishSubject.create<LatLng>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var activity: MainActivity
    private val locationViewModel by viewModel<LocationViewModel>()
    private lateinit var entities: ArrayList<Entity>
    private val picasso: Picasso by inject()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            googleMap.uiSettings.isMapToolbarEnabled = false
            locationViewModel
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.style))
            this.googleMap = googleMap
            googleMap.isMyLocationEnabled = activity.canEnableLocationButton()
            getLocation()
            googleMap.setOnMarkerClickListener {
                val entity = (it.tag as Entity)
                EntityDetailsSheetDialog.newInstance(entity).show(fragmentManager!!, "")
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
                        Intent(activity, EntityDetailsActivity::class.java)
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
            .replace(R.id.fragmentLayout, EntitySearchFragment(), "")
            .addToBackStack("")
            .commit()
    }

    private fun observeViewModel() {

        viewModel.events.observe(this, Observer { event ->
            when (event) {

                is UiState.Success -> {
                    Log.d("MEGA", "Data loaded")
                    entityFilterLoading.gone()
                    entityLoading.gone()
                    errorLayout.gone()

                    val data = event.data
                    if (data.isNotEmpty()) {
                        entities.clear()
                        entities.addAll(data)
                        Log.d("KLD", toString())
                        entityEntitiesListAdapter.notifyDataSetChanged()
                    }
                }
                is UiState.Loading -> {
                    entityFilterLoading.visible()
                    entityLoading.visible()
                    errorLayout.gone()
                }
                is UiState.Error -> {
                    errorLayout.visible()
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
                is UiState.Empty -> {
                    loadingCard.hide()
                    emptyCard.visible()
                    errorCard.hide()

                }
                is UiState.Success -> {
                    loadingCard.hide()
                    emptyCard.hide()
                    errorCard.hide()

                    val data = event.data
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


                        picasso.load("http://104.248.145.132/${entity.category.iconPng}")
                            .placeholder(R.drawable.ic_marker_placeholder)
                            .error(R.drawable.ic_marker_placeholder)
                            .into(PicassoMarker(marker))
                    }


                }
                is UiState.Loading -> {
                    loadingCard.visible()
                    emptyCard.hide()
                    errorCard.hide()

                }
                is UiState.Error -> {
                    loadingCard.hide()
                    emptyCard.hide()
                    errorCard.visible()


                }
            }
        })


    }


    private fun loadNearbyPlacesFromCurrentLocation() {
        val target = googleMap?.cameraPosition?.target
        if (target != null)
            viewModel.loadNearby(target.latitude, target.longitude)
    }


    private fun zoomToMyLocation(location: Location) {
        googleMap.zoomToMyLocation(location.latitude, location.longitude)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()

    }

    private fun getLocation() {
        locationViewModel.getLocationUpdates().observe(this, Observer {
            zoomToMyLocation(it.toLocation())
        })
    }

}
