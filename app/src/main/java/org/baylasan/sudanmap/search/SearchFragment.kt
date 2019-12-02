package org.baylasan.sudanmap.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_search.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.searchfilter.SearchFilterFragment

class SearchFragment : Fragment(R.layout.fragment_search) {

    companion object {
        @JvmStatic
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filterButton.setOnClickListener {
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentLayout, SearchFilterFragment.newInstance(), "searchFilter")
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ?.addToBackStack(null)
                ?.commit()
        }
        searchRecyclerView.adapter = SearchAdapter()
        searchRecyclerView.layoutManager = LinearLayoutManager(activity)
    }
}