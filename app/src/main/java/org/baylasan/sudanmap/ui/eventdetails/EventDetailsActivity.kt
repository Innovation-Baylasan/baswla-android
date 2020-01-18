package org.baylasan.sudanmap.ui.eventdetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_event_details.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.ui.view.AppBarChangedListener

class EventDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)
        setSupportActionBar(toolbar)

        app_bar.addOnOffsetChangedListener(object : AppBarChangedListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                if (state == State.COLLAPSED) {

                    supportActionBar?.title = "Art Space event"

                } else {
                    supportActionBar?.title = ""


                }
            }

        })
    }
}
