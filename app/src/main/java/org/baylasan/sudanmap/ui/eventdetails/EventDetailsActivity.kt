package org.baylasan.sudanmap.ui.eventdetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_event_details.*
import kotlinx.android.synthetic.main.content_event_details.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.event.model.Event
import org.baylasan.sudanmap.ui.main.place.load
import org.baylasan.sudanmap.common.openWebPage

class EventDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)
        val event = intent.getParcelableExtra<Event>("event") as Event
        eventImage.load(event.eventPicture)
        eventName.text = event.eventName
        eventDescription.text = event.description
        applyButton.setOnClickListener {
            openWebPage(event.registrationLink)
        }
    }
}
