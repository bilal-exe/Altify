package bilal.altify.presentation.screens.nowplaying.browse

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import bilal.altify.R
import bilal.altify.domain.model.AltListItem
import bilal.altify.domain.model.AltTrack
import bilal.altify.presentation.prefrences.AltPreferencesState
import bilal.altify.presentation.prefrences.BackgroundColourConfig
import bilal.altify.presentation.screens.nowplaying.current_track.bodyColor
import bilal.altify.presentation.util.AltText
import bilal.altify.presentation.util.getColor

@Composable
fun Browser(
    preferences: AltPreferencesState,
    palette: Palette? = null,
    track: AltTrack?,
    listItems: List<AltListItem>,
    darkTheme: Boolean,
    getThumbnail: (String) -> Unit
) {

    val backgroundColor = if (palette == null) {
        when {
            darkTheme -> Color.Black
            else -> Color.White
        }
    } else {
        when (preferences.backgroundColour) {
            BackgroundColourConfig.VIBRANT -> palette.vibrantSwatch
            BackgroundColourConfig.MUTED -> palette.mutedSwatch
        }?.getColor() ?: MaterialTheme.colorScheme.surface
    }

    BrowserSolidBackground(backgroundColor = backgroundColor) {
        when {
            listItems.isEmpty() ->
                EmptyListItems(preferences, palette, darkTheme)
            else ->
                ItemsList(
                    listItems,
                    track,
                    palette
                )
        }
    }
}

@Composable
private fun BrowserSolidBackground(
    backgroundColor: Color,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .background(color = backgroundColor)
    ) { content() }
}

@Composable
private fun EmptyListItems(
    preferences: AltPreferencesState,
    palette: Palette?,
    darkTheme: Boolean
) {
    Column(
        modifier = Modifier
            .padding(vertical = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.error),
            contentDescription = "",
            tint = bodyColor
        )
        AltText(
            text = "Nothing to browse..."
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyPreview() {
    Browser(
        preferences = AltPreferencesState(),
        palette = null,
        track = null,
        listItems = emptyList(),
        darkTheme = true,
        getThumbnail = {}
    )
}
