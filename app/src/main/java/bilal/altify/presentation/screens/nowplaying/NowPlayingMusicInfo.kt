package bilal.altify.presentation.screens.nowplaying

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import bilal.altify.data.dataclasses.AltTrack

@Composable
fun NowPlayingMusicInfo(track: AltTrack?) {
    if (track != null) {
        NowPlayingMusicInfo(
            name = track.name,
            artist = track.artist,
            album = track.name
        )
    }
}

@Composable
fun NowPlayingMusicInfo(name: String, artist: String, album: String) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AltText(
            text = name,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1
        )
        AltText(
            text = "by $artist",
            fontSize = 20.sp,
            maxLines = 1,
            fontWeight = FontWeight.SemiBold
        )
        AltText(
            text = album,
            fontSize = 20.sp,
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
    )
}