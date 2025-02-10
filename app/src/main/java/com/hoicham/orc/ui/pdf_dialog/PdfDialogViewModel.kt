package com.hoicham.orc.ui.pdf_dialog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoicham.orc.datastore.repository.ScanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PdfDialogViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val scanRepo: ScanRepository
) : ViewModel() {
    private val args = PdfDialogFragmentArgs.fromSavedStateHandle(savedStateHandle)

    val scan = scanRepo.getScanById(args.pdfScanId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
}