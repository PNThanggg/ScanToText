package com.hoicham.orc.ui.support

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SupportViewModel @Inject constructor() : ViewModel() {
    private val _events = Channel<SupportEvents>()
    val events = _events.receiveAsFlow()

    private val loading = MutableStateFlow(false)
    private val selectedVendor = MutableStateFlow(Vendor.GOOGLE)

    val state = combine(loading, selectedVendor) { p0, p1 ->
        val updatedVendors = VendorUiModel.vendorUiModels.map {
            VendorUiModel(it.vendor, isSelected = it.vendor.vendorName == p1.vendorName)
        }

        SupportUiState(loading = p0, vendors = updatedVendors)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        SupportUiState(vendors = VendorUiModel.vendorUiModels)
    )

    fun selectVendor(vendor: Vendor) {
        selectedVendor.update { vendor }
    }
}