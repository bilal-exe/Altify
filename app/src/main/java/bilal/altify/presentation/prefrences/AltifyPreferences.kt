package bilal.altify.presentation.prefrences

import bilal.altify.presentation.DarkThemeConfig
import bilal.altify.presentation.screens.nowplaying.ArtworkDisplayConfig
import bilal.altify.presentation.screens.nowplaying.BackgroundStyleConfig
import bilal.altify.presentation.screens.nowplaying.MusicInfoAlignmentConfig

interface AltPreference {
    val code: Int
    val title: String
}

data class AltPreferencesState(
    val darkTheme: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val backgroundStyle: BackgroundStyleConfig = BackgroundStyleConfig.SOLID,
    val artworkDisplayConfig: ArtworkDisplayConfig = ArtworkDisplayConfig.NORMAL,
    val musicInfoAlignmentConfig: MusicInfoAlignmentConfig = MusicInfoAlignmentConfig.CENTER
)