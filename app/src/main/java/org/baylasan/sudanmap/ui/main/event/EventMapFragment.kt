package org.baylasan.sudanmap.ui.main.event


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.search_bar_layout.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.event.model.Event
import org.baylasan.sudanmap.domain.LocationViewModel
import org.baylasan.sudanmap.ui.eventdetails.EventDetailsSheetDialog
import org.baylasan.sudanmap.ui.eventsearch.EventSearchFragment
import org.baylasan.sudanmap.ui.main.MainActivity
import org.baylasan.sudanmap.utils.canEnableLocationButton
import org.baylasan.sudanmap.utils.zoomToMyLocation
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class EventMapFragment : Fragment(R.layout.fragment_event_map) {
    private lateinit var activity: MainActivity
    private lateinit var googleMap: GoogleMap
    private val viewModel: EventViewModel by viewModel()
    private val picasso: Picasso by inject()
    private val locationViewModel: LocationViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity

        viewModel.loadEvents()


        viewModel.errorLiveData.observe(this, Observer {

        })


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        openDrawerMenu.setOnClickListener {
            activity.openDrawer()
        }

        mapFragment?.getMapAsync { googleMap ->
            googleMap.setOnMarkerClickListener {
                val event = it.tag as Event
                EventDetailsSheetDialog.newInstance(event).show(childFragmentManager, "")
                true
            }
            val canEnableLocationButton = activity.canEnableLocationButton()
            Log.d("MEGA", "CAN Show my location $canEnableLocationButton")
            googleMap.isMyLocationEnabled = canEnableLocationButton
            googleMap.uiSettings.isMyLocationButtonEnabled = canEnableLocationButton
            googleMap.uiSettings.isMapToolbarEnabled = false
            locationViewModel.getLocationUpdates().observe(this, Observer {
                googleMap.zoomToMyLocation(it.latitude, it.longitude)
            })
            this.googleMap = googleMap
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.style))

        }
        searchField.setOnClickListener {
            openSearchPage()
        }
        searchButton.setOnClickListener {
            openSearchPage()
        }
        viewModel.dataLiveData.observe(this, Observer { events ->
            events.events.forEach { event ->
                val location = event.location
                val marker = googleMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            location.lat,
                            location.long
                        )
                    ).icon(BitmapDescriptorFactory.fromResource(R.drawable.event_marker))
                )
                marker.tag = event


            }
        })
    }


    private fun openSearchPage() {
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentLayout, EventSearchFragment(), "")
            .addToBackStack("")
            .commit()
    }

}
