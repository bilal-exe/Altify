package bilal.altify.domain.prefrences

import bilal.altify.presentation.DarkThemeConfig
import bilal.altify.presentation.prefrences.AltPreferencesState
import bilal.altify.presentation.prefrences.ArtworkDisplayConfig
import bilal.altify.presentation.prefrences.BackgroundColourConfig
import bilal.altify.presentation.prefrences.BackgroundStyleConfig
import bilal.altify.presentation.prefrences.FullScreenMusicInfoAlignment
import bilal.altify.presentation.prefrences.MusicInfoAlignmentConfig
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    val state: Flow<AltPreferencesState>

    suspend fun setSpotifyToken(token: String?)

    suspend fun setDarkThemeConfig(config: DarkThemeConfig)

    suspend fun setBackgroundStyleConfig(config: BackgroundStyleConfig)

    suspend fun setArtworkDisplayConfig(config: ArtworkDisplayConfig)

    suspend fun setMusicInfoAlignmentConfig(config: MusicInfoAlignmentConfig)

    suspend fun setBackgroundColourConfig(config: BackgroundColourConfig)

    suspend fun setFullscreenInfoAlignmentConfig(config: FullScreenMusicInfoAlignment)

}