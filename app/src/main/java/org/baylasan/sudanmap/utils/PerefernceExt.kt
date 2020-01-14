/*
 * Copyright (c) 2019.
 * Created by Khalid Mohammed.
 * Last modified 1/3/19 4:33 PM.
 * All rights reserved.
 */

package org.baylasan.sudanmap.utils

import android.content.SharedPreferences

/**
 * Created by khalid on 5/18/18.
 * belong destination baswala.
 */


fun <T> SharedPreferences.putPreference(name: String, value: T) = with(this.edit()) {
    try {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type can't be saved into Preferences for $name value $value")
        }.apply()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun <T> SharedPreferences.findPreference(name: String, default: T): T = with(this) {
    val res: Any = when (default) {
        is Long -> getLong(name, default)
        is String -> getString(name, default)
        is Int -> getInt(name, default)
        is Boolean -> getBoolean(name, default)
        is Float -> getFloat(name, default)
        else -> throw IllegalArgumentException("This type can be saved into Preferences")
    }!!

    res as T
}


fun SharedPreferences.clear() {
    edit().clear().apply()
}