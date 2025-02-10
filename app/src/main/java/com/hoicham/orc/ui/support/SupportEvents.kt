package com.hoicham.orc.ui.support

sealed interface SupportEvents {
    data object SupportGiven : SupportEvents

    data class ErrorOccurred(val message: String, val debugMessage: String) : SupportEvents
}