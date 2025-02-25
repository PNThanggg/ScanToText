package com.hoicham.orc.ui.support

import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.GooglePlay

data class SupportUiState(
    val vendors: List<VendorUiModel> = emptyList(),
    val loading: Boolean = false,
)


data class VendorUiModel(
    val vendor: Vendor, val isSelected: Boolean = false
) {
    companion object {
        val vendorUiModels = Vendor.vendorList.map { VendorUiModel(it) }
    }
}

enum class Vendor(val vendorName: String) {
    GOOGLE("Google Play");

    companion object {
        val vendorList = Vendor.values()

        fun vendorIcon(vendor: Vendor) = when (vendor) {
            GOOGLE -> LineAwesomeIcons.GooglePlay
        }
    }
}
