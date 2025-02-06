package com.hoicham.orc.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CoreUtils {
    fun formatDateToHumanReadable(time: Long): String? {
        return SimpleDateFormat("MMM dd yyyy", Locale.getDefault()).format(Date(time))
    }
}