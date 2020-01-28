package org.baylasan.sudanmap.ui.event

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_events.*
import kotlinx.android.synthetic.main.row_search_result.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.common.show
import org.baylasan.sudanmap.ui.addevent.AddEventActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventsActivity : AppCompatActivity() {
    private val viewModel by viewModel<EventViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)
        setSupportActionBar(toolbar)

        addEventButton.setOnClickListener {
            startActivity(Intent(this, AddEventActivity::class.java))
        }
        loadingRecyclerView.adapter=LoadingAdapter()

        /*viewModel.loadMyEvents()
        viewModel.eventEvent.observe(this, Observer {
                loadingRecyclerView.show()
        })*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)

    }
}
