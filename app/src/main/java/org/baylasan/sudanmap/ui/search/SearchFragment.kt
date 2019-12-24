package org.baylasan.sudanmap.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_search.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.ui.searchfilter.SearchFilterFragment


class SearchFragment : Fragment(R.layout.fragment_search) {

    companion object {
        @JvmStatic
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBackBtn.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        filterButton.setOnClickListener {
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentLayout, SearchFilterFragment.newInstance(), "searchFilter")
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ?.addToBackStack(null)
                ?.commit()
        }


        val itemDecor = DividerItemDecoration(searchRecyclerView.context, DividerItemDecoration.HORIZONTAL)
       // itemDecor.setDrawable(ContextCompat.getDrawable(activity!!, R.drawable.divider)!!)

        searchRecyclerView.addItemDecoration(itemDecor)
        searchRecyclerView.adapter =
            SearchAdapter()
        searchRecyclerView.layoutManager = LinearLayoutManager(activity)
    }
}