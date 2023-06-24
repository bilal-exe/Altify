package bilal.altify.presentation.screens.nowplaying

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.google.accompanist.systemuicontroller.rememberSystemUiController

val bodyColor = mutableStateOf(Color.Black)
val titleColor = mutableStateOf(Color.Black)

@Composable
fun NowPlayingBackground(
    bitmap: Bitmap? = null,
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    if (bitmap == null) NowPlayingGradientBackground(darkTheme, content)
    else NowPlayingGradientBackground(bitmap, darkTheme, content)
}

@Composable
private fun NowPlayingGradientBackground(
    bitmap: Bitmap,
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val palette = Palette.from(bitmap).generate()
    val systemUiController = rememberSystemUiController()
    val surfaceColor = MaterialTheme.colorScheme.surface
    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant

    val mainColor = palette.dominantSwatch?.getColor() ?: surfaceColor
    val endColor = (if (darkTheme) palette.darkMutedSwatch?.getColor()
    else palette.lightMutedSwatch?.getColor()) ?: surfaceVariantColor

    titleColor.value = Color(palette.dominantSwatch!!.titleTextColor)
    bodyColor.value = Color(palette.dominantSwatch!!.bodyTextColor)

    LaunchedEffect(key1 = palette) {
        val statusBarColor = palette.darkVibrantSwatch?.getColor() ?: surfaceColor
        systemUiController.setStatusBarColor(statusBarColor)
    }

    NowPlayingGradientBackground(
        mainColor = mainColor,
        endColor = endColor,
        content = content
    )
}

// all the functions end here
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
        verticalArrangement = Arrangement.SpaceEvenly
    ) { content() }
}

@Composable
private fun NowPlayingGradientBackground(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) =
    if (darkTheme) {
        bodyColor.value = Color.White
        titleColor.value = Color.White
        NowPlayingGradientBackground(
            mainColor = Color.Black,
            endColor = Color.LightGray,
            content = content
        )
    } else {
        bodyColor.value = Color.Black
        titleColor.value = Color.Black
        NowPlayingGradientBackground(
            mainColor = Color.White,
            endColor = Color.DarkGray,
            content = content
        )
    }

private fun Palette.Swatch.getColor() = Color(rgb)

@Preview(showBackground = true)
@Composable
private fun NowPlayingBackgroundPreview() {
    NowPlayingGradientBackground(
        mainColor = Color.White,
        endColor = Color.Black
    ) {
        Text(text = "Hello")
    }
}

@Preview(showBackground = true)
@Composable
private fun NowPlayingDefualtBackgroundPreview() {
    NowPlayingGradientBackground(true) {
        AltText(text = "Title")
    }
}

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