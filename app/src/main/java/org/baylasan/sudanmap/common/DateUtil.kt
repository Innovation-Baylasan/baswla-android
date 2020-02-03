package org.baylasan.sudanmap.common

import java.text.SimpleDateFormat
import java.util.*

fun parseDate(date: String): Date? {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()).parse(date)
}