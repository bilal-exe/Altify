package bilal.altify.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import bilal.altify.data.spotify.remote.SpotifyConnectorImpl
import bilal.altify.domain.repository.SpotifyConnector
import bilal.altify.presentation.prefrences.AltifyPreferencesDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesSpotifyConnector(@ApplicationContext context: Context): SpotifyConnector =
        SpotifyConnectorImpl(context)

    @Provides
    @Singleton
    fun providesAltifyPreferencesDataSource(@ApplicationContext context: Context): AltifyPreferencesDataSource =
        AltifyPreferencesDataSource(
            PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("Settings") }
            )
        )

}