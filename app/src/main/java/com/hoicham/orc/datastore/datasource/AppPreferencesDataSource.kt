package com.hoicham.orc.datastore.datasource

import androidx.datastore.core.DataStore
import com.hoicham.orc.datastore.model.ApplicationPreferences
import timber.log.Timber
import javax.inject.Inject

class AppPreferencesDataSource @Inject constructor(
    private val appPreferences: DataStore<ApplicationPreferences>,
) : PreferencesDataSource<ApplicationPreferences> {

    override val preferences = appPreferences.data

    override suspend fun update(
        transform: suspend (ApplicationPreferences) -> ApplicationPreferences,
    ) {
        try {
            appPreferences.updateData(transform)
        } catch (ioException: Exception) {
            Timber.tag("AppPlayerPreferences").e("Failed to update app preferences: $ioException")
        }
    }
}
