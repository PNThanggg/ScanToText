package com.hoicham.orc.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hoicham.orc.database.entity.FilteredTextModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FilteredTextModelDao {
    @Upsert
    suspend fun upsert(model: FilteredTextModel)

    @Query("SELECT * FROM FILTERED_TEXT_MODEL")
    fun getAllModels(): Flow<List<FilteredTextModel>>

    @Query("SELECT * FROM FILTERED_TEXT_MODEL WHERE scan_id = :scanId")
    fun getModelsByScanId(scanId: Int): Flow<List<FilteredTextModel>>
}