package bilal.altify.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import bilal.altify.data.prefrences.PreferencesRepositoryImpl
import bilal.altify.data.spotify.respoitories.AccessTokenRepositoryImpl
import bilal.altify.data.spotify.respoitories.SpotifySourceImpl
import bilal.altify.domain.prefrences.PreferencesRepository
import bilal.altify.domain.spotify.repositories.AccessTokenRepository
import bilal.altify.domain.spotify.repositories.SpotifyConnector
import bilal.altify.domain.spotify.repositories.SpotifySource
import bilal.altify.domain.spotify.use_case.AltifyUseCases
import bilal.altify.domain.spotify.use_case.ExecuteCommandUseCase
import bilal.altify.domain.spotify.use_case.GetBrowserStateFlowUseCase
import bilal.altify.domain.spotify.use_case.GetCurrentTrackFlowUseCase
import bilal.altify.presentation.volume_notification.VolumeNotifications
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesSpotifyConnector(@ApplicationContext context: Context): SpotifyConnector =
        SpotifySourceImpl(context)

    @Provides
    @Singleton
    fun providesSpotifySource(spotifyConnector: SpotifyConnector): SpotifySource =
        spotifyConnector

    @Provides
    @Singleton
    fun providesAltifyPreferencesDataSource(@ApplicationContext context: Context): PreferencesRepository =
        PreferencesRepositoryImpl(
            PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("Settings") }
            )
        )

    @Provides
    @Singleton
    fun providesAccessTokenRepository(@ApplicationContext context: Context): AccessTokenRepository =
        AccessTokenRepositoryImpl(
            PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("AccessToken") }
            )
        )

    @Provides
    @Singleton
    fun providesVolumeNotifications(@ApplicationContext context: Context): VolumeNotifications =
        VolumeNotifications(context)

    @Provides
    @Singleton
    fun providesAltifyUseCases(): AltifyUseCases =
        AltifyUseCases(
            currentTrack = GetCurrentTrackFlowUseCase(),
            browser = GetBrowserStateFlowUseCase(),
            commands = ExecuteCommandUseCase()
        )

    private val json = Json { ignoreUnknownKeys = true }
    private const val baseUrl = "https://api.spotify.com"

    @Provides
    @Singleton
    fun providesRetrofitInstance(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(baseUrl)
            .build()

}