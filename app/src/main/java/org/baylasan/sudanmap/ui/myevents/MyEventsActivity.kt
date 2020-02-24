package org.baylasan.sudanmap.ui.myevents

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_my_events.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.common.expiredSession
import org.baylasan.sudanmap.common.gone
import org.baylasan.sudanmap.common.visible
import org.baylasan.sudanmap.data.common.UnAuthorizedException
import org.baylasan.sudanmap.ui.addevent.AddEventActivity
import org.baylasan.sudanmap.ui.eventdetails.EventDetailsActivity
import org.baylasan.sudanmap.ui.eventsearch.EventAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyEventsActivity : AppCompatActivity() {
    private val viewModel by viewModel<MyEventsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_events)
        setSupportActionBar(toolbar)

        addEventButton.setOnClickListener {
            startActivityForResult(Intent(this, AddEventActivity::class.java), 123)
        }
        loadingRecyclerView.adapter = LoadingAdapter()
        refreshLayout.setOnRefreshListener {
            viewModel.loadMyEvents()
        }
        viewModel.loadMyEvents()
        viewModel.eventEvent.observe(this, Observer {
            refreshLayout.isRefreshing = false
            if (it is UiState.Loading) {
                eventsLoadingLayout.visible()
                emptyEventLayout.gone()
                eventsRecyclerView.gone()
            }
            if (it is UiState.Success) {
                refreshLayout.isRefreshing = false
                eventsLoadingLayout.gone()
                if (it.data.isEmpty()) {
                    emptyEventLayout.visible()
                } else {
                    eventsRecyclerView.visible()
                    eventsRecyclerView.adapter = EventAdapter(it.data) {
                        val intent = Intent(this, EventDetailsActivity::class.java)
                        intent.putExtra("event", it)
                        startActivity(intent)
                    }
                    eventsRecyclerView.layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                }
            }
            if (it is UiState.Error) {
                if (it.throwable is UnAuthorizedException) {
                    expiredSession()
                } else {
                    refreshLayout.isRefreshing = false
                    eventsLoadingLayout.gone()
                    emptyEventLayout.gone()
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            viewModel.loadMyEvents()
        }
    }
}
