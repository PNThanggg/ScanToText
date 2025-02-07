package com.hoicham.orc.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.hoicham.orc.datastore.model.ApplicationPreferences
import com.hoicham.orc.datastore.serializer.ApplicationPreferencesSerializer
import com.hoicham.orc.di.AppDispatchers
import com.hoicham.orc.di.ApplicationScope
import com.hoicham.orc.di.Dispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

private const val APP_PREFERENCES_DATASTORE_FILE = "app_preferences.json"

/**
 * Hilt module for DataStore preferences
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    /**
     * Provides DataStore for application preferences
     * @param context Application context
     * @param ioDispatcher IO dispatcher for background operations
     * @param scope Application coroutine scope
     * @return DataStore for ApplicationPreferences
     */
    @Provides
    @Singleton
    fun provideAppPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(AppDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
    ): DataStore<ApplicationPreferences> {
        return DataStoreFactory.create(
            serializer = ApplicationPreferencesSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
            produceFile = { context.dataStoreFile(APP_PREFERENCES_DATASTORE_FILE) },
        )
    }
}
