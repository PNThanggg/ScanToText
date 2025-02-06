package com.hoicham.orc.core.extension

import android.content.res.Resources

fun Float.dpToPx(resources: Resources): Float {
//    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)

    val scale = resources.displayMetrics.density
    return this * scale + 0.5f
}

fun Float.spToPx(resources: Resources): Float {
    val scale = resources.displayMetrics.scaledDensity
    return this * scale
}