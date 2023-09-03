package bilal.altify.presentation.screens.nowplaying.current_track

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import bilal.altify.domain.model.AltTrack
import bilal.altify.presentation.prefrences.MusicInfoAlignmentConfig
import bilal.altify.presentation.util.AltText

@Composable
fun NowPlayingMusicInfo(track: AltTrack?, config: MusicInfoAlignmentConfig) {
    Crossfade(
        targetState = track,
        animationSpec = tween(durationMillis = 1000)
    ) {
        if (it != null) {
            NowPlayingMusicInfo(
                name = it.name,
                artist = it.artist,
                album = it.name,
                config = config
            )
        }
    }
}

@Composable
fun NowPlayingMusicInfo(
    name: String,
    artist: String,
    album: String,
    config: MusicInfoAlignmentConfig
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = when (config) {
            MusicInfoAlignmentConfig.CENTER -> Alignment.CenterHorizontally
            MusicInfoAlignmentConfig.LEFT -> Alignment.Start
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        AltText(
            text = name,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1
        )
        AltText(
            text = "by $artist",
            fontSize = 15.sp,
            maxLines = 1,
            fontWeight = FontWeight.SemiBold
        )
        AltText(
            text = album,
            fontSize = 15.sp,
            maxLines = 1,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview
@Composable
private fun NowPlayingMusicInfoPreview() {
    NowPlayingMusicInfo(
        name = "name",
        artist = "artist",
        album = "album",
        config = MusicInfoAlignmentConfig.CENTER
    )
}