package bilal.altify.presentation.screens.nowplaying.browse

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bilal.altify.R
import bilal.altify.domain.model.AltLibraryState
import bilal.altify.domain.model.AltListItem
import bilal.altify.domain.model.AltTrack
import bilal.altify.domain.use_case.Command
import bilal.altify.domain.use_case.ContentCommand
import bilal.altify.domain.use_case.ImagesCommand
import bilal.altify.domain.use_case.UserCommand
import bilal.altify.presentation.prefrences.AltPreferencesState
import bilal.altify.presentation.screens.nowplaying.current_track.bottomColor
import bilal.altify.presentation.screens.nowplaying.titleColor
import bilal.altify.presentation.util.AltText

// TODO: Add swipe to add to queue
@Composable
fun Browser(
    preferences: AltPreferencesState,
    track: AltTrack?,
    listItems: List<AltListItem>,
    thumbnailMap: Map<String, Bitmap>,
    libraryState: Map<String, AltLibraryState>,
    executeCommand: (Command) -> Unit
) {

    BackHandler {
        executeCommand(ContentCommand.GetPrevious)
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
    val addToLibrary: (String) -> Unit = {
        executeCommand(UserCommand.AddToLibrary(it))
    }
    val removeFromLibrary: (String) -> Unit = {
        executeCommand(UserCommand.RemoveFromLibrary(it))
    }

    LaunchedEffect(key1 = listItems) {
        if (listItems.isNotEmpty()) {
            executeCommand(ImagesCommand.ClearThumbnails)
            listItems.forEach { item ->
                if (!item.imageUri.isNullOrBlank()) getThumbnail(item.imageUri)
            }
            executeCommand(UserCommand.UpdateBrowserLibraryState(listItems.map { it.uri }))
        }
    }

    BrowserSolidBackground(backgroundColor = bottomColor) {
        Column {
            GetRecommendedButton(getRecommended)
            when {
                listItems.isEmpty() ->
                    EmptyListItems()

                else ->
                    ItemsList(
                        listItems = listItems,
                        track = track,
                        playItem = playItem,
                        getChildrenOfItem = getChildrenOfItem,
                        thumbnailMap = thumbnailMap,
                        libraryState = libraryState,
                        addToLibrary = addToLibrary,
                        removeFromLibrary = removeFromLibrary
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
        colors = ButtonDefaults.outlinedButtonColors(containerColor = titleColor)
    ) {
        Text(
            text = "Get Recommended Items",
            color = bottomColor
        )
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
private fun EmptyListItems() {
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
            tint = titleColor
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
        track = null,
        listItems = emptyList(),
        thumbnailMap = emptyMap(),
        libraryState = emptyMap(),
        executeCommand = {}
    )
}

@Preview
@Composable
fun BrowserPreview() {
    val items = mutableListOf<AltListItem>()
    repeat(5) {
        val ali = AltListItem(
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
        track = null,
        listItems = items,
        thumbnailMap = emptyMap(),
        libraryState = emptyMap(),
        executeCommand = {}
    )
}