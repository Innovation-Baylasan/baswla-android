package org.baylasan.sudanmap.ui.addentity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_add_entity.*
import org.baylasan.sudanmap.R

class AddEntityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entity)
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync{

        }
        backButton.setOnClickListener {
            finish()
        }




    }

}
