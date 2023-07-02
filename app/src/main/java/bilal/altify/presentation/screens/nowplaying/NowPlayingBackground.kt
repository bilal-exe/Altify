package bilal.altify.presentation.screens.nowplaying

import android.graphics.Bitmap
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
import bilal.altify.presentation.prefrences.AltPreference
import bilal.altify.presentation.util.AltText
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.lang.Exception

var bodyColor by mutableStateOf(Color.Black)
    private set
var titleColor by mutableStateOf(Color.Black)
    private set

enum class BackgroundStyleConfig(override val code: Int, override val title: String) :
    AltPreference {
    SOLID(0, "Solid Colour"),
    DIAGONAL_GRADIENT(1, "Diagonal Gradient"),
    VERTICAL_GRADIENT(2, "Vertical Gradient"),
    PLAIN(3, "Plain")
}

@Composable
fun NowPlayingBackground(
    bitmap: Bitmap? = null,
    darkTheme: Boolean = false,
    style: BackgroundStyleConfig = BackgroundStyleConfig.SOLID,
    content: @Composable () -> Unit,
) {

    val themeColor = if (darkTheme) Color.Black else Color.White

    if (style == BackgroundStyleConfig.PLAIN) {
        bodyColor = if (darkTheme) Color.White else Color.Black
        titleColor = if (darkTheme) Color.White else Color.Black
        NowPlayingSolidBackground(backgroundColor = themeColor, content = content)
        return
    }

    val palette = bitmap?.let { Palette.from(it).generate() }
    val systemUiController = rememberSystemUiController()
    val surfaceColor = MaterialTheme.colorScheme.surface

    if (palette == null) {
        bodyColor = if (darkTheme) Color.White else Color.Black
        titleColor = if (darkTheme) Color.White else Color.Black
    } else {
        titleColor = Color(palette.dominantSwatch!!.titleTextColor)
        bodyColor = Color(palette.dominantSwatch!!.bodyTextColor)
    }

    LaunchedEffect(key1 = palette) {
//        val statusBarColor = if (palette == null) surfaceColor else {
//            when (darkTheme) {
//                true -> palette.darkMutedSwatch?.getColor()
//                false -> palette.lightMutedSwatch?.getColor()
//            } ?: surfaceColor
//        }
        val statusBarColor = palette?.dominantSwatch?.getColor() ?: surfaceColor
        systemUiController.setStatusBarColor(statusBarColor)
    }

    when (style) {

        BackgroundStyleConfig.SOLID -> {
            val backgroundColor = if (palette?.dominantSwatch == null) {
                if (darkTheme) Color.Black else Color.White
            } else palette.dominantSwatch!!.getColor()
            NowPlayingSolidBackground(backgroundColor = backgroundColor, content = content)
        }

        BackgroundStyleConfig.DIAGONAL_GRADIENT -> NowPlayingDiagonalGradientBackground(
            palette = palette,
            darkTheme = darkTheme,
            content = content
        )


        BackgroundStyleConfig.VERTICAL_GRADIENT -> NowPlayingVerticalGradientBackground(
            palette = palette,
            darkTheme = darkTheme,
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
    content: @Composable () -> Unit
) {

    if (palette == null) NowPlayingDiagonalGradientBackground(
        mainColor = if (darkTheme) Color.Black else Color.White,
        endColor = if (darkTheme) Color.LightGray else Color.DarkGray,
        content = content
    ) else {
        val mainColor = palette.dominantSwatch?.getColor() ?: MaterialTheme.colorScheme.surface
        val endColor = (if (darkTheme) palette.darkMutedSwatch?.getColor()
        else palette.lightMutedSwatch?.getColor()) ?: MaterialTheme.colorScheme.surfaceVariant

        NowPlayingDiagonalGradientBackground(
            mainColor = mainColor,
            endColor = endColor,
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
    content: @Composable () -> Unit
) {
    NowPlayingVerticalGradientBackground(
        mainColor = palette?.darkVibrantSwatch?.getColor() ?: MaterialTheme.colorScheme.surface,
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

private fun Palette.Swatch.getColor() = Color(rgb)

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
        bitmap = null,
        darkTheme = false,
        style = BackgroundStyleConfig.DIAGONAL_GRADIENT,
        content = { AltText(text = "Hello", isTitle = true) }
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