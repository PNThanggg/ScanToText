package com.hoicham.orc.core.utils

import android.content.Context
import android.content.SharedPreferences

object SharePrefUtils {
    private const val PREF_NAME = "AppName"
    private lateinit var sharePref: SharedPreferences

    fun init(context: Context) {
        if (SharePrefUtils::sharePref.isInitialized) {
            return
        }

        sharePref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun <T> saveKey(key: String, value: T) {
        when (value) {
            is String -> sharePref.edit().putString(key, value).apply()
            is Int -> sharePref.edit().putInt(key, value).apply()
            is Boolean -> sharePref.edit().putBoolean(key, value).apply()
            is Long -> sharePref.edit().putLong(key, value).apply()
            is Float -> sharePref.edit().putFloat(key, value).apply()
        }

    }

    fun getString(key: String): String {
        return sharePref.getString(key, "")?.trim() ?: ""
    }

    fun getStringSet(key: String): Set<String> {
        return sharePref.getStringSet(key, emptySet()) ?: emptySet()
    }

    fun setStringSet(key: String, setString: Set<String>) {
        sharePref.edit().putStringSet(key, setString).apply()
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharePref.getInt(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharePref.getBoolean(key, defaultValue)
    }

    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return sharePref.getLong(key, defaultValue)
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return sharePref.getFloat(key, defaultValue)
    }
}