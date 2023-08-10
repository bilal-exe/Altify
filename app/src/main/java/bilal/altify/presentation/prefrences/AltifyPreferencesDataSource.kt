package bilal.altify.presentation.prefrences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import bilal.altify.presentation.DarkThemeConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class AltifyPreferencesDataSource(
    private val altPreferences: DataStore<Preferences>
) {

    private val darkThemeConfigKey = intPreferencesKey("DARK_THEME")
    private val backgroundStyleConfigKey = intPreferencesKey("BACKGROUND_STYLE")
    private val artworkDisplayConfigKey = intPreferencesKey("ARTWORK_DISPLAY")
    private val musicInfoAlignmentConfigKey = intPreferencesKey("MUSIC_INFO_ALIGNMENT")
    private val backgroundColourConfigKey = intPreferencesKey("BACKGROUND_COLOUR")

    val state = altPreferences.data.map { preferences ->
        AltPreferencesState(
            darkTheme = DarkThemeConfig.values()
                .find { it.code == preferences[darkThemeConfigKey] }
                ?: DarkThemeConfig.FOLLOW_SYSTEM,
            backgroundStyle = BackgroundStyleConfig.values()
                .find { it.code == preferences[backgroundStyleConfigKey] }
                ?: BackgroundStyleConfig.SOLID,
            artworkDisplay = ArtworkDisplayConfig.values()
                .find { it.code == preferences[artworkDisplayConfigKey] }
                ?: ArtworkDisplayConfig.NORMAL,
            musicInfoAlignment = MusicInfoAlignmentConfig.values()
                .find { it.code == preferences[musicInfoAlignmentConfigKey] }
                ?: MusicInfoAlignmentConfig.CENTER,
            backgroundColour = BackgroundColourConfig.values()
                .find { it.code == preferences[backgroundColourConfigKey] }
                ?: BackgroundColourConfig.VIBRANT
        )
    }
        .stateIn(CoroutineScope(IO), SharingStarted.Eagerly, AltPreferencesState())

    suspend fun setDarkThemeConfig(config: DarkThemeConfig) {
        altPreferences.edit { it[darkThemeConfigKey] = config.code }
    }

    suspend fun setBackgroundStyleConfig(config: BackgroundStyleConfig) {
        altPreferences.edit { it[backgroundStyleConfigKey] = config.code }
    }

    suspend fun setArtworkDisplayConfig(config: ArtworkDisplayConfig) {
        altPreferences.edit { it[artworkDisplayConfigKey] = config.code }
    }

    suspend fun setMusicInfoAlignmentConfig(config: MusicInfoAlignmentConfig) {
        altPreferences.edit { it[musicInfoAlignmentConfigKey] = config.code }
    }

    suspend fun setBackgroundColourConfig(config: BackgroundColourConfig) {
        altPreferences.edit { it[backgroundColourConfigKey] = config.code }
    }

}