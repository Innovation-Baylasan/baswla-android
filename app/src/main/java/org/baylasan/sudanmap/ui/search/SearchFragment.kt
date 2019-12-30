package org.baylasan.sudanmap.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_search.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.ui.main.DataEvent
import org.baylasan.sudanmap.ui.main.EmptyEvent
import org.baylasan.sudanmap.ui.main.LoadingEvent
import org.baylasan.sudanmap.ui.profile.CompanyProfileActivity
import org.baylasan.sudanmap.ui.searchfilter.SearchFilterFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment(R.layout.fragment_search) {

    companion object {
        const val SEARCH_KEY = "keyword"
        @JvmStatic
        fun newInstance(keyword: String): SearchFragment {
            val fragment = SearchFragment()
            val bundle = Bundle()
            bundle.putString(SEARCH_KEY, keyword)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val viewModel: SearchViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val keyword = arguments?.getString(SEARCH_KEY)

        if (keyword != null)
            viewModel.findEntitiesWithKeyword(keyword)
        viewModel.events.observe(this, Observer { event ->
            when (event) {
                is DataEvent -> {
                    searchRecyclerView.adapter = SearchAdapter(list = event.entities, onClick = {
                        val profileIntent = Intent(activity, CompanyProfileActivity::class.java)
                        profileIntent.putExtra("entity", it)
                        startActivity(profileIntent)
                    })
                }
                is LoadingEvent -> {
                }
                is EmptyEvent -> {

                }
            }

        })
        searchBackBtn.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }


        filterButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentLayout, SearchFilterFragment.newInstance(), "searchFilter")
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ?.addToBackStack(null)
                ?.commit()
        }


        val itemDecor =
            DividerItemDecoration(searchRecyclerView.context, DividerItemDecoration.HORIZONTAL)
        // itemDecor.setDrawable(ContextCompat.getDrawable(activity!!, R.drawable.divider)!!)

        searchRecyclerView.addItemDecoration(itemDecor)
        searchRecyclerView.layoutManager = LinearLayoutManager(activity)

    }
}