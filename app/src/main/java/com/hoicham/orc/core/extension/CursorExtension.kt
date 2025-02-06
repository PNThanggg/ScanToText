package com.hoicham.orc.core.extension

import android.database.Cursor

fun Cursor.getStringValue(key: String, defaultValue: String = ""): String {
    val columnIndex = getColumnIndex(key)
    return if (columnIndex >= 0) {
        getString(columnIndex)
    } else {
        defaultValue
    }
}

fun Cursor.getStringValueOrDefault(key: String, defaultValue: String = ""): String {
    val columnIndex = getColumnIndex(key)
    return if (columnIndex >= 0 && !isNull(columnIndex)) {
        getString(columnIndex)
    } else {
        defaultValue
    }
}

fun Cursor.getIntValue(key: String, defaultValue: Int = -1): Int {
    val columnIndex = getColumnIndex(key)
    return if (columnIndex >= 0) {
        getInt(columnIndex)
    } else {
        defaultValue
    }
}

fun Cursor.getIntValueOrDefault(key: String, defaultValue: Int = -1): Int {
    val columnIndex = getColumnIndex(key)
    return if (columnIndex >= 0 && !isNull(columnIndex)) {
        getInt(columnIndex)
    } else {
        defaultValue
    }
}


fun Cursor.getLongValue(key: String, defaultValue: Long = -1L): Long {
    val columnIndex = getColumnIndex(key)
    return if (columnIndex >= 0) {
        getLong(columnIndex)
    } else {
        defaultValue
    }
}

fun Cursor.getBlobValueOrDefault(key: String, defaultValue: ByteArray = ByteArray(0)): ByteArray {
    val columnIndex = getColumnIndex(key)
    return if (columnIndex >= 0 && !isNull(columnIndex)) {
        getBlob(columnIndex)
    } else {
        defaultValue
    }
}

fun Cursor.getBlobValue(key: String, defaultValue: ByteArray = ByteArray(0)): ByteArray {
    val columnIndex = getColumnIndex(key)
    return if (columnIndex >= 0) {
        getBlob(columnIndex)
    } else {
        defaultValue
    }
}
