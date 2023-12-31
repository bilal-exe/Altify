package bilal.altify.presentation.screens.home.browse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bilal.altify.R
import bilal.altify.domain.model.ContentType
import bilal.altify.domain.model.ImageRemoteId
import bilal.altify.domain.model.ListItem
import bilal.altify.domain.model.ListItems
import bilal.altify.domain.model.RemoteId
import bilal.altify.domain.spotify.use_case.model.BrowserState
import bilal.altify.domain.spotify.use_case.model.Command
import bilal.altify.presentation.prefrences.AltPreferencesState

fun LazyListScope.browser(
    preferences: AltPreferencesState,
    playingTrackId: RemoteId?,
    backgroundColor: Color,
    executeCommand: (Command) -> Unit,
    uiState: BrowserUIState,
) {

    when (uiState) {
        BrowserUIState.Loading -> {
            item { BrowserLoading(backgroundColor) }
        }
        is BrowserUIState.Success ->
            browser(
                preferences = preferences,
                playingTrackId = playingTrackId,
                browserState = uiState.browserState,
                executeCommand = executeCommand,
                backgroundColor = backgroundColor,
            )
    }
}

@Composable
fun BrowserLoading(backgroundColor: Color) {
    val height = LocalConfiguration.current.screenHeightDp.dp
    BrowserSolidBackground(
        backgroundColor = backgroundColor,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

private fun LazyListScope.browser(
    preferences: AltPreferencesState,
    playingTrackId: RemoteId?,
    browserState: BrowserState,
    executeCommand: (Command) -> Unit,
    backgroundColor: Color,
) {
    when (browserState.listItems) {
        null ->
            emptyListItems()
        else ->
            browserItemsList(
                listItems = browserState.listItems,
                playingTrackId = playingTrackId,
                thumbnailMap = browserState.thumbnailMap,
                libraryState = browserState.libraryState,
                executeCommand = executeCommand,
                backgroundColor = backgroundColor,
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
            .drawWithCache {
                onDrawBehind {
                    drawRect(backgroundColor)
                }
            }
    ) { content() }
}

private fun LazyListScope.emptyListItems() {
    item {
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
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Nothing to browse...",
                fontSize = 25.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyPreview() {
    val backgroundColor = MaterialTheme.colorScheme.background
    LazyColumn {
        browser(
            preferences = AltPreferencesState(),
            playingTrackId = null,
            browserState = BrowserState(),
            executeCommand = { },
            backgroundColor = backgroundColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BrowserPreview() {
    val items = mutableListOf<ListItem>()
    repeat(5) {
        val ali = ListItem(
            remoteId = RemoteId.fake,
            imageRemoteId = ImageRemoteId(""),
            title = "Title",
            subtitle = "Subtitle",
            playable = it % 2 == 0,
        )
        items.add(ali)
    }
    val backgroundColor = MaterialTheme.colorScheme.background
    LazyColumn {
        browser(
            preferences = AltPreferencesState(),
            playingTrackId = null,
            browserState = BrowserState(
                listItems = ListItems(5, items)
            ),
            executeCommand = { },
            backgroundColor = backgroundColor,
        )
    }
}