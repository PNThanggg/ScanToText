package com.hoicham.orc.database

import com.hoicham.orc.database.dao.FilteredTextModelDao
import com.hoicham.orc.database.dao.ScanDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    fun provideMediumDao(db: ApplicationDatabase): ScanDao = db.scanDao

    @Provides
    fun provideDirectoryDao(db: ApplicationDatabase): FilteredTextModelDao = db.filteredTextModelDao
}
