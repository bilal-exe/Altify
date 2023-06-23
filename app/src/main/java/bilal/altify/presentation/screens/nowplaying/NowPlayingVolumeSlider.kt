package bilal.altify.presentation.screens.nowplaying

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bilal.altify.R

@Composable
fun NowPlayingVolumeSlider(
    volume: Float,
    increaseVolume: () -> Unit,
    decreaseVolume: () -> Unit,
    setVolume: (Float) -> Unit
) {

    val sliderColors = SliderDefaults.colors(
        thumbColor = Color.Red,
        activeTrackColor = Color.White,
        inactiveTrackColor = Color.Gray
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top)
    ) {
        Slider(
            modifier = Modifier.height(20.dp),
            value = volume,
            onValueChange = { setVolume(it) },
            valueRange = 0f..1f,
            colors = sliderColors
        )
        Row {
            Icon(painterResource(id = R.drawable.volume_mute), "", tint = Color.White)
            Spacer(modifier = Modifier.weight(1f))
            Icon(painterResource(id = R.drawable.volume_up), "", tint = Color.White)
        }
    }
}

@Preview
@Composable
private fun NowPlayingVolumeSliderPreview() {
    NowPlayingVolumeSlider(0.5f, {}, {}, {})
}