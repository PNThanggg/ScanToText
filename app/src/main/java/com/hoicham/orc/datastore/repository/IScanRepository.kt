package com.hoicham.orc.datastore.repository

import com.hoicham.orc.database.entity.Scan
import kotlinx.coroutines.flow.Flow

interface IScanRepository {
    fun insertScan(scan: Scan)

    fun deleteScan(scan: Scan)

    fun updateScan(scan: Scan)

    fun getScanById(id: Int): Flow<Scan>
}