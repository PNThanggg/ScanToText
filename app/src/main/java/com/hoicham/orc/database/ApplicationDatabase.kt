package com.hoicham.orc.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hoicham.orc.database.converters.DateConverter
import com.hoicham.orc.database.dao.FilteredTextModelDao
import com.hoicham.orc.database.dao.ScanDao
import com.hoicham.orc.database.entity.FilteredTextModel
import com.hoicham.orc.database.entity.Scan

@Database(
    entities = [
        Scan::class,
        FilteredTextModel::class
   ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(
    DateConverter::class
)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract val scanDao: ScanDao

    abstract val filteredTextModelDao: FilteredTextModelDao

    companion object {
        const val DATABASE_NAME = "scan_db"
    }
}