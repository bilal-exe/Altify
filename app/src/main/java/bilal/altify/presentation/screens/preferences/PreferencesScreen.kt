package bilal.altify.presentation.screens.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bilal.altify.presentation.AltifyUIState
import bilal.altify.presentation.DarkThemeConfig
import bilal.altify.presentation.prefrences.AltPreference
import bilal.altify.presentation.prefrences.AltPreferencesState
import bilal.altify.presentation.screens.nowplaying.ArtworkDisplayConfig
import bilal.altify.presentation.screens.nowplaying.BackgroundStyleConfig

private var backgroundColor by mutableStateOf(Color.White)
private var textColor by mutableStateOf(Color.Black)

@Composable
fun PreferencesScreen(
    viewModel: PreferencesViewModel,
    uiState: AltifyUIState,
    navToNowPlaying: () -> Unit
) {
    val prefState by viewModel.state.collectAsState()

    val setDarkThemeConfig = viewModel::setDarkThemeConfig
    val setBackgroundStyleConfig = viewModel::setBackgroundStyleConfig
    val setArtworkDisplayConfig = viewModel::setArtworkDisplayConfig

    PreferencesScreen(
        prefState = prefState,
        setDarkThemeConfig = setDarkThemeConfig,
        setBackgroundStyleConfig = setBackgroundStyleConfig,
        setArtworkDisplayConfig = setArtworkDisplayConfig,
        navToNowPlaying = navToNowPlaying
    )
}

@Suppress("UNCHECKED_CAST")
@Composable
private fun PreferencesScreen(
    prefState: AltPreferencesState,
    setDarkThemeConfig: (DarkThemeConfig) -> Unit,
    setBackgroundStyleConfig: (BackgroundStyleConfig) -> Unit,
    setArtworkDisplayConfig: (ArtworkDisplayConfig) -> Unit,
    navToNowPlaying: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(12.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleLarge,
            color = textColor
        )
        Divider(Modifier.padding(vertical = 16.dp))
        SettingSection(
            title = "Theme",
            selected = prefState.darkTheme,
            onClick = setDarkThemeConfig as ((AltPreference) -> Unit),
            values = DarkThemeConfig.values() as Array<AltPreference>
        )
        SettingSection(
            title = "Background Style",
            selected = prefState.backgroundStyle,
            onClick = setBackgroundStyleConfig as ((AltPreference) -> Unit),
            values = BackgroundStyleConfig.values() as Array<AltPreference>
        )
        SettingSection(
            title = "Artwork Display Style",
            selected = prefState.artworkDisplayConfig,
            onClick = setArtworkDisplayConfig as (AltPreference) -> Unit,
            values = ArtworkDisplayConfig.values() as Array<AltPreference>
        )
    }
}

@Composable
private fun SettingSection(
    title: String,
    selected: AltPreference,
    onClick: (AltPreference) -> Unit,
    values: Array<AltPreference>,
) {
    SettingsSectionTitle(text = "Theme")
    Column(Modifier.selectableGroup()) {
        values.forEach {
            SettingsChooserRow(text = it.title, selected = it == selected) { onClick(it) }
        }
    }
}

@Suppress("SameParameterValue")
@Composable
private fun SettingsSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}

@Composable
fun SettingsChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}


@Preview(showBackground = true)
@Composable
fun PreferencesScreenPreview() {
    PreferencesScreen(
        prefState = AltPreferencesState(),
        setDarkThemeConfig = {},
        setBackgroundStyleConfig = {},
        setArtworkDisplayConfig = {},
        navToNowPlaying = {},
    )
}