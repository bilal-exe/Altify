package bilal.altify.presentation.screens.nowplaying.current_track

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.palette.graphics.Palette
import bilal.altify.presentation.prefrences.BackgroundColourConfig
import bilal.altify.presentation.prefrences.BackgroundStyleConfig
import bilal.altify.presentation.util.AltText
import bilal.altify.presentation.util.getColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController

var bodyColor by mutableStateOf(Color.Black)
    private set
var titleColor by mutableStateOf(Color.Black)
    private set

@Composable
fun NowPlayingBackground(
    palette: Palette? = null,
    darkTheme: Boolean = false,
    style: BackgroundStyleConfig = BackgroundStyleConfig.SOLID,
    color: BackgroundColourConfig = BackgroundColourConfig.VIBRANT,
    content: @Composable () -> Unit,
) {

    val themeColor = if (darkTheme) Color.Black else Color.White

    if (palette == null) {
        bodyColor = if (darkTheme) Color.White else Color.Black
        titleColor = if (darkTheme) Color.White else Color.Black
        NowPlayingSolidBackground(backgroundColor = themeColor, content = content)
        return
    }

    val systemUiController = rememberSystemUiController()
    val surfaceColor = MaterialTheme.colorScheme.surface

    if (style == BackgroundStyleConfig.VERTICAL_GRADIENT) {
        bodyColor = if (darkTheme) Color.White else Color.Black
        titleColor = if (darkTheme) Color.White else Color.Black
    } else {
        titleColor = Color(palette.dominantSwatch!!.titleTextColor)
        bodyColor = Color(palette.dominantSwatch!!.bodyTextColor)
    }

    LaunchedEffect(key1 = palette) {
        val statusBarColor = when (style) {
            BackgroundStyleConfig.SOLID ->
                palette.dominantSwatch?.getColor() ?: surfaceColor

            BackgroundStyleConfig.DIAGONAL_GRADIENT ->
                palette.dominantSwatch?.getColor() ?: surfaceColor

            BackgroundStyleConfig.VERTICAL_GRADIENT -> when (color) {
                BackgroundColourConfig.VIBRANT -> palette.vibrantSwatch
                BackgroundColourConfig.MUTED -> palette.mutedSwatch
            }?.getColor() ?: surfaceColor

            else -> throw Exception()
        }
        systemUiController.setStatusBarColor(statusBarColor)
    }

    when (style) {

        BackgroundStyleConfig.SOLID -> {
            val backgroundColor = if (palette.dominantSwatch == null) {
                if (darkTheme) Color.Black else Color.White
            } else palette.dominantSwatch!!.getColor()
            NowPlayingSolidBackground(backgroundColor = backgroundColor, content = content)
        }

        BackgroundStyleConfig.DIAGONAL_GRADIENT -> NowPlayingDiagonalGradientBackground(
            palette = palette,
            darkTheme = darkTheme,
            color = color,
            content = content
        )


        BackgroundStyleConfig.VERTICAL_GRADIENT -> NowPlayingVerticalGradientBackground(
            palette = palette,
            darkTheme = darkTheme,
            color = color,
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
    Box(
        modifier = Modifier
            .background(color = backgroundColor)
            .fillMaxSize()
    ) { content() }
}

@Composable
private fun NowPlayingDiagonalGradientBackground(
    palette: Palette?,
    darkTheme: Boolean,
    color: BackgroundColourConfig,
    content: @Composable () -> Unit
) {

    if (palette == null) NowPlayingDiagonalGradientBackground(
        mainColor = if (darkTheme) Color.Black else Color.White,
        endColor = if (darkTheme) Color.LightGray else Color.DarkGray,
        content = content
    ) else {
        val mainColor = palette.dominantSwatch?.getColor() ?: MaterialTheme.colorScheme.surface
        val endColor = when (color) {
            BackgroundColourConfig.VIBRANT ->
                if (darkTheme) palette.darkVibrantSwatch else palette.lightVibrantSwatch

            BackgroundColourConfig.MUTED ->
                if (darkTheme) palette.darkMutedSwatch else palette.lightMutedSwatch
        }

        NowPlayingDiagonalGradientBackground(
            mainColor = mainColor,
            endColor = endColor?.getColor() ?: MaterialTheme.colorScheme.surfaceVariant,
            content = content
        )
    }
}

@Composable
private fun NowPlayingDiagonalGradientBackground(
    mainColor: Color,
    endColor: Color,
    content: @Composable () -> Unit
) {
    val brush = Brush.linearGradient(
        colorStops = arrayOf(
            0.5f to mainColor,
            1f to endColor
        )
    )

    Box(
        modifier = Modifier
            .background(brush)
            .fillMaxSize()
    ) { content() }
}

@Composable
private fun NowPlayingVerticalGradientBackground(
    palette: Palette?,
    darkTheme: Boolean,
    color: BackgroundColourConfig,
    content: @Composable () -> Unit
) {
    val mainColor = when (color) {
        BackgroundColourConfig.VIBRANT -> palette?.vibrantSwatch
        BackgroundColourConfig.MUTED -> palette?.mutedSwatch
    }?.getColor() ?: MaterialTheme.colorScheme.surface

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
    val brush = Brush.verticalGradient(
        colors = listOf(
            mainColor,
            if (darkTheme) Color.Black else Color.White
        )
    )

    Box(
        modifier = Modifier
            .background(brush)
            .fillMaxSize()
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
    NowPlayingBackground(
        palette = null,
        darkTheme = false,
        style = BackgroundStyleConfig.DIAGONAL_GRADIENT,
        content = { AltText(text = "Hello", isTitle = true) },
    )
}

@Preview(showBackground = true)
@Composable
fun NowPlayingVerticalGradientBackgroundPreview() {
    NowPlayingVerticalGradientBackground(
        mainColor = Color.Red,
        darkTheme = false,
        content = { AltText(text = "Hello", isTitle = true) }
    )
}