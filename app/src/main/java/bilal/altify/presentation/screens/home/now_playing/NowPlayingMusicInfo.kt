package bilal.altify.presentation.screens.home.now_playing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bilal.altify.domain.spotify.model.AltLibraryState
import bilal.altify.domain.spotify.model.AltTrack
import bilal.altify.domain.spotify.use_case.Command
import bilal.altify.domain.spotify.use_case.UserCommand
import bilal.altify.presentation.prefrences.MusicInfoAlignmentConfig
import bilal.altify.presentation.screens.home.nowPlayingItemsPadding
import bilal.altify.presentation.util.UpdateEffect
import bilal.altify.presentation.util.shakeShrinkAnimation

@Composable
fun NowPlayingMusicInfo(
    track: AltTrack?,
    config: MusicInfoAlignmentConfig,
    libraryState: AltLibraryState?,
    executeCommand: (Command) -> Unit,
    showControls: Boolean
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
                removeFromLibrary = removeFromLibrary,
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
    uri: String,
    config: MusicInfoAlignmentConfig,
    libraryState: AltLibraryState?,
    addToLibrary: (String) -> Unit,
    removeFromLibrary: (String) -> Unit,
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
                    uri = uri,
                    libraryState = libraryState,
                    addToLibrary = addToLibrary,
                    removeFromLibrary = removeFromLibrary,
                )
            }
        }
    }
}

@Composable
fun AddOrRemoveFromLibraryButton(
    uri: String,
    libraryState: AltLibraryState?,
    addToLibrary: (String) -> Unit,
    removeFromLibrary: (String) -> Unit,
) {
    if (libraryState != null) {
        val scale = remember { Animatable(1f) }
        val rotation = remember { Animatable(1f) }
        val coroutineScope = rememberCoroutineScope()

        UpdateEffect(libraryState.isAdded) {
            shakeShrinkAnimation(scale =  scale, rotation = rotation, scope = coroutineScope)
        }

        IconButton(
            onClick =  {
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
                modifier = Modifier
                    .size(30.dp)
                    .scale(scale = scale.value)
                    .rotate(rotation.value),
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
        libraryState = AltLibraryState("", false, canAdd = false),
        addToLibrary = {},
        removeFromLibrary = {},
        showControls = true
    )
}

@Preview
@Composable
private fun NowPlayingLongMusicInfoPreview() {
    NowPlayingMusicInfo(
        name = "namenamenamenamenamenamenamename",
        artist = "artist",
        album = "album",
        uri = "",
        config = MusicInfoAlignmentConfig.CENTER,
        libraryState = AltLibraryState("", false, canAdd = false),
        addToLibrary = {},
        removeFromLibrary = {},
        showControls = true
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
        libraryState = AltLibraryState("", false, false),
        addToLibrary = {},
        removeFromLibrary = {},
        showControls = true
    )
}