package bilal.altify.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import bilal.altify.data.spotify.SpotifyController
import bilal.altify.presentation.prefrences.AltifyPreferencesDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Date
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesSpotifyControllerFactory(@ApplicationContext context: Context): SpotifyController.SpotifyControllerFactory =
        SpotifyController.SpotifyControllerFactory(context)

    @Provides
    @Singleton
    fun providesAltifyPreferencesDataSource(@ApplicationContext context: Context): AltifyPreferencesDataSource =
        AltifyPreferencesDataSource(
            PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("Settings") }
            )
        )

}