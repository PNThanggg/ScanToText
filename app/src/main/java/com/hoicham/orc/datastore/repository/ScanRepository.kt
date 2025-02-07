package com.hoicham.orc.datastore.repository

import com.hoicham.orc.database.dao.ScanDao
import com.hoicham.orc.database.entity.Scan
import com.hoicham.orc.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScanRepository @Inject constructor(
    private val scanDao: ScanDao,
    @ApplicationScope private val applicationScope: CoroutineScope,
) : IScanRepository {

    val allScans = scanDao.getAllScans()

    val allPinnedScans = scanDao.getAllPinnedScans()

    override fun insertScan(scan: Scan) {
        applicationScope.launch {
            scanDao.upsert(scan)
        }
    }

    override fun deleteScan(scan: Scan) {
        applicationScope.launch {
            scanDao.deleteScan(scan)
        }
    }

    override fun updateScan(scan: Scan) {
        applicationScope.launch {
            scanDao.upsert(scan)
        }
    }

    override fun getScanById(id: Int) = scanDao.getScanById(id)
}