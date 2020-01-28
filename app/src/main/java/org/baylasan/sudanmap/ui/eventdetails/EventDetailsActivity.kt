package org.baylasan.sudanmap.ui.eventdetails

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_event_details.*
import kotlinx.android.synthetic.main.content_event_details.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.openWebPage
import org.baylasan.sudanmap.data.event.model.Event
import org.baylasan.sudanmap.ui.main.entity.load

class EventDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        toolbar.setOnClickListener { finish() }
        val event = intent.getParcelableExtra<Event>("event") as Event
        eventImage.load(event.eventPicture)
        eventName.text = event.eventName
        eventDescription.text = event.description
        applyButton.setOnClickListener {
            openWebPage(event.registrationLink)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onContextItemSelected(item)

    }
}
