package com.hoicham.orc.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationPreferences(
    val firstLaunch: Boolean = false,
    val scanCount: Int = 6,
)
