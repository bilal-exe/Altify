package bilal.altify.presentation.util

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import bilal.altify.presentation.screens.nowplaying.titleColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AltSlider(
    progress: Long,
    modifier: Modifier = Modifier,
    duration: Long = 0,
    onSliderMoved: (Long) -> Unit,
    darkTheme: Boolean,
) {
    val sliderColors = SliderDefaults.colors(
        activeTrackColor = titleColor,
        inactiveTrackColor = getComplementaryColor(titleColor)
    )
    Slider(
        modifier = modifier.height(20.dp),
        value = progress.toFloat(),
        onValueChange = { onSliderMoved(it.toLong()) },
        valueRange = 0f..duration.toFloat(),
        colors = sliderColors,
        thumb = {
            Canvas(
                modifier = Modifier.size(DpSize(20.dp, 20.dp)),
            ) {
                drawCircle(
                    color = if (darkTheme) Color.White else Color.Black,
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
    onSliderMoved: (Float) -> Unit,
    darkTheme: Boolean
) {
    val sliderColors = SliderDefaults.colors(
        activeTrackColor = titleColor,
        inactiveTrackColor = getComplementaryColor(titleColor)
    )
    Slider(
        modifier = modifier.height(20.dp),
        value = progress,
        onValueChange = { onSliderMoved(it) },
        valueRange = 0f..duration,
        colors = sliderColors,
        thumb = {
            Canvas(
                modifier = Modifier.size(DpSize(20.dp, 20.dp)),
            ) {
                drawCircle(
                    color = if (darkTheme) Color.White else Color.Black,
                    center = Offset(x = size.width / 2, y = size.height / 2),
                    radius = 10f
                )
            }
        }
    )
}