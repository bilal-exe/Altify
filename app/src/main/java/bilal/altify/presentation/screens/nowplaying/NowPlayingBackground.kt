package bilal.altify.presentation.screens.nowplaying

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import bilal.altify.presentation.util.WhiteText
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun NowPlayingBackground(
    bitmap: Bitmap? = null,
    content: @Composable () -> Unit,
) {
    if (bitmap == null) NowPlayingGradientBackground(content)
    else NowPlayingGradientBackground(bitmap, content)
}

@Composable
private fun NowPlayingGradientBackground(
    bitmap: Bitmap,
    content: @Composable () -> Unit
) {
    val palette = Palette.from(bitmap).generate()
    val systemUiController = rememberSystemUiController()
    val surfaceColor = MaterialTheme.colorScheme.surface
    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant

    val startColor = palette.dominantSwatch?.getColor() ?: surfaceColor
    val endColor = palette.darkMutedSwatch?.getColor() ?: surfaceVariantColor

    LaunchedEffect(key1 = palette) {
        val statusBarColor = palette.darkVibrantSwatch?.getColor() ?: surfaceColor
        systemUiController.setStatusBarColor(statusBarColor)
    }

    NowPlayingGradientBackground(startColor = startColor, endColor = endColor, content = content)
}

// all the functions end here
@Composable
private fun NowPlayingGradientBackground(
    startColor: Color,
    endColor: Color,
    content: @Composable () -> Unit
) {
    val brush = Brush.linearGradient(
        colors = listOf(startColor, endColor),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    Column(
        modifier = Modifier
            .background(brush)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) { content() }
}

@Composable
private fun NowPlayingGradientBackground(content: @Composable () -> Unit) =
    NowPlayingGradientBackground(
        startColor = Color.Gray,
        endColor = Color.Black,
        content = content
    )

private fun Palette.Swatch.getColor() = Color(rgb)

@Preview(showBackground = true)
@Composable
private fun NowPlayingBackgroundPreview() {
    NowPlayingGradientBackground(startColor = Color.White, endColor = Color.Black) {
        Text(text = "Hello")
    }
}

@Preview(showBackground = true)
@Composable
private fun NowPlayingDefualtBackgroundPreview() {
    NowPlayingGradientBackground {
        WhiteText(text = "Hello")
    }
}