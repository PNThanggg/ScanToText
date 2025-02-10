package com.hoicham.orc.ui.detail_scan

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoicham.orc.core.utils.getCurrentDateTime
import com.hoicham.orc.database.entity.toExtractionModel
import com.hoicham.orc.datastore.repository.FilteredTextRepository
import com.hoicham.orc.datastore.repository.ScanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class DetailScanViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val scanRepository: ScanRepository,
    private val filteredModelsRepository: FilteredTextRepository
) : ViewModel() {
    private val args = DetailScanFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val loading = MutableStateFlow(true)

    private val scan = scanRepository.getScanById(args.scanId)

    private val filteredModels =
        scan.flatMapLatest { filteredModelsRepository.getModelsByScanId(it.scanId.toInt()) }
            .map { it.map { filteredTextModel -> filteredTextModel.toExtractionModel() } }
            .onEach { loading.value = false }

    val state = combine(
        loading, scan, filteredModels
    ) { isLoading, scan, textModels ->
        DetailScanUiState(
            isLoading = isLoading,
            scan = scan,
            filteredTextModels = textModels,
            isCreated = args.isCreated
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), DetailScanUiState())

    private val scanTitle = savedStateHandle.getStateFlow("scan_title", state.value.scan?.scanTitle)
    private val scanContent =
        savedStateHandle.getStateFlow("scan_content", state.value.scan?.scanText)

    private val updateTitleJob = scanTitle.debounce(200).filterNotNull().onEach { title ->
        val updatedScan = state.value.scan?.copy(
            scanTitle = title, dateModified = getCurrentDateTime()
        )
        updatedScan?.let { scanRepository.updateScan(it) }
    }.launchIn(viewModelScope)

    private val updateContentJob = scanContent.debounce(200).filterNotNull().onEach { content ->
        val updatedScan = state.value.scan?.copy(
            scanText = content, dateModified = getCurrentDateTime()
        )
        updatedScan?.let { scanRepository.updateScan(it) }
    }.launchIn(viewModelScope)

    fun onTitleChange(newValue: String) = savedStateHandle.set("scan_title", newValue)
    fun onContentChanged(newValue: String) = savedStateHandle.set("scan_content", newValue)

    fun deleteScan() = viewModelScope.launch {
        val current = state.value.scan
        current?.let { scanRepository.deleteScan(it) }
    }

    fun updateScanPinned() = viewModelScope.launch {
        state.value.scan?.let {
            val updated = it.copy(isPinned = !it.isPinned)
            scanRepository.updateScan(updated)
        }
    }
}