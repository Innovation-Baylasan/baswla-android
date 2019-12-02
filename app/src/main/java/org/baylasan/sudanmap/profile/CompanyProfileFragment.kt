package org.baylasan.sudanmap.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_company_profile.*
import org.baylasan.sudanmap.R
import kotlin.math.abs


/**
 * A simple [Fragment] subclass.
 */
class CompanyProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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


private fun View.gone() {
    if (isVisible) visibility = View.GONE
}

private fun View.show() {
    visibility = View.VISIBLE
}

/**
 * Creating custom listener to appBarLayout
 * to ease work with scrolling event
 */
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