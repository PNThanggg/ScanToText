package com.hoicham.orc.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.hoicham.orc.database.entity.Scan
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanDao {
    @Upsert
    suspend fun upsert(scan: Scan)

    @Query("SELECT * FROM scan WHERE is_pinned=0 ORDER BY date_created DESC")
    fun getAllScans(): Flow<List<Scan>>

    @Query("SELECT * FROM scan WHERE is_pinned=1 ORDER BY date_created DESC")
    fun getAllPinnedScans(): Flow<List<Scan>>

    @Delete
    suspend fun deleteScan(scan: Scan)

    @Query("SELECT * FROM scan WHERE scan_id=:id")
    fun getScanById(id: Int): Flow<Scan>
}