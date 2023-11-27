package bilal.altify.presentation.util

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Composable
private fun sliderColors() =
    SliderDefaults.colors(
        thumbColor = MaterialTheme.colorScheme.onBackground,
        activeTrackColor = MaterialTheme.colorScheme.onBackground,
        inactiveTrackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.25f),
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AltSlider(
    progress: Long,
    modifier: Modifier = Modifier,
    duration: Duration = 0.minutes,
    onSliderMoved: (Long) -> Unit,
) {
    val color = MaterialTheme.colorScheme.onBackground
    Slider(
        modifier = modifier.height(20.dp),
        value = progress.toFloat(),
        onValueChange = { onSliderMoved(it.toLong()) },
        valueRange = 0f..duration.inWholeMilliseconds.toFloat(),
        colors = sliderColors(),
        thumb = {
            Canvas(
                modifier = Modifier.size(DpSize(20.dp, 20.dp)),
            ) {
                drawCircle(
                    color = color,
                    center = Offset(x = size.width / 2, y = size.height / 2),
                    radius = 10f
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AltSlider(
    progress: Float,
    modifier: Modifier = Modifier,
    duration: Float,
    onSliderMoved: (Float) -> Unit
) {
    val color = MaterialTheme.colorScheme.onBackground
    Slider(
        modifier = modifier.height(20.dp),
        value = progress,
        onValueChange = { onSliderMoved(it) },
        valueRange = 0f..duration,
        colors = sliderColors(),
        thumb = {
            Canvas(
                modifier = Modifier.size(DpSize(20.dp, 20.dp)),
            ) {
                drawCircle(
                    color = color,
                    center = Offset(x = size.width / 2, y = size.height / 2),
                    radius = 10f
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SliderPreview() {
    AltSlider(
        progress = 1000f,
        onSliderMoved = {},
        duration = 2000f
    )
}