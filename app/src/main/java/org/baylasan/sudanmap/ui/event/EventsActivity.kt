package org.baylasan.sudanmap.ui.event

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_events.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.common.gone
import org.baylasan.sudanmap.common.show
import org.baylasan.sudanmap.ui.addevent.AddEventActivity
import org.baylasan.sudanmap.ui.eventsearch.EventAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventsActivity : AppCompatActivity() {
    private val viewModel by viewModel<EventsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)
        setSupportActionBar(toolbar)

        addEventButton.setOnClickListener {
            startActivity(Intent(this, AddEventActivity::class.java))
        }
        loadingRecyclerView.adapter = LoadingAdapter()
        refreshLayout.setOnRefreshListener {
            viewModel.loadMyEvents()
        }
        viewModel.loadMyEvents()
        viewModel.eventEvent.observe(this, Observer {
            refreshLayout.isRefreshing = false
            if (it is UiState.Loading) {
                eventsLoadingLayout.show()
                emptyEventLayout.gone()
            }
            if (it is UiState.Success) {
                refreshLayout.isRefreshing = false
                eventsLoadingLayout.gone()
                if (it.data.isEmpty()) {
                    emptyEventLayout.show()
                } else {
                    eventsRecyclerView.adapter = EventAdapter(it.data)
                }
            }
            if (it is UiState.Error) {
                refreshLayout.isRefreshing = false
                eventsLoadingLayout.gone()
                emptyEventLayout.gone()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)

    }
}
