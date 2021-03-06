package org.baylasan.sudanmap.ui.entitysearch

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
import kotlinx.android.synthetic.main.fragment_entity_search.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.common.gone
import org.baylasan.sudanmap.common.setEndDrawableOnTouchListener
import org.baylasan.sudanmap.common.visible
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.ui.entitydetails.EntityDetailsActivity
import org.baylasan.sudanmap.ui.main.MainActivity
import org.baylasan.sudanmap.ui.main.entity.EntitiesListAdapter
import org.baylasan.sudanmap.ui.searchfilter.SearchFilterFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class EntitySearchFragment : Fragment(R.layout.fragment_entity_search),
    EntitiesListAdapter.OnItemClick {

    companion object {
        const val SEARCH_KEY = "keyword"
        @JvmStatic
        fun newInstance(): EntitySearchFragment {
            return EntitySearchFragment()
        }
    }

    private lateinit var activity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    private val viewModel: EntitySearchViewModel by viewModel()
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

        viewModel.events.observe(viewLifecycleOwner, Observer { event ->
            Log.d("MEGA", "$event")
            when (event) {
                is UiState.Success -> {
                    searchRecyclerView.visible()
                    progressBar.gone()
                    emptyView.gone()
                    errorView.gone()
                    searchRecyclerView.adapter =
                        EntitiesListAdapter(list = event.data.toMutableList(), onItemClick = this)
                }
                is UiState.Loading -> {
                    progressBar.visible()
                    searchRecyclerView.gone()
                    emptyView.gone()
                    errorView.gone()

                }
                is UiState.Empty -> {
                    emptyView.visible()
                    progressBar.gone()
                    searchRecyclerView.gone()
                    errorView.gone()
                }
                is UiState.Error -> {
                    errorView.visible()
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



        searchRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

    }

    override fun onItemClick(entity: Entity) {
        val profileIntent = Intent(activity, EntityDetailsActivity::class.java)
        profileIntent.putExtra("entity", entity)
        startActivity(profileIntent)
    }


}