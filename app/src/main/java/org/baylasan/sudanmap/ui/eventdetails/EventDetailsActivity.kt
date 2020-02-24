package org.baylasan.sudanmap.ui.eventdetails

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_event_details.*
import kotlinx.android.synthetic.main.content_event_details.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.openWebPage
import org.baylasan.sudanmap.common.visible
import org.baylasan.sudanmap.data.event.model.Event
import org.baylasan.sudanmap.ui.main.UserProfileViewModel
import org.baylasan.sudanmap.ui.main.entity.load
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventDetailsActivity : AppCompatActivity() {
    private val userProfileViewModel by viewModel<UserProfileViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        toolbar.setOnClickListener { finish() }

        val event = intent.getParcelableExtra("event") as Event
        applyButton.visible()
        eventImage.load(event.picture ?: "")
        eventName.text = event.name
        eventDescription.text = event.description
        startDate.text=event.startDatetime
        endDate.text=event.endDatetime
        applyButton.setOnClickListener {
            openWebPage(event.registrationLink)
        }
        backButton.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onContextItemSelected(item)

    }
}
