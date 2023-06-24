package bilal.altify.presentation.screens.nowplaying

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.palette.graphics.Palette
import bilal.altify.presentation.prefrences.AltPreference
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val bodyColor = mutableStateOf(Color.Black)
private val titleColor = mutableStateOf(Color.Black)

enum class BackgroundStyleConfig(override val code: Int) : AltPreference {
    SOLID(0), GRADIENT(1), PLAIN(2)
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
        bodyColor.value = themeColor
        titleColor.value = themeColor
    } else {
        titleColor.value = Color(palette.dominantSwatch!!.titleTextColor)
        bodyColor.value = Color(palette.dominantSwatch!!.bodyTextColor)
    }

    LaunchedEffect(key1 = palette) {
        val statusBarColor = if (palette == null) surfaceColor else {
            when (darkTheme) {
                true -> palette.darkVibrantSwatch?.getColor()
                false -> palette.lightVibrantSwatch?.getColor()
            } ?: surfaceColor
        }
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
    Box(
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

    Box(
        modifier = Modifier
            .background(brush)
            .fillMaxSize(),
    ) { content() }
}

private fun Palette.Swatch.getColor() = Color(rgb)

@Composable
fun AltText(
    text: String,
    modifier: Modifier = Modifier,
    isTitle: Boolean = true,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current
) {
    val color by remember {
        if (isTitle) titleColor else bodyColor
    }
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = style,
    )
    Log.d("COLOR", color.toArgb().toString())
}

@Composable
fun AltText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    isTitle: Boolean = true,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    val color by remember {
        if (isTitle) titleColor else bodyColor
    }
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = style,
    )
    Log.d("COLOR", color.toArgb().toString())
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
private fun NowPlayingGradientBackgroundPreview() {
    NowPlayingBackground(
        bitmap = null,
        darkTheme = false,
        style = BackgroundStyleConfig.GRADIENT,
        content = { AltText(text = "Hello", isTitle = true) }
    )
}