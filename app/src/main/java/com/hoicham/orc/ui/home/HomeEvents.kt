package com.hoicham.orc.ui.home

import com.hoicham.orc.database.entity.Scan

sealed class HomeEvents {
    data object ShowLoadingDialog : HomeEvents()

    data class ShowCurrentScanSaved(val id: Int) : HomeEvents()

    data object ShowScanEmpty : HomeEvents()

    data class ShowUndoDeleteScan(val scan: Scan) : HomeEvents()

    data object ShowOnboarding : HomeEvents()

    data object ShowErrorWhenScanning : HomeEvents()

    data object ShowPermissionInfo : HomeEvents()

    data object ShowSupportDialog : HomeEvents()
}