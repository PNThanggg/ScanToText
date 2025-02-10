package com.hoicham.orc.ui.detail_scan

import com.hoicham.orc.database.entity.ExtractionModel
import com.hoicham.orc.database.entity.Scan

data class DetailScanUiState(
    val scan: Scan? = null,
    val filteredTextModels: List<ExtractionModel> = emptyList(),
    val isLoading: Boolean = true,
    val isCreated: Int = 0
)
