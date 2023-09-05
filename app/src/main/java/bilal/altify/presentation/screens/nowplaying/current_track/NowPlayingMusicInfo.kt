package bilal.altify.presentation.screens.nowplaying.current_track

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bilal.altify.domain.model.AltLibraryState
import bilal.altify.domain.model.AltTrack
import bilal.altify.presentation.Command
import bilal.altify.presentation.UserCommand
import bilal.altify.presentation.prefrences.MusicInfoAlignmentConfig
import bilal.altify.presentation.screens.nowplaying.bodyColor
import bilal.altify.presentation.screens.nowplaying.nowPlayingItemsPadding
import bilal.altify.presentation.screens.nowplaying.titleColor
import bilal.altify.presentation.util.AltText

@Composable
fun NowPlayingMusicInfo(
    track: AltTrack?,
    config: MusicInfoAlignmentConfig,
    libraryState: AltLibraryState?,
    executeCommand: (Command) -> Unit
) {

    val getLibraryState: (String) -> Unit = {
        executeCommand(UserCommand.UpdateCurrentTrackState(it))
    }
    val addToLibrary: (String) -> Unit = {
        executeCommand(UserCommand.AddToLibrary(it))
    }
    val removeFromLibrary: (String) -> Unit = {
        executeCommand(UserCommand.RemoveFromLibrary(it))
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
                uri = it.uri,
                config = config,
                libraryState = libraryState,
                addToLibrary = addToLibrary,
                removeFromLibrary = removeFromLibrary
            )
        }
    }
}

@Composable
private fun NowPlayingMusicInfo(
    name: String,
    artist: String,
    album: String,
    uri: String,
    config: MusicInfoAlignmentConfig,
    libraryState: AltLibraryState?,
    addToLibrary: (String) -> Unit,
    removeFromLibrary: (String) -> Unit
) {
    Log.d("libstate", libraryState.toString())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(nowPlayingItemsPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (config == MusicInfoAlignmentConfig.CENTER) Spacer(modifier = Modifier.weight(1f))
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = when (config) {
                MusicInfoAlignmentConfig.CENTER -> Alignment.CenterHorizontally
                MusicInfoAlignmentConfig.LEFT -> Alignment.Start
            }
        ) {
            AltText(
                text = name,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
            Text(
                text = "by ${artist}\n$album",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = bodyColor,
                textAlign = when (config) {
                    MusicInfoAlignmentConfig.CENTER -> TextAlign.Center
                    MusicInfoAlignmentConfig.LEFT -> TextAlign.Left
                }
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .height(IntrinsicSize.Max),
            contentAlignment = Alignment.CenterEnd,
        ) {
            AddOrRemoveFromLibraryButton(
                uri = uri,
                libraryState = libraryState,
                addToLibrary = addToLibrary,
                removeFromLibrary = removeFromLibrary,
            )
        }
    }
}

// TODO: add a nice animation 😊
@Composable
fun AddOrRemoveFromLibraryButton(
    uri: String,
    libraryState: AltLibraryState?,
    addToLibrary: (String) -> Unit,
    removeFromLibrary: (String) -> Unit,
) {
    if (libraryState != null) {
        IconButton(
            onClick = {
                when (libraryState.isAdded) {
                    true -> removeFromLibrary(uri)
                    false -> if (libraryState.canAdd) addToLibrary(uri)
                }
            }
        ) {
            Icon(
                imageVector = when (libraryState.isAdded) {
                    true -> Icons.Filled.Favorite
                    false -> Icons.Outlined.FavoriteBorder
                },
                contentDescription = "",
                modifier = Modifier.size(30.dp),
                tint = titleColor
            )
        }
    }
}

@Preview
@Composable
private fun NowPlayingMusicInfoPreview() {
    NowPlayingMusicInfo(
        name = "name",
        artist = "artist",
        album = "album",
        uri = "",
        config = MusicInfoAlignmentConfig.CENTER,
        libraryState = AltLibraryState(""),
        addToLibrary = {},
        removeFromLibrary = {}
    )
}

@Preview
@Composable
private fun NowPlayingMusicInfoLeftPreview() {
    NowPlayingMusicInfo(
        name = "name",
        artist = "artist",
        album = "album",
        uri = "",
        config = MusicInfoAlignmentConfig.LEFT,
        libraryState = AltLibraryState(""),
        addToLibrary = {},
        removeFromLibrary = {}
    )
}