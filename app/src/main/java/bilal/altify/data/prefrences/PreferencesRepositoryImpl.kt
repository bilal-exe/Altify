package bilal.altify.data.prefrences

import bilal.altify.domain.prefrences.PreferencesRepository
import bilal.altify.presentation.DarkThemeConfig
import bilal.altify.presentation.prefrences.AltPreferencesState
import bilal.altify.presentation.prefrences.ArtworkDisplayConfig
import bilal.altify.presentation.prefrences.BackgroundColourConfig
import bilal.altify.presentation.prefrences.BackgroundStyleConfig
import bilal.altify.presentation.prefrences.FullScreenMusicInfoAlignment
import bilal.altify.presentation.prefrences.MusicInfoAlignmentConfig
import kotlinx.coroutines.flow.Flow

class PreferencesRepositoryImpl(
    private val source: DatastorePreferencesDataSource
) : PreferencesRepository {

    override val state get() = source.state

    override suspend fun setDarkThemeConfig(config: DarkThemeConfig) =
        source.setDarkThemeConfig(config = config)

    override suspend fun setBackgroundStyleConfig(config: BackgroundStyleConfig) =
        source.setBackgroundStyleConfig(config = config)

    override suspend fun setArtworkDisplayConfig(config: ArtworkDisplayConfig) =
        source.setArtworkDisplayConfig(config = config)

    override suspend fun setMusicInfoAlignmentConfig(config: MusicInfoAlignmentConfig) =
        source.setMusicInfoAlignmentConfig(config = config)

    override suspend fun setBackgroundColourConfig(config: BackgroundColourConfig) =
        source.setBackgroundColourConfig(config = config)

    override suspend fun setFullscreenInfoAlignmentConfig(config: FullScreenMusicInfoAlignment) =
        source.setFullscreenInfoAlignmentConfig(config = config)

}