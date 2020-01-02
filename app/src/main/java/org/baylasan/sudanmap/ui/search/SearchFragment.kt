package org.baylasan.sudanmap.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import org.baylasan.sudanmap.utils.gone
import org.baylasan.sudanmap.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment(R.layout.fragment_search), TextWatcher {

    companion object {
        const val SEARCH_KEY = "keyword"
        @JvmStatic
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    private val viewModel: SearchViewModel by viewModel()
    override fun onStop() {
        super.onStop()
        queryField.removeTextChangedListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchRecyclerView.layoutManager = LinearLayoutManager(activity)

        queryField.addTextChangedListener(this)
        viewModel.events.observe(this, Observer { event ->
            Log.d("MEGA","$event")
            when (event) {
                is DataEvent -> {
                    searchRecyclerView.show()
                    progressBar.gone()
                    emptyView.gone()
                    errorView.gone()
                    searchRecyclerView.adapter = SearchAdapter(list = event.entityList, onClick = {
                        val profileIntent = Intent(activity, CompanyProfileActivity::class.java)
                        profileIntent.putExtra("entity", it)
                        startActivity(profileIntent)
                    })
                }
                is LoadingEvent -> {
                    progressBar.show()
                    searchRecyclerView.gone()
                    emptyView.gone()
                    errorView.gone()

                }
                is EmptyEvent -> {
                    emptyView.show()
                    progressBar.gone()
                    searchRecyclerView.gone()
                    errorView.gone()
                }
                else -> {
                    errorView.show()
                    emptyView.gone()
                    searchRecyclerView.gone()
                    progressBar.gone()
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

    override fun afterTextChanged(s: Editable) {
        viewModel.findEntitiesWithKeyword(s.toString())

    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    }
}