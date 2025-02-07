package com.hoicham.orc.core.utils

import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.safeNav(directions: NavDirections) {
    currentDestination?.getAction(directions.actionId)?.let {
        navigate(directions)
    }
}