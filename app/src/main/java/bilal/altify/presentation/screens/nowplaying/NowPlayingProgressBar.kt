package bilal.altify.presentation.screens.nowplaying

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bilal.altify.data.util.toMinsSecs
import bilal.altify.presentation.util.AltText

@Composable
fun NowPlayingProgressBar(
    progress: Long,
    duration: Long = 0,
    onSliderMoved: (Long) -> Unit
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
            value = progress.toFloat(),
            onValueChange = { onSliderMoved(it.toLong()) },
            valueRange = 0f..duration.toFloat(),
            colors = sliderColors
        )
        Row {
            AltText(text = progress.toMinsSecs())
            Spacer(modifier = Modifier.weight(1f))
            AltText(text = duration.toMinsSecs())
        }
    }
}

@Preview
@Composable
private fun NowPlayingProgressBarPreview() {
    NowPlayingProgressBar(5000L, 10000L) {}
}