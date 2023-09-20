package bilal.altify.presentation.screens.nowplaying.current_track

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import bilal.altify.presentation.prefrences.BackgroundColourConfig
import bilal.altify.presentation.prefrences.BackgroundStyleConfig
import bilal.altify.presentation.util.AltText
import bilal.altify.presentation.util.SetStatusBarColor
import bilal.altify.presentation.util.getColor

var bottomColor by mutableStateOf(Color.Black)
    private set

@Composable
fun NowPlayingBackground(
    palette: Palette? = null,
    darkTheme: Boolean = false,
    styleConfig: BackgroundStyleConfig = BackgroundStyleConfig.SOLID,
    colourConfig: BackgroundColourConfig = BackgroundColourConfig.VIBRANT,
    content: @Composable () -> Unit,
) {

    if (palette == null) {
        NowPlayingSolidBackground(
            backgroundColor = if (darkTheme) Color.Black else Color.White,
            content = content
        )
        return
    }

    val surfaceColor = MaterialTheme.colorScheme.surface

    val mainColor = when (colourConfig) {

        BackgroundColourConfig.VIBRANT ->
            if (darkTheme) palette.darkVibrantSwatch else palette.lightVibrantSwatch

        BackgroundColourConfig.MUTED ->
            if (darkTheme) palette.darkMutedSwatch else palette.lightMutedSwatch

    }?.getColor() ?: surfaceColor

    when (styleConfig) {

        BackgroundStyleConfig.SOLID -> {
            NowPlayingSolidBackground(backgroundColor = mainColor, content = content)
        }

        BackgroundStyleConfig.DIAGONAL_GRADIENT -> NowPlayingDiagonalGradientBackground(
            palette = palette,
            colourConfig = colourConfig,
            content = content
        )


        BackgroundStyleConfig.VERTICAL_GRADIENT -> NowPlayingVerticalGradientBackground(
            palette = palette,
            darkTheme = darkTheme,
            color = colourConfig,
            content = content
        )

        else -> throw Exception()
    }
}

@Composable
private fun NowPlayingSolidBackground(
    backgroundColor: Color,
    content: @Composable () -> Unit,
) {
    bottomColor = backgroundColor
    SetStatusBarColor(color = backgroundColor)
    Box(
        modifier = Modifier
            .background(color = backgroundColor)
            .size(
                width = LocalConfiguration.current.screenWidthDp.dp,
                height = LocalConfiguration.current.screenHeightDp.dp
            )
    ) { content() }
}

@Composable
private fun NowPlayingDiagonalGradientBackground(
    palette: Palette,
    colourConfig: BackgroundColourConfig,
    content: @Composable () -> Unit
) {

    val mainColor = palette.dominantSwatch?.getColor() ?: MaterialTheme.colorScheme.surface
    val endColor = when (colourConfig) {
        BackgroundColourConfig.VIBRANT -> palette.vibrantSwatch
        BackgroundColourConfig.MUTED -> palette.mutedSwatch
    }
        ?.getColor() ?: MaterialTheme.colorScheme.surfaceVariant

    NowPlayingDiagonalGradientBackground(
        mainColor = mainColor,
        endColor = endColor,
        content = content
    )
}

@Composable
private fun NowPlayingDiagonalGradientBackground(
    mainColor: Color,
    endColor: Color,
    content: @Composable () -> Unit
) {
    bottomColor = endColor

    val brush = Brush.linearGradient(
        colorStops = arrayOf(
            0.5f to mainColor,
            1f to endColor
        )
    )

    SetStatusBarColor(color = mainColor)
    Box(
        modifier = Modifier
            .background(brush)
            .size(
                width = LocalConfiguration.current.screenWidthDp.dp,
                height = LocalConfiguration.current.screenHeightDp.dp
            )
    ) { content() }
}

@Composable
private fun NowPlayingVerticalGradientBackground(
    palette: Palette,
    darkTheme: Boolean,
    color: BackgroundColourConfig,
    content: @Composable () -> Unit
) {
    val mainColor = when (color) {
        BackgroundColourConfig.VIBRANT -> palette.vibrantSwatch
        BackgroundColourConfig.MUTED -> palette.mutedSwatch
    }
        ?.getColor() ?: MaterialTheme.colorScheme.surface

    NowPlayingVerticalGradientBackground(
        mainColor = mainColor,
        darkTheme = darkTheme,
        content = content
    )
}

@Composable
private fun NowPlayingVerticalGradientBackground(
    mainColor: Color,
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    bottomColor = if (darkTheme) Color.Black else Color.White

    val brush = Brush.verticalGradient(
        colors = listOf(mainColor, bottomColor)
    )

    SetStatusBarColor(color = mainColor)
    Box(
        modifier = Modifier
            .background(brush)
            .size(
                width = LocalConfiguration.current.screenWidthDp.dp,
                height = LocalConfiguration.current.screenHeightDp.dp
            )
    ) { content() }
}

@Preview(showBackground = true)
@Composable
private fun NowPlayingDefaultPlainBackgroundPreview() {
    NowPlayingSolidBackground(Color.White) {
        AltText(text = "Title")
    }
}

@Preview(showBackground = true)
@Composable
private fun NowPlayingDefaultSolidBackgroundPreview() {
    NowPlayingSolidBackground(Color.Red) {
        AltText(text = "Title")
    }
}

@Preview(showBackground = true)
@Composable
private fun NowPlayingDiagonalGradientBackgroundPreview() {
    NowPlayingDiagonalGradientBackground(
        mainColor = Color.Black,
        endColor = Color.White,
        content = { AltText(text = "Hello") },
    )
}

@Preview(showBackground = true)
@Composable
fun NowPlayingVerticalGradientBackgroundPreview() {
    NowPlayingVerticalGradientBackground(
        mainColor = Color.Red,
        darkTheme = false,
        content = { AltText(text = "Hello") }
    )
}