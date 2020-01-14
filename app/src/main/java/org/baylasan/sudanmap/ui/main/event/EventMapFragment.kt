package org.baylasan.sudanmap.ui.main.event


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.search_bar_layout.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.ui.eventsearch.EventSearchFragment
import org.baylasan.sudanmap.ui.main.MainActivity
import org.baylasan.sudanmap.utils.GpsChecker


class EventMapFragment : Fragment(R.layout.fragment_event_map), GpsChecker.OnGpsListener {
    private lateinit var activity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        GpsChecker(activity).turnGPSOn(this)
        mapFragment?.getMapAsync {googleMap ->

        }
        searchField.setOnClickListener {
            openSearchPage()
        }
        searchButton.setOnClickListener {
            openSearchPage()
        }

    }


    private fun openSearchPage() {
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentLayout, EventSearchFragment(), "")
            .addToBackStack("")
            .commit()
    }

    override fun onGpsEnabled() {

    }
}
