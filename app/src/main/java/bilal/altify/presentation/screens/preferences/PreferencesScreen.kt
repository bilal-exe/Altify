package bilal.altify.presentation.screens.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import bilal.altify.presentation.DarkThemeConfig
import bilal.altify.presentation.prefrences.AltPreference
import bilal.altify.presentation.prefrences.AltPreferencesState
import bilal.altify.presentation.prefrences.ArtworkDisplayConfig
import bilal.altify.presentation.prefrences.BackgroundColourConfig
import bilal.altify.presentation.prefrences.BackgroundStyleConfig
import bilal.altify.presentation.prefrences.MusicInfoAlignmentConfig
import bilal.altify.presentation.util.SetStatusBarColor

private var backgroundColor by mutableStateOf(Color.White)
private var textColor by mutableStateOf(Color.Black)
private var radioButtonUnselected by mutableStateOf(Color.DarkGray)

@Composable
fun PreferencesScreen(
    navToNowPlaying: () -> Unit,
    viewModel: PreferencesViewModel = hiltViewModel()
) {
    val prefState by viewModel.state.collectAsState(initial = AltPreferencesState())

    val setDarkThemeConfig = viewModel::setDarkThemeConfig
    val setBackgroundStyleConfig = viewModel::setBackgroundStyleConfig
    val setArtworkDisplayConfig = viewModel::setArtworkDisplayConfig
    val setMusicInfoAlignmentConfig = viewModel::setMusicInfoAlignmentConfig
    val setBackgroundColourConfig = viewModel::setBackgroundColourConfig

    when (prefState.darkTheme) {
        DarkThemeConfig.FOLLOW_SYSTEM ->
            if (isSystemInDarkTheme()) setDarkModeColors() else setLightModeColors()
        DarkThemeConfig.LIGHT ->
            setLightModeColors()
        DarkThemeConfig.DARK ->
            setDarkModeColors()
    }
    
    SetStatusBarColor(color = backgroundColor)

    PreferencesScreen(
        prefState = prefState,
        setDarkThemeConfig = setDarkThemeConfig,
        setBackgroundStyleConfig = setBackgroundStyleConfig,
        setArtworkDisplayConfig = setArtworkDisplayConfig,
        setMusicInfoAlignmentConfig = setMusicInfoAlignmentConfig,
        setBackgroundColourConfig = setBackgroundColourConfig,
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
    setMusicInfoAlignmentConfig: (MusicInfoAlignmentConfig) -> Unit,
    setBackgroundColourConfig: (BackgroundColourConfig) -> Unit,
    navToNowPlaying: () -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .background(backgroundColor)
            .padding(12.dp)
    ) {
        SettingsTopAppBar(
            title = "Settings",
            icon = Icons.Default.ArrowBack,
            iconContentDescription = "",
            onIconClick = navToNowPlaying
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
            selected = prefState.artworkDisplay,
            onClick = setArtworkDisplayConfig as (AltPreference) -> Unit,
            values = ArtworkDisplayConfig.values() as Array<AltPreference>
        )
        SettingSection(
            title = "Music Information Alignment",
            selected = prefState.musicInfoAlignment,
            onClick = setMusicInfoAlignmentConfig as (AltPreference) -> Unit,
            values = MusicInfoAlignmentConfig.values() as Array<AltPreference>,
        )
        SettingSection(
            title = "Background Colour Style",
            selected = prefState.backgroundColour,
            onClick = setBackgroundColourConfig as (AltPreference) -> Unit,
            values = BackgroundColourConfig.values() as Array<AltPreference>
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconContentDescription: String = "",
    onIconClick: (() -> Unit)? = null,
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, fontSize = 36.sp) },
        actions = {
            if (icon != null && onIconClick != null)
                IconButton(onClick = onIconClick) {
                    Icon(
                        imageVector = icon,
                        contentDescription = iconContentDescription,
                        tint = textColor,
                    )
                }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier,
    )
}

@Composable
private fun SettingSection(
    title: String,
    selected: AltPreference,
    onClick: (AltPreference) -> Unit,
    values: Array<AltPreference>,
) {
    SettingsSectionTitle(text = title)
    Column(Modifier.selectableGroup()) {
        values.forEach {
            SettingsChooserRow(text = it.title, selected = it == selected) { onClick(it) }
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
}

@Suppress("SameParameterValue")
@Composable
private fun SettingsSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
        color = textColor
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
            colors = RadioButtonDefaults.colors(
                selectedColor = textColor
            )
        )
        Spacer(Modifier.width(8.dp))
        Text(text, color = textColor)
    }
}

private fun setDarkModeColors() {
    textColor = Color.White
    backgroundColor = Color.Black
    radioButtonUnselected = Color.LightGray
}

private fun setLightModeColors() {
    textColor = Color.Black
    backgroundColor = Color.White
    radioButtonUnselected = Color.DarkGray
}


@Preview(showBackground = true)
@Composable
fun PreferencesScreenPreview() {
    PreferencesScreen(
        prefState = AltPreferencesState(),
        setDarkThemeConfig = {},
        setBackgroundStyleConfig = {},
        setArtworkDisplayConfig = {},
        setMusicInfoAlignmentConfig = {},
        setBackgroundColourConfig = {},
        navToNowPlaying = {},
    )
}