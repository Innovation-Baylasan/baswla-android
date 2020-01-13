package org.baylasan.sudanmap.ui.place

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_places.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.ui.addentity.AddEntityActivity

class PlacesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)
        addPlaceButton.setOnClickListener {
            startActivity(Intent(this, AddEntityActivity::class.java))
        }
    }
}
