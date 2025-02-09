package com.hoicham.orc.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.hoicham.orc.core.extension.toInt
import com.hoicham.orc.core.utils.getCurrentDateTime
import com.hoicham.orc.database.entity.FilteredTextModel
import com.hoicham.orc.database.entity.Scan
import com.hoicham.orc.datastore.repository.FilteredTextRepository
import com.hoicham.orc.datastore.repository.PreferencesRepository
import com.hoicham.orc.datastore.repository.ScanRepository
import com.hoicham.orc.use_case.entity_extraction.EntityExtractionUseCase
import com.hoicham.orc.use_case.entity_extraction.ExtractionResultModel
import com.hoicham.orc.use_case.text_extract.ScanTextFromImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val prefs: PreferencesRepository,
    private val scanRepo: ScanRepository,
    private val filteredTextModelRepo: FilteredTextRepository,
    private val scanTextFromImageUseCase: ScanTextFromImageUseCase,
    private val entityExtractionUseCase: EntityExtractionUseCase
) : ViewModel() {
    private val _events = Channel<HomeEvents>(capacity = 1)
    val events = _events.receiveAsFlow()

    private val isLoading = MutableStateFlow(true)
    private var homeFragActive = true

    private val listOfScans =
        scanRepo.allScans.distinctUntilChanged().onEach { isLoading.value = false }

    private val listOfPinnedScans =
        scanRepo.allPinnedScans.distinctUntilChanged().onEach { isLoading.value = false }

    private val supportCount = prefs.applicationPreferences.onEach {
        val hasSeen = it.firstLaunch
        if (it.scanCount % 12 == 0 && hasSeen) {
            _events.send(HomeEvents.ShowSupportDialog)
            prefs.updateApplicationPreferences { preferences ->
                preferences.copy(
                    scanCount = preferences.scanCount + 1
                )
            }
        }
    }.launchIn(viewModelScope)

    val state = combine(
        isLoading, listOfScans, listOfPinnedScans
    ) { loading, scans, pinnedScans ->
        HomeUiState(
            isLoading = loading, scans = scans, pinnedScans = pinnedScans
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), HomeUiState())

    init {
        initOnBoarding()
    }

    fun handlePermissionDenied() = viewModelScope.launch {
        _events.send(HomeEvents.ShowPermissionInfo)
    }

    private fun createScan(text: String, extractionResultModels: List<ExtractionResultModel>) =
        viewModelScope.launch {
            if (text.isNotEmpty() or text.isNotBlank()) {
                val scan = Scan(
                    scanText = text,
                    dateCreated = getCurrentDateTime(),
                    dateModified = getCurrentDateTime(),
                    scanTitle = "",
                    isPinned = false
                )

                val result = scanRepo.insertScan(scan)
                val scanId = result.toInt()

                extractionResultModels.forEach { extractedModel ->
                    val model = FilteredTextModel(
                        scanId = scanId,
                        type = extractedModel.type.name.lowercase(),
                        content = extractedModel.content
                    )
                    filteredTextModelRepo.insertModel(model)
                    Timber.tag("DEBUG").d("createScan: model inserted %s", model.content)
                }

                if (homeFragActive) {
                    _events.send(HomeEvents.ShowCurrentScanSaved(scanId)).also {
                        prefs.updateApplicationPreferences {
                            it.copy(
                                scanCount = it.scanCount + 1
                            )
                        }
                    }
                }
            } else {
                _events.send(HomeEvents.ShowScanEmpty)
            }

        }

    private fun showLoadingDialog() = viewModelScope.launch {
        _events.send(HomeEvents.ShowLoadingDialog)
    }

    fun deleteScan(scan: Scan) = viewModelScope.launch {
        scanRepo.deleteScan(scan)
        _events.send(HomeEvents.ShowUndoDeleteScan(scan))
    }

    fun insertScan(scan: Scan) = viewModelScope.launch {
        scanRepo.insertScan(scan)
    }

    private fun initOnBoarding() = viewModelScope.launch {
        val hasSeen = prefs.applicationPreferences.first().firstLaunch
        if (!hasSeen) {
            _events.send(HomeEvents.ShowOnboarding).also {
                prefs.updateApplicationPreferences {
                    it.copy(
                        scanCount = it.scanCount + 1
                    )
                }
            }
        }
    }

    fun handleScan(image: InputImage) {
        showLoadingDialog()
        viewModelScope.launch {
            val completeTextResult = scanTextFromImageUseCase(image)
            completeTextResult.onSuccess { completeText ->
                val extractedEntitiesResult =
                    entityExtractionUseCase(completeText).fold(onSuccess = { it },
                        onFailure = { emptyList() })
                createScan(completeText, extractedEntitiesResult)
            }
            completeTextResult.onFailure {
                Timber.tag("DEBUG").e("Error: %s", it.localizedMessage)
                _events.send(HomeEvents.ShowErrorWhenScanning)
            }
        }
    }

    fun onHomeFrag() {
        homeFragActive = true
    }

    fun moveAwayFromScreen() {
        homeFragActive = false
    }
}