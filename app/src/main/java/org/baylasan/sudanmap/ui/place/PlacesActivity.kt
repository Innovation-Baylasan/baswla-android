package org.baylasan.sudanmap.ui.place

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_places.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.ui.addentity.AddEntityActivity

class PlacesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)
        setSupportActionBar(toolbar)

        addPlaceButton.setOnClickListener {
            startActivity(Intent(this, AddEntityActivity::class.java))
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)

    }
}
