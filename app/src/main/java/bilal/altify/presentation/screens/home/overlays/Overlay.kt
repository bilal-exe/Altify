package bilal.altify.presentation.screens.home.overlays

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bilal.altify.R
import bilal.altify.presentation.screens.home.now_playing.bottomColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun Overlay(
    overlayType: OverlayType
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = overlayType) {
        if (overlayType == null) return@LaunchedEffect
        visible = true
        CoroutineScope(IO).launch {
            delay(5000)
            visible = false
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        OverlayTemplate(visible) {
            when (overlayType) {
                is OverlayType.Time -> TimeOverlay(overlayType.value)
                is OverlayType.Volume -> VolumeOverlay(overlayType.volume)
                is OverlayType.AddToPlaylist -> TODO()
            }
        }
    }
}

@Composable
private fun VolumeOverlay(volume: Float) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.volume_up),
            contentDescription = "",
            modifier = Modifier
                .size(100.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(
            progress = volume,
            trackColor = bottomColor,
        )
    }
}

@Composable
private fun TimeOverlay(value: Float) {
    Text(
        text = value.roundToInt().toString(),
        fontSize = 100.sp,
        color = bottomColor,
    )
}

@Composable
private fun OverlayTemplate(
    visible: Boolean,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 20.dp,
        shadowElevation = 20.dp,
        modifier = Modifier
            .height(LocalConfiguration.current.screenHeightDp.dp / 4)
            .width(LocalConfiguration.current.screenWidthDp.dp / 2)
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut(
                animationSpec = tween(delayMillis = 2500)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                content = content,
                contentAlignment = Alignment.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimeOverlayPreview() {
    OverlayTemplate(true) {
        TimeOverlay(20f)
    }
}

@Preview(showBackground = true)
@Composable
fun VolumeOverlayPreview() {
    OverlayTemplate(true) {
        VolumeOverlay(0.5f)
    }
}