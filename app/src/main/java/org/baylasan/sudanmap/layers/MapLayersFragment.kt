package org.baylasan.sudanmap.layers


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.Transition
import kotlinx.android.synthetic.main.fragment_map_layers.*
import org.baylasan.sudanmap.R

/**
 * A simple [Fragment] subclass.
 */
class MapLayersFragment : Fragment( R.layout.fragment_map_layers) {
    companion object {
        @JvmStatic
        fun newInstance(): MapLayersFragment {
            return MapLayersFragment()
        }
    }

    private lateinit var activity: Activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as Activity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapLayersRecyclerView.layoutManager = GridLayoutManager(activity, 3)

        mapLayersRecyclerView.adapter = MapLayersAdapter()
    }

}
