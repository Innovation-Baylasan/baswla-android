package org.baylasan.sudanmap.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

fun View.gone() {
    animate().translationY(height.toFloat())
        .alpha(0.0f)
        .setDuration(300)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                super.onAnimationEnd(animation, isReverse)
                visibility = View.GONE
            }
        })
}


fun View.show() {
    animate().translationY(height.toFloat())
        .alpha(1.0f)
        .setDuration(300)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                super.onAnimationEnd(animation, isReverse)
                visibility = View.VISIBLE
            }
        })
}