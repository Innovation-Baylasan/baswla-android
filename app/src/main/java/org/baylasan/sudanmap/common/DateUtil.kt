package org.baylasan.sudanmap.common

import java.text.SimpleDateFormat
import java.util.*

fun parseDate(date: String): Date? {
    return SimpleDateFormat("yyyy-MM-d HH:mm:ss", Locale.getDefault()).parse(date)
}