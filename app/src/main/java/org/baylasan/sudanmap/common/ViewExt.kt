package org.baylasan.sudanmap.common

import android.R.attr.duration
import android.app.Activity
import android.content.Context
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.children


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

fun String.toClickableSpan( onClick: () -> Unit): SpannableStringBuilder {
    val spannableStringBuilder = SpannableStringBuilder(this)

    spannableStringBuilder.setSpan(object : ClickableSpan() {
        override fun onClick(widget: View) {
            onClick.invoke()
        }
    }, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return spannableStringBuilder
}
fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        val startIndexOfLink = this.text.toString().indexOf(link.first)
        spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    this.movementMethod = LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}
fun ViewGroup.inflate(layoutRes: Int, attachToRoot: Boolean = false): View? {
    return LayoutInflater.from(context)
        .inflate(layoutRes, this, attachToRoot)
}


fun Context.toast(message: String?) {
    if (message == null || message.isEmpty())
        return
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

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.expand() {
    measure(
        WindowManager.LayoutParams.MATCH_PARENT
        ,
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    )
    val targetHeight = measuredHeight
    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    layoutParams.height = 1
    visibility = View.VISIBLE
    val a: Animation = object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation?
        ) {
            layoutParams.height =
                if (interpolatedTime == 1f) targetHeight else (targetHeight * interpolatedTime).toInt()
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }
    a.duration = duration.toLong()
    startAnimation(a)
}

fun View.collapse() {
    val initialHeight = measuredHeight
    val a: Animation = object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation?
        ) {
            if (interpolatedTime == 1f) {
                visibility = View.GONE
            } else {
                layoutParams.height =
                    initialHeight - (initialHeight * interpolatedTime).toInt()
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }
    a.duration = duration.toLong()
    startAnimation(a)
}

fun View.gone() {
    if (isVisible) {
        visibility = View.GONE
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
}

fun View.disable() {
    isEnabled = false
}

fun Activity.disableTouch() {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )
}

fun Activity.enableTouch() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

}

fun View.disableChildren() {
    disable()
    (this as? ViewGroup)?.children?.forEach {
        it.disable()
    }
}

fun View.enableChildren() {
    enable()
    (this as? ViewGroup)?.children?.forEach {
        it.enable()
    }
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


private fun View.alphaAnimation(duration: Long, visibility: Int) {
    val alphaAnimation =
        if (visibility == VISIBLE) AlphaAnimation(0f, 1f) else AlphaAnimation(1f, 0f)
    alphaAnimation.duration = duration
    alphaAnimation.fillAfter = true
    startAnimation(alphaAnimation)
}

fun View.showAlpha() {
    alphaAnimation(200, VISIBLE)
}

fun View.hideAlpha() {
    alphaAnimation(200, GONE)
}
