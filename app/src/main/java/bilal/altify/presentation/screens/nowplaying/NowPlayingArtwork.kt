package bilal.altify.presentation.screens.nowplaying

import android.graphics.Bitmap
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bilal.altify.R
import bilal.altify.presentation.prefrences.AltPreference

enum class ArtworkDisplayConfig(override val code: Int): AltPreference {
    NORMAL(0), SPINNING_DISC(1)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NowPlayingArtwork(bitmap: Bitmap?, toggleControls: () -> Unit) {
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState(pageCount = { 2 })
    HorizontalPager(
        state = pageState,
        modifier = Modifier
            .padding(16.dp)
            .clickable { toggleControls() },
        pageSpacing = 50.dp
    ) {
        when (it) {
            0 -> NowPlayingStaticArtwork(bitmap = bitmap)
            1 -> NowPlayingRotatingArtwork(bitmap = bitmap)
        }
    }
}

@Composable
private fun NowPlayingStaticArtwork(
    bitmap: Bitmap?,
) {
    if (bitmap == null) PlaceholderArtwork()
    else Image(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Gray),
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
    )
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
fun NowPlayingRotatingArtwork(bitmap: Bitmap?) {
    val infiniteTransition = rememberInfiniteTransition()
    val spin by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing)
        )
    )
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .graphicsLayer { rotationZ = spin }
    ) {
        NowPlayingStaticArtwork(bitmap = bitmap)
    }
}

@Preview(showBackground = true)
@Composable
private fun NowPlayingArtworkPreview() {
    NowPlayingBackground {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            NowPlayingArtwork(null) { }
        }
    }
}