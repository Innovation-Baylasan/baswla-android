package org.baylasan.sudanmap.ui.main.event


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.SupportMapFragment
import org.baylasan.sudanmap.R



class EventMapFragment : Fragment(R.layout.fragment_event_map) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment?.getMapAsync {

        }

    }

}
