package com.hoicham.orc.core.extension

import com.hoicham.orc.core.utils.isOreoPlus
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

fun Long.toSize(): String {
    if (this <= 0) return "0"
    val units = arrayOf("B", "kB", "MB", "GB", "TB")
    val digitGroups = (log10(this.toDouble()) / log10(1024.0)).toInt()
    return DecimalFormat("#.00").format(this / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
}

private val SimpleDateFormat by lazy {
    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
}

fun Long.toLocalizedString(locale: Locale = Locale.getDefault()): String {
    return if (isOreoPlus()) {
        DateFormat.getDateTimeInstance(
            DateFormat.MEDIUM,
            DateFormat.SHORT,
            locale
        ).format(
            Date.from(
                Instant.ofEpochMilli(this)
            )
        )
    } else {
        SimpleDateFormat.format(Date(this))
    }
}
