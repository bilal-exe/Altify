package bilal.altify.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import bilal.altify.data.spotify.remote.SpotifyConnectorImpl
import bilal.altify.domain.sources.SpotifyConnector
import bilal.altify.domain.use_case.AltifyUseCases
import bilal.altify.domain.use_case.ExecuteCommandUseCase
import bilal.altify.domain.use_case.GetBrowserStateFlowUseCase
import bilal.altify.domain.use_case.GetCurrentTrackFlowUseCase
import bilal.altify.presentation.prefrences.AltifyPreferencesDataSource
import bilal.altify.presentation.volume_notification.VolumeNotifications
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

    @Provides
    @Singleton
    fun providesVolumeNotifications(@ApplicationContext context: Context) : VolumeNotifications =
        VolumeNotifications(context)

    @Provides
    @Singleton
    fun providesAltifyUseCases() : AltifyUseCases =
        AltifyUseCases(
            currentTrack = GetCurrentTrackFlowUseCase(),
            browser = GetBrowserStateFlowUseCase(),
            commands = ExecuteCommandUseCase()
        )

}