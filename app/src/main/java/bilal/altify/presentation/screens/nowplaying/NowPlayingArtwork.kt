package bilal.altify.presentation.screens.nowplaying

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bilal.altify.R
import bilal.altify.data.spotify.Player
import bilal.altify.presentation.prefrences.AltPreference
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    playbackPosition: Long
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenWidthDp.dp)
            .padding(horizontal = 16.dp)
            .clickable { toggleControls() },
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
            .background(Color.Gray),
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
        targetValue = if (isPaused) 0.85f else 1f,
        tween(durationMillis = 1000, easing = EaseOutBounce),
    )
    NowPlayingArtwork(bitmap = bitmap, size = size)
}

@Composable
private fun PlaceholderArtwork() {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Gray),
        painter = painterResource(id = R.drawable.music),
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
    )
}

@Composable
fun NowPlayingRotatingArtwork(bitmap: Bitmap?, isPaused: Boolean, playbackPosition: Long) {
    var rotation by remember { mutableFloatStateOf(0f) }
    val scope = rememberCoroutineScope()
    DisposableEffect(key1 = isPaused) {
        val job = if (isPaused) null else scope.launch {
            while (true) {
                rotation += 0.36f
                delay(10)
            }
        }
        onDispose { job?.cancel() }
    }
    Log.d("A", rotation.toString())
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .graphicsLayer { rotationZ = rotation }
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