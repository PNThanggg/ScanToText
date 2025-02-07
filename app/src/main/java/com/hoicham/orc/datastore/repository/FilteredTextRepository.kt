package com.hoicham.orc.datastore.repository

import com.hoicham.orc.database.dao.FilteredTextModelDao
import com.hoicham.orc.database.entity.FilteredTextModel
import com.hoicham.orc.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class FilteredTextRepository @Inject constructor(
    private val filteredTextModelDao: FilteredTextModelDao,
    @ApplicationScope private val applicationScope: CoroutineScope,
) : IFilteredTextRepository {
    override fun getAllModels() = filteredTextModelDao.getAllModels()

    override fun getModelsByScanId(scanId: Int) = filteredTextModelDao.getModelsByScanId(scanId)

    override fun insertModel(model: FilteredTextModel) {
        applicationScope.launch {
            filteredTextModelDao.upsert(model)
        }
    }
}