package bilal.altify.presentation.screens.nowplaying.current_track

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bilal.altify.presentation.util.toMinsSecs
import bilal.altify.presentation.util.AltSlider
import bilal.altify.presentation.util.AltText

@Composable
fun NowPlayingProgressBar(
    progress: Long,
    duration: Long = 0,
    onSliderMoved: (Long) -> Unit,
    darkTheme: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top)
    ) {
        AltSlider(
            progress = progress,
            onSliderMoved = onSliderMoved,
            duration = duration,
            darkTheme = darkTheme
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
    NowPlayingProgressBar(5000L, 10000L, {}, false)
}