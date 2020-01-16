package org.baylasan.sudanmap.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText


fun View.hide() {
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



fun ViewGroup.inflate(layoutRes: Int, attachToRoot: Boolean = false): View? {
    return LayoutInflater.from(context)
        .inflate(layoutRes, this, attachToRoot)
}


fun Context.toast(message: String) {

    Toast.makeText(this, message, Toast.LENGTH_LONG)
        .show()

}


fun Context.toast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_LONG)
        .show()
}



fun View.slideExit() {
    if (translationY == 0f) animate().translationY(-height.toFloat())
}

fun View.slideEnter() {
    if (translationY < 0f) animate().translationY(0f)
}

fun View.show() {
    visibility = View.VISIBLE
}


fun View.gone() {
    if (this != null) {
        if (isVisible) {
            visibility = View.GONE
        }
    }
}

var TextView.textColor: Int
    get() = currentTextColor
    set(v) = setTextColor(v)


//fun Context.color(res: Int): Int = ContextCompat.getColor(this, res)

fun ImageView.color(res: Int) = with(context) {
    setBackgroundColor(res)
}

//inline fun supportsLollipop(code: () -> Unit) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//        code()
//    }
//}
//
//inline fun supportsKitkat(code: () -> Unit) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//        code()
//    }
//}

fun EditText.afterChanged(cb: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            cb(s.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    })
}




val View.isVisible: Boolean
    get() = this.visibility == View.VISIBLE


fun View.enable() {
    isEnabled = true
    alpha = 1.0f
}

fun View.disable() {
    isEnabled = false
    alpha = 0.3f
}

//edit text
fun EditText.clear() {
    setText("")
}

fun EditText.clearError() {
    error = null
}


fun EditText.asString(): String {
    try {
        return text.toString().trim()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}



fun View.hideKeybord() {
    if (requestFocus()) {
        val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
}


