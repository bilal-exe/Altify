package bilal.altify.presentation.screens.home.now_playing

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bilal.altify.presentation.screens.home.nowPlayingItemsPadding
import bilal.altify.presentation.util.AltSlider
import bilal.altify.presentation.util.toMinsSecs
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

@Composable
fun NowPlayingProgressBar(
    progress: Duration,
    duration: Duration = 0.minutes,
    onSliderMoved: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(nowPlayingItemsPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top)
    ) {
        AltSlider(
            progress = progress.inWholeMilliseconds,
            duration = duration,
            onSliderMoved = onSliderMoved,
        )
        Row (
            Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = progress.toMinsSecs(),
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = duration.toMinsSecs(),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NowPlayingProgressBarPreview() {
    NowPlayingProgressBar(5000L.milliseconds, 10000L.milliseconds) {}
}