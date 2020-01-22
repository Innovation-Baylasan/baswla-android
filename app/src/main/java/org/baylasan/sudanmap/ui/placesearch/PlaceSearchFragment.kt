package org.baylasan.sudanmap.ui.placesearch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_place_search.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.ui.main.MainActivity
import org.baylasan.sudanmap.ui.main.place.DataEvent
import org.baylasan.sudanmap.ui.main.place.EmptyEvent
import org.baylasan.sudanmap.ui.main.place.EntitiesListAdapter
import org.baylasan.sudanmap.ui.main.place.LoadingEvent
import org.baylasan.sudanmap.ui.placedetails.PlaceDetailsActivity
import org.baylasan.sudanmap.ui.searchfilter.SearchFilterFragment
import org.baylasan.sudanmap.common.gone
import org.baylasan.sudanmap.common.setEndDrawableOnTouchListener
import org.baylasan.sudanmap.common.show
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class PlaceSearchFragment : Fragment(R.layout.fragment_place_search), EntitiesListAdapter.OnItemClick {

    companion object {
        const val SEARCH_KEY = "keyword"
        @JvmStatic
        fun newInstance(): PlaceSearchFragment {
            return PlaceSearchFragment()
        }
    }

    private lateinit var activity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    private val viewModel: PlaceSearchViewModel by viewModel()
    override fun onStop() {
        super.onStop()
        disposable.dispose()
    }

    private lateinit var disposable: Disposable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queryField.setEndDrawableOnTouchListener {
            queryField.setText("")
        }

        disposable = queryField.textChanges()
            .skip(1)
            .filter { it.isNotBlank() || it.isNotEmpty() }
            .map(CharSequence::toString)
            .debounce(500, TimeUnit.MILLISECONDS)
            .throttleLast(200, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewModel.findEntitiesWithKeyword(it)
            }, {
                it.printStackTrace()
            })

        viewModel.events.observe(this, Observer { event ->
            Log.d("MEGA", "$event")
            when (event) {
                is DataEvent -> {
                    searchRecyclerView.show()
                    progressBar.gone()
                    emptyView.gone()
                    errorView.gone()
                    searchRecyclerView.adapter =
                        EntitiesListAdapter(list = event.entityList, onItemClick = this)
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
            activity.supportFragmentManager.popBackStack()
        }


        filterButton.setOnClickListener {
            activity.supportFragmentManager.beginTransaction()
                .add(R.id.fragmentLayout, SearchFilterFragment.newInstance(), "searchFilter")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit()
        }



        searchRecyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

    }

    override fun onItemClick(entity: Entity) {
        val profileIntent = Intent(activity, PlaceDetailsActivity::class.java)
        profileIntent.putExtra("entity", entity)
        startActivity(profileIntent)
    }


}