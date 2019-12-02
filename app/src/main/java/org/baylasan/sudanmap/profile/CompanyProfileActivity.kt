package org.baylasan.sudanmap.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_company_profile.*
import org.baylasan.sudanmap.R
import kotlin.math.abs

class CompanyProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_profile)


        setSupportActionBar(toolbar)
        appbar.addOnOffsetChangedListener(object : AppBarChangedListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State) {
                if (state == State.EXPANDED) // toolbar.title = companyNameTxt.text
                {
                    profileImage.show()
                    profileToolBarTitleTxt.text = ""
                } else {
                    profileImage.gone()
                    // toolbar.title = companyNameTxt.text
                    profileToolBarTitleTxt.text = companyNameTxt.text
                }
            }

        })
    }
}

private fun View.gone() {
    if (isVisible) visibility = View.GONE
}

private fun View.show() {
    visibility = View.VISIBLE
}


abstract class AppBarChangedListener : AppBarLayout.OnOffsetChangedListener {
    enum class State { EXPANDED, COLLAPSED, IDLE }

    abstract fun onStateChanged(appBarLayout: AppBarLayout?, state: State)
    private var currentState = State.IDLE
    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (verticalOffset == 0) {
            if (currentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED)
            }
            currentState = State.EXPANDED
        } else if (abs(verticalOffset) >= appBarLayout?.totalScrollRange!!) {
            if (currentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED)
            }
            currentState = State.COLLAPSED
        } else {
            if (currentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE)
            }
            currentState = State.IDLE
        }
    }

}