package org.baylasan.sudanmap.ui.view

import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

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