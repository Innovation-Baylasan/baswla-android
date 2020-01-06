package org.baylasan.sudanmap.utils

import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

fun View.gone() {
    visibility = View.GONE
}

 fun View.show() {
    visibility = View.VISIBLE
} fun View.hide() {
    visibility = View.INVISIBLE
}
fun AppCompatEditText.setEndDrawableOnTouchListener(func: AppCompatEditText.() -> Unit) {
    setOnTouchListener { _, event ->
        var consumed = false
        if (event.action == MotionEvent.ACTION_UP) {
            val drawable = compoundDrawablesRelative[2]
            if (drawable != null) {

                if (event.rawX >= (right - drawable.bounds.width())) {
                    func()
                    consumed = true
                }
            }
        }
        consumed

    }
}