package bilal.altify.presentation.screens.nowplaying

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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

var bodyColor by mutableStateOf(Color.Black)
    private set
var titleColor by mutableStateOf(Color.Black)
    private set

enum class BackgroundStyleConfig(override val code: Int, override val title: String) : AltPreference {
    SOLID(0, "Solid Colour"), GRADIENT(1, "Gradient"), PLAIN(2, "Plain")
}

@Composable
fun NowPlayingBackground(
    bitmap: Bitmap? = null,
    darkTheme: Boolean = false,
    style: BackgroundStyleConfig = BackgroundStyleConfig.SOLID,
    content: @Composable () -> Unit,
) {

    val themeColor = if (darkTheme) Color.White else Color.Black

    if (style == BackgroundStyleConfig.PLAIN) {
        NowPlayingSolidBackground(backgroundColor = themeColor, content = content)
        return
    }

    val palette = bitmap?.let { Palette.from(it).generate() }
    val systemUiController = rememberSystemUiController()
    val surfaceColor = MaterialTheme.colorScheme.surface

    if (palette == null) {
        bodyColor = themeColor
        titleColor = themeColor
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

        BackgroundStyleConfig.GRADIENT -> {
            if (palette == null) NowPlayingGradientBackground(
                darkTheme = darkTheme,
                content = content
            ) else NowPlayingGradientBackground(
                palette = palette,
                darkTheme = darkTheme,
                content = content
            )
        }

        else -> throw Exception()
    }
}

@Composable
private fun NowPlayingSolidBackground(
    backgroundColor: Color,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(color = backgroundColor)
            .fillMaxSize(),
    ) { content() }
}

@Composable
private fun NowPlayingGradientBackground(
    palette: Palette?,
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {

    if (palette == null) NowPlayingGradientBackground(
        darkTheme = darkTheme,
        content = content
    ) else {
        val mainColor = palette.dominantSwatch?.getColor() ?: MaterialTheme.colorScheme.surface
        val endColor = (if (darkTheme) palette.darkVibrantSwatch?.getColor()
        else palette.lightMutedSwatch?.getColor()) ?: MaterialTheme.colorScheme.surfaceVariant

        NowPlayingGradientBackground(
            mainColor = mainColor,
            endColor = endColor,
            content = content
        )
    }
}

@Composable
private fun NowPlayingGradientBackground(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) =
    if (darkTheme) NowPlayingGradientBackground(
        mainColor = Color.Black,
        endColor = Color.LightGray,
        content = content
    ) else NowPlayingGradientBackground(
        mainColor = Color.White,
        endColor = Color.DarkGray,
        content = content
    )

@Composable
private fun NowPlayingGradientBackground(
    mainColor: Color,
    endColor: Color,
    content: @Composable () -> Unit
) {
    val brush = Brush.linearGradient(
        colorStops = arrayOf(
            0.6f to mainColor,
            1f to endColor
        )
    )

    Column(
        modifier = Modifier
            .background(brush)
            .fillMaxSize(),
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
private fun NowPlayingGradientBackgroundPreview() {
    NowPlayingBackground(
        bitmap = null,
        darkTheme = false,
        style = BackgroundStyleConfig.GRADIENT,
        content = { AltText(text = "Hello", isTitle = true) }
    )
}