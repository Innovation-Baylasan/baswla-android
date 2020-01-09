package org.baylasan.sudanmap.ui.addentity

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginEnd
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMarginsRelative
import androidx.core.widget.NestedScrollView
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_add_entity.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.ui.view.AppBarChangedListener

class AddEntityActivity : AppCompatActivity() {
    private val onStateChanged = object : AppBarChangedListener() {
        override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
            if (state == State.COLLAPSED) {
                toolbar.setBackgroundColor(
                    ContextCompat.getColor(
                        this@AddEntityActivity,
                        R.color.colorAccent
                    )
                )
            }
            else {
                toolbar.setBackgroundColor(
                    ContextCompat.getColor(
                        this@AddEntityActivity,
                        android.R.color.transparent
                    )
                )
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_entity)
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync {

        }
        appBar.addOnOffsetChangedListener(onStateChanged)


    }

    override fun onStop() {
        super.onStop()
        appBar.removeOnOffsetChangedListener(onStateChanged)
    }


}
