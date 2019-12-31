package org.baylasan.sudanmap.ui.profile


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.content_company_profile.*
import kotlinx.android.synthetic.main.fragment_company_profile.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.domain.entity.model.Entity
import org.baylasan.sudanmap.ui.main.load
import org.baylasan.sudanmap.ui.main.loadCircle
import org.baylasan.sudanmap.utils.gone
import org.baylasan.sudanmap.utils.show
import kotlin.math.abs



class CompanyProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_company_profile)

        profileBackBtn.setOnClickListener {
            super.onBackPressed()
        }

        val entity = intent?.getParcelableExtra<Entity>("entity")
        entity?.let {
            profileToolBarTitleTxt.text = it.name
            companyNameTxt.text = it.name
            companyDescrition.text = it.description
//            profileCoverImage.load(it.cover)
//            profileImage.loadCircle(it.avatar)

        }


        appbar.addOnOffsetChangedListener(object : AppBarChangedListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State) {
                if (state == State.EXPANDED) // toolbar.title = companyNameTxt.text
                {
                    profileImage.show()
                    profileToolBarTitleTxt.text = ""
                } else {
                    profileImage.gone()
                    profileToolBarTitleTxt.text = companyNameTxt.text
                }
            }

        })
    }


}


/**
 * Creating custom listener to appBarLayout
 * to ease work with scrolling event
 */
abstract class AppBarChangedListener : AppBarLayout.OnOffsetChangedListener {
    enum class State { EXPANDED, COLLAPSED, IDLE }

    abstract fun onStateChanged(appBarLayout: AppBarLayout?, state: State)
    private var currentState =
        State.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (verticalOffset == 0) {
            if (currentState != State.EXPANDED) {
                onStateChanged(
                    appBarLayout,
                    State.EXPANDED
                )
            }
            currentState =
                State.EXPANDED
        } else if (abs(verticalOffset) >= appBarLayout?.totalScrollRange!!) {
            if (currentState != State.COLLAPSED) {
                onStateChanged(
                    appBarLayout,
                    State.COLLAPSED
                )
            }
            currentState =
                State.COLLAPSED
        } else {
            if (currentState != State.IDLE) {
                onStateChanged(
                    appBarLayout,
                    State.IDLE
                )
            }
            currentState =
                State.IDLE
        }
    }

}