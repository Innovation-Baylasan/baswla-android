package org.baylasan.sudanmap.ui.layers


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_map_layers.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.UiState
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class MapLayersFragment : Fragment(R.layout.fragment_map_layers) {
    private val viewModel: MapLayersViewModel by viewModel()

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

        selectBtn.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.popBackStack()
        }

        mapLayersRecyclerView.layoutManager = GridLayoutManager(activity, 3)
        viewModel.loadCategories()
        val mapLayersAdapter = MapLayersAdapter()

        viewModel.events.observe(this, Observer {
            when (it) {
                is UiState.Success -> {
                    progressBar.visibility = View.GONE
                    mapLayersAdapter.list.addAll(it.data.map { Selectable(item = it) })
                    mapLayersAdapter.notifyDataSetChanged()
                }
                is UiState.Loading -> {
                    progressBar.visibility = View.VISIBLE

                }
                else -> progressBar.visibility = View.GONE
            }

        })
        mapLayersRecyclerView.adapter =
            mapLayersAdapter
    }

}
