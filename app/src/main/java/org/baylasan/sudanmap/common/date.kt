package org.baylasan.sudanmap.common

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.toyyyyMMddHHmmss(): String {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(this.time)
}