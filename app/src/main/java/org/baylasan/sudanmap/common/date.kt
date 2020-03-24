package org.baylasan.sudanmap.common

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.toyyyyMMddHHmmss(): String {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(this.time)
}

fun String.fromYYYYMMddTHHmmss(): Date {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH).parse(this) ?: Date()
}