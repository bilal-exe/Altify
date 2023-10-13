package bilal.altify.data.prefrences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import bilal.altify.domain.prefrences.PreferencesRepository
import bilal.altify.presentation.DarkThemeConfig
import bilal.altify.presentation.prefrences.AltPreferencesState
import bilal.altify.presentation.prefrences.ArtworkDisplayConfig
import bilal.altify.presentation.prefrences.BackgroundColourConfig
import bilal.altify.presentation.prefrences.BackgroundStyleConfig
import bilal.altify.presentation.prefrences.FullScreenMusicInfoAlignment
import bilal.altify.presentation.prefrences.MusicInfoAlignmentConfig
import kotlinx.coroutines.flow.map


class DatastorePreferencesDataSource(
    private val altPreferences: DataStore<Preferences>
) {

    private val spotifyAccessTokenKey = stringPreferencesKey("SPOTIFY_TOKEN")
    private val darkThemeConfigKey = intPreferencesKey("DARK_THEME")
    private val backgroundStyleConfigKey = intPreferencesKey("BACKGROUND_STYLE")
    private val artworkDisplayConfigKey = intPreferencesKey("ARTWORK_DISPLAY")
    private val musicInfoAlignmentConfigKey = intPreferencesKey("MUSIC_INFO_ALIGNMENT")
    private val backgroundColourConfigKey = intPreferencesKey("BACKGROUND_COLOUR")
    private val fullscreenInfoAlignmentConfigKey = intPreferencesKey("FULLSCREEN_INFO_ALIGNMENT")

    val state = altPreferences.data.map { preferences ->
        AltPreferencesState(
            spotifyAccessToken = run<String?>{
                val token = preferences[spotifyAccessTokenKey]
                if (token.isNullOrEmpty()) null else token
            },
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
                ?: BackgroundColourConfig.VIBRANT,
            fullScreenInfoAlignment = FullScreenMusicInfoAlignment.values()
                .find { it.code == preferences[fullscreenInfoAlignmentConfigKey] }
                ?: FullScreenMusicInfoAlignment.MIDDLE
        )
    }

    suspend fun setSpotifyToken(token: String?) {
        altPreferences.edit { it[spotifyAccessTokenKey] = token ?: "" }
    }

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

    suspend fun setFullscreenInfoAlignmentConfig(config: FullScreenMusicInfoAlignment) {
        altPreferences.edit { it[fullscreenInfoAlignmentConfigKey] = config.code }
    }

}