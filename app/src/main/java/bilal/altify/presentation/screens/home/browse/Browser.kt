package bilal.altify.presentation.screens.home.browse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import bilal.altify.R
import bilal.altify.domain.spotify.model.AltListItem
import bilal.altify.domain.spotify.model.AltListItems
import bilal.altify.domain.spotify.model.BrowserState
import bilal.altify.domain.spotify.use_case.Command
import bilal.altify.presentation.prefrences.AltPreferencesState

// TODO: Add swipe to add to queue
@Composable
fun Browser(
    preferences: AltPreferencesState,
    playingTrackUri: String?,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    executeCommand: (Command) -> Unit,
    viewModel: BrowserViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val uiState = state
    BrowserSolidBackground(backgroundColor = backgroundColor) {
        Column {
            when (uiState) {
                BrowserUIState.Loading ->
                    BrowserLoading()
                is BrowserUIState.Success ->
                    Browser(
                        preferences = preferences,
                        playingTrackUri = playingTrackUri,
                        browserState = uiState.browserState,
                        executeCommand = executeCommand
                    )
            }
        }
    }
}

@Composable
fun BrowserLoading() {
    val height = LocalConfiguration.current.screenHeightDp.dp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Browser(
    preferences: AltPreferencesState,
    playingTrackUri: String?,
    browserState: BrowserState,
    executeCommand: (Command) -> Unit
) {
    when {
        browserState.listItems().isEmpty() ->
            EmptyListItems()
        else ->
            ItemsList(
                listItems = browserState.listItems,
                track = playingTrackUri,
                thumbnailMap = browserState.thumbnailMap,
                libraryState = browserState.libraryState,
                executeCommand = executeCommand
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
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
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
        playingTrackUri = null,
        executeCommand = { },
        browserState = BrowserState()
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
        playingTrackUri = null,
        executeCommand = { },
        browserState = BrowserState(
            listItems = AltListItems(items)
        )
    )
}