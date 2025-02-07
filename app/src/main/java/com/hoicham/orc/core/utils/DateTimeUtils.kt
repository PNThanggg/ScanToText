package com.hoicham.orc.core.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getCurrentDateTime(): Long {
    return Calendar.getInstance().time.time
}

fun dateAsString(
    dateInMillis: Long,
    format: String = "dd.MM.yyyy HH:mm",
    locale: Locale = Locale.getDefault()
): String {
    val date = Date(dateInMillis)
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(date)
}