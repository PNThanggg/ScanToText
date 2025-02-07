package com.hoicham.orc.datastore.repository

import com.hoicham.orc.database.entity.FilteredTextModel
import kotlinx.coroutines.flow.Flow

interface IFilteredTextRepository {
    fun getAllModels(): Flow<List<FilteredTextModel>>

    fun getModelsByScanId(scanId: Int): Flow<List<FilteredTextModel>>

    fun insertModel(model: FilteredTextModel)
}