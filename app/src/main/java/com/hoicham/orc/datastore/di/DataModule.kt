package com.hoicham.orc.datastore.di

import com.hoicham.orc.datastore.repository.FilteredTextRepository
import com.hoicham.orc.datastore.repository.IFilteredTextRepository
import com.hoicham.orc.datastore.repository.IScanRepository
import com.hoicham.orc.datastore.repository.LocalPreferencesRepository
import com.hoicham.orc.datastore.repository.PreferencesRepository
import com.hoicham.orc.datastore.repository.ScanRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsPreferencesRepository(
        preferencesRepository: LocalPreferencesRepository,
    ): PreferencesRepository

    @Binds
    fun bindsScanRepository(
        scanRepository: ScanRepository,
    ): IScanRepository

    @Binds
    fun bindsFilteredTextRepository(
        filteredTextRepository: FilteredTextRepository,
    ): IFilteredTextRepository
}
