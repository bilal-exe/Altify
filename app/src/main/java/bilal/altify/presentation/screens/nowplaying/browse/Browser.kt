package bilal.altify.presentation.screens.nowplaying.browse

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import bilal.altify.R
import bilal.altify.domain.model.AltListItem
import bilal.altify.domain.model.AltTrack
import bilal.altify.presentation.Command
import bilal.altify.presentation.ContentCommand
import bilal.altify.presentation.ImagesCommand
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
    thumbnailMap: Map<String, Bitmap>,
    executeCommand: (Command) -> Unit
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

    val getRecommended: () -> Unit = {
        executeCommand(ContentCommand.GetRecommended)
    }
    val playItem: (AltListItem) -> Unit = {
        executeCommand(ContentCommand.Play(it))
    }
    val getChildrenOfItem: (AltListItem) -> Unit = {
        executeCommand(ContentCommand.GetChildrenOfItem(it))
    }
    val getThumbnail: (String) -> Unit = {
        executeCommand(ImagesCommand.GetThumbnail(it))
    }

    LaunchedEffect(key1 = listItems) {
        if (listItems.isNotEmpty()) {
            executeCommand(ImagesCommand.ClearThumbnails)
            listItems.forEach { item ->
                if (!item.imageUri.isNullOrBlank()) getThumbnail(item.imageUri)
            }
        }
    }

    BrowserSolidBackground(backgroundColor = backgroundColor) {
        Column(
            modifier = Modifier
                .height(LocalConfiguration.current.screenHeightDp.dp)
        ) {
            GetRecommendedButton(getRecommended)
            when {
                listItems.isEmpty() ->
                    EmptyListItems(preferences, palette, darkTheme)

                else ->
                    ItemsList(
                        listItems = listItems,
                        track = track,
                        palette = palette,
                        playItem = playItem,
                        getChildrenOfItem = getChildrenOfItem,
                        thumbnailMap = thumbnailMap
                    )
            }
        }
    }
}

@Composable
fun GetRecommendedButton(getRecommended: () -> Unit) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(16.dp)),
        onClick = { getRecommended() },
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Gray)
    ) {
        AltText(text = "Get Recommended Items")
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
            .padding(vertical = 100.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier
                .size(150.dp),
            painter = painterResource(id = R.drawable.error),
            contentDescription = "",
            tint = bodyColor
        )
        Spacer(modifier = Modifier.height(24.dp))
        AltText(
            text = "Nothing to browse...",
            fontSize = 25.sp
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
        darkTheme = false,
        thumbnailMap = emptyMap(),
        executeCommand = {}
    )
}

@Preview
@Composable
fun BrowserPreview() {
    val items = mutableListOf<AltListItem>()
    repeat(5) {
        val ali = AltListItem(
            id = it.toString(),
            uri = "",
            imageUri = "",
            title = "Title",
            subtitle = "Subtitle",
            playable = it % 2 == 0,
            hasChildren = true
        )
        items.add(ali)
    }
    Browser(
        preferences = AltPreferencesState(),
        palette = null,
        track = null,
        listItems = items,
        darkTheme = false,
        thumbnailMap = emptyMap(),
        executeCommand = {}
    )
}