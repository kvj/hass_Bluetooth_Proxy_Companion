package org.kvj.habtproxy

import android.content.Context
import android.content.SharedPreferences

fun SharedPreferences.getInt(context: Context, key: Int, defaultValue: Int): Int {
    return getString(context.getString(key), context.getString(defaultValue))?.toInt(10) ?: 0
}

fun SharedPreferences.getBool(context: Context, key: Int, defaultValue: Int): Boolean {
    return getBoolean(context.getString(key), context.getString(defaultValue).toBoolean())
}

fun SharedPreferences.getString(context: Context, key: Int, defaultValue: Int): String? {
    return getString(context.getString(key), if (defaultValue != 0) context.getString(defaultValue) else null)
}