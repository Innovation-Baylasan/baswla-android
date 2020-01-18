package org.baylasan.sudanmap.ui.event

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_events.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.ui.addevent.AddEventActivity

class EventsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)
        addEventButton.setOnClickListener {
            startActivity(Intent(this,AddEventActivity::class.java))
        }
    }
}
