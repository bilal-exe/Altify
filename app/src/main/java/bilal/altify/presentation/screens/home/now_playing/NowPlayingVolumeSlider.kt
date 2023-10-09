package bilal.altify.presentation.screens.home.now_playing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bilal.altify.R
import bilal.altify.presentation.screens.home.nowPlayingItemsPadding
import bilal.altify.presentation.util.AltSlider

@Composable
fun NowPlayingVolumeSlider(
    volume: Float,
    setVolume: (Float) -> Unit
) {

    SliderDefaults.colors(
        thumbColor = Color.Red,
        inactiveTrackColor = Color.Gray
    )

    Row(
        modifier = Modifier
            .width(LocalConfiguration.current.screenWidthDp.dp)
            .padding(nowPlayingItemsPadding),
        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painterResource(id = R.drawable.volume_mute), "")
        AltSlider(
            progress = volume,
            duration = 1f
        ) { setVolume(it) }
    }

}

@Preview
@Composable
private fun NowPlayingVolumeSliderPreview() {
    NowPlayingVolumeSlider(0.5f) {}
}