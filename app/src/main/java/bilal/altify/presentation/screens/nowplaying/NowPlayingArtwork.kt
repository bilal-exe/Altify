package bilal.altify.presentation.screens.nowplaying

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import bilal.altify.R
import bilal.altify.presentation.prefrences.AltPreference
import kotlin.math.roundToInt

enum class ArtworkDisplayConfig(override val code: Int, override val title: String) :
    AltPreference {
    NORMAL(0, "Normal"), SPINNING_DISC(1, "Spinning Record")
}

@Composable
fun NowPlayingArtwork(
    bitmap: Bitmap?,
    toggleControls: () -> Unit,
    config: ArtworkDisplayConfig,
    isPaused: Boolean,
    playbackPosition: Long,
    skipPrevious: () -> Unit,
    skipNext: () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    val screenWidthPx =
        with(LocalDensity.current) { screenWidthDp.roundToPx() }
    Box(
        modifier = Modifier
            .fillMaxWidth(if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) 1f else 0.45f)
            .height(if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) screenWidthDp else screenHeightDp)
            .padding(24.dp)
            .clickable { toggleControls() }
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .draggable(
                state = rememberDraggableState { offsetX += it },
                orientation = Orientation.Horizontal,
                onDragStopped = {
                    if (offsetX > screenWidthPx / 2) skipPrevious()
                    if (offsetX < -(screenWidthPx / 2)) skipNext()
                    offsetX = 0f
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        when (config) {
            ArtworkDisplayConfig.NORMAL -> NowPlayingStaticArtwork(
                bitmap = bitmap,
                isPaused = isPaused
            )

            ArtworkDisplayConfig.SPINNING_DISC -> NowPlayingRotatingArtwork(
                bitmap = bitmap,
                isPaused = isPaused,
                playbackPosition = playbackPosition
            )
        }
    }
}

@Composable
private fun NowPlayingArtwork(
    bitmap: Bitmap?,
    size: Float = 1f
) {
    if (bitmap == null) PlaceholderArtwork()
    else Image(
        modifier = Modifier
            .fillMaxWidth(size)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Gray)
            .aspectRatio(1f),
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
    )
}

@Composable
private fun NowPlayingStaticArtwork(
    bitmap: Bitmap?,
    isPaused: Boolean
) {
    val size by animateFloatAsState(
        targetValue = if (isPaused) 0.75f else 1f,
        tween(durationMillis = 1000, easing = if (isPaused) EaseOutBounce else FastOutSlowInEasing),
    )
    NowPlayingArtwork(bitmap = bitmap, size = size)
}

@Composable
private fun PlaceholderArtwork() {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Gray)
            .aspectRatio(1f),
        painter = painterResource(id = R.drawable.music),
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
    )
}

@Composable
fun NowPlayingRotatingArtwork(bitmap: Bitmap?, isPaused: Boolean, playbackPosition: Long) {
    var currentRotation by remember { mutableFloatStateOf(0f) }
    val rotation = remember { Animatable(currentRotation) }

    LaunchedEffect(isPaused) {
        if (!isPaused) {
            rotation.animateTo(
                targetValue = currentRotation + 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(10000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            ) { currentRotation = value }
        } else {
            if (currentRotation > 0f) {
                rotation.animateTo(
                    targetValue = currentRotation + 36,
                    animationSpec = tween(
                        2500,
                        easing = LinearOutSlowInEasing
                    )
                ) { currentRotation = value }
            }
        }
    }
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .graphicsLayer { rotationZ = rotation.value }
    ) {
        NowPlayingArtwork(bitmap = bitmap)
    }
}

@Preview(showBackground = true)
@Composable
private fun NowPlayingStaticArtworkPreview() {
    NowPlayingBackground {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            NowPlayingStaticArtwork(null, isPaused = false)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NowPlayingStaticPausedArtworkPreview() {
    NowPlayingBackground {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            NowPlayingStaticArtwork(null, isPaused = true)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NowPlayingSpinningArtworkPreview() {
    NowPlayingBackground {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            NowPlayingRotatingArtwork(null, isPaused = false, playbackPosition = 0)
        }
    }
}