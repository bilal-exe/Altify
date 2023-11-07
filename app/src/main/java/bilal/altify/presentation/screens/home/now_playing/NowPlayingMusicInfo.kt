package bilal.altify.presentation.screens.home.now_playing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bilal.altify.domain.spotify.model.LibraryState
import bilal.altify.domain.spotify.model.Track
import bilal.altify.domain.spotify.use_case.model.Command
import bilal.altify.domain.spotify.use_case.model.UserCommand
import bilal.altify.presentation.prefrences.MusicInfoAlignmentConfig
import bilal.altify.presentation.screens.home.nowPlayingItemsPadding
import bilal.altify.presentation.util.ShakeBounceAnimation

@Composable
fun NowPlayingMusicInfo(
    track: Track?,
    config: MusicInfoAlignmentConfig,
    libraryState: LibraryState?,
    executeCommand: (Command) -> Unit,
    showControls: Boolean
) {

    val getLibraryState: (String) -> Unit = {
        executeCommand(UserCommand.UpdateCurrentTrackState(it))
    }
    val toggleLibraryStatus: (String, Boolean) -> Unit = { uri, bool ->
        executeCommand(UserCommand.ToggleLibraryStatus(uri, bool))
    }

    LaunchedEffect(key1 = track) {
        if (track != null) getLibraryState(track.uri)
    }

    Crossfade(
        targetState = track,
        animationSpec = tween(durationMillis = 1000), label = ""
    ) {
        if (it != null) {
            NowPlayingMusicInfo(
                name = it.name,
                artist = it.artist,
                album = it.name,
                config = config,
                libraryState = libraryState,
                toggleLibraryStatus = toggleLibraryStatus,
                showControls = showControls
            )
        }
    }
}

@Composable
private fun NowPlayingMusicInfo(
    name: String,
    artist: String,
    album: String,
    config: MusicInfoAlignmentConfig,
    libraryState: LibraryState?,
    toggleLibraryStatus: (String, Boolean) -> Unit,
    showControls: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(nowPlayingItemsPadding),
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = when (config) {
                MusicInfoAlignmentConfig.CENTER -> Alignment.CenterHorizontally
                MusicInfoAlignmentConfig.LEFT -> Alignment.Start
            },
            modifier = Modifier
                .align(
                    when (config) {
                        MusicInfoAlignmentConfig.CENTER -> Alignment.Center
                        MusicInfoAlignmentConfig.LEFT -> Alignment.CenterStart
                    }
                )
        ) {
            Text(
                text = name,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "by ${artist}\n$album",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = when (config) {
                    MusicInfoAlignmentConfig.CENTER -> TextAlign.Center
                    MusicInfoAlignmentConfig.LEFT -> TextAlign.Left
                }
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .height(IntrinsicSize.Max),
            contentAlignment = Alignment.CenterEnd,
        ) {
            AnimatedVisibility(showControls) {
                AddOrRemoveFromLibraryButton(
                    libraryState = libraryState,
                    toggleLibraryStatus = toggleLibraryStatus,
                )
            }
        }
    }
}

@Composable
fun AddOrRemoveFromLibraryButton(
    libraryState: LibraryState?,
    toggleLibraryStatus: (String, Boolean) -> Unit,
) {
    if (libraryState != null) {
        val icon = when (libraryState.isAdded) {
            true -> Icons.Filled.Favorite
            false -> Icons.Outlined.FavoriteBorder
        }
        val interactionSource = remember { MutableInteractionSource() }
        ShakeBounceAnimation(
            icon = icon,
            modifier = Modifier
                .clickable(interactionSource = interactionSource, indication = null ) {
                    toggleLibraryStatus(libraryState.uri, !libraryState.isAdded)
                },
        ) {
            Icon(
                imageVector = when (libraryState.isAdded) {
                    true -> Icons.Filled.Favorite
                    false -> Icons.Outlined.FavoriteBorder
                },
                contentDescription = "",
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NowPlayingMusicInfoPreview() {
    NowPlayingMusicInfo(
        name = "name",
        artist = "artist",
        album = "album",
        config = MusicInfoAlignmentConfig.CENTER,
        libraryState = LibraryState("", false, canAdd = false),
        toggleLibraryStatus = { _, _ -> },
        showControls = true
    )
}

@Preview(showBackground = true)
@Composable
private fun NowPlayingLongMusicInfoPreview() {
    NowPlayingMusicInfo(
        name = "namenamenamenamenamenamenamename",
        artist = "artist",
        album = "album",
        config = MusicInfoAlignmentConfig.CENTER,
        libraryState = LibraryState("", false, canAdd = false),
        toggleLibraryStatus = { _, _ -> },
        showControls = true
    )
}

@Preview(showBackground = true)
@Composable
private fun NowPlayingMusicInfoLeftPreview() {
    NowPlayingMusicInfo(
        name = "name",
        artist = "artist",
        album = "album",
        config = MusicInfoAlignmentConfig.LEFT,
        libraryState = LibraryState("", isAdded = false, canAdd = false),
        toggleLibraryStatus = { _, _ -> },
        showControls = true
    )
}