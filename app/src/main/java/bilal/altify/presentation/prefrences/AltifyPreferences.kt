package bilal.altify.presentation.prefrences

import bilal.altify.presentation.DarkThemeConfig

interface AltPreference {
    val code: Int
    val title: String
}

enum class ArtworkDisplayConfig(override val code: Int, override val title: String) : AltPreference {
    NORMAL(0, "Normal"), SPINNING_DISC(1, "Spinning Record")
}

enum class BackgroundStyleConfig(override val code: Int, override val title: String) : AltPreference {
    SOLID(0, "Solid Colour"),
    DIAGONAL_GRADIENT(1, "Diagonal Gradient"),
    VERTICAL_GRADIENT(2, "Vertical Gradient"),
    PLAIN(3, "Plain")
}

enum class MusicInfoAlignmentConfig(override val code: Int, override val title: String) : AltPreference {
    CENTER(0, "Center"), LEFT(1, "Left")
}

data class AltPreferencesState(
    val darkTheme: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val backgroundStyle: BackgroundStyleConfig = BackgroundStyleConfig.SOLID,
    val artworkDisplayConfig: ArtworkDisplayConfig = ArtworkDisplayConfig.NORMAL,
    val musicInfoAlignmentConfig: MusicInfoAlignmentConfig = MusicInfoAlignmentConfig.CENTER
)