package bilal.altify.di

import android.content.Context
import bilal.altify.data.SpotifyController
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
    fun providesSpotifyControllerFactory(@ApplicationContext context: Context): SpotifyController.SpotifyControllerFactory =
        SpotifyController.SpotifyControllerFactory(context)

}