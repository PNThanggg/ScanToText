package com.hoicham.orc.datastore.repository

import com.hoicham.orc.datastore.datasource.AppPreferencesDataSource
import com.hoicham.orc.datastore.model.ApplicationPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalPreferencesRepository @Inject constructor(
    private val appPreferencesDataSource: AppPreferencesDataSource,
) : PreferencesRepository {
    override val applicationPreferences: Flow<ApplicationPreferences>
        get() = appPreferencesDataSource.preferences


    override suspend fun updateApplicationPreferences(
        transform: suspend (ApplicationPreferences) -> ApplicationPreferences,
    ) {
        appPreferencesDataSource.update(transform)
    }
}
