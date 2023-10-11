package bilal.altify.presentation.screens.home.now_playing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bilal.altify.presentation.prefrences.BackgroundStyleConfig
import bilal.altify.presentation.util.SetStatusBarColor

var bottomColor by mutableStateOf(Color.Black)
    private set

@Composable
fun NowPlayingBackground(
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    styleConfig: BackgroundStyleConfig = BackgroundStyleConfig.SOLID,
    content: @Composable () -> Unit,
) {
    val mainColor by rememberUpdatedState(backgroundColor)
    when (styleConfig) {
        BackgroundStyleConfig.SOLID ->
            NowPlayingSolidBackground(
                backgroundColor = mainColor,
                content = content
            )
        BackgroundStyleConfig.VERTICAL_GRADIENT ->
            NowPlayingVerticalGradientBackground(
                mainColor = mainColor,
                content = content
            )
        BackgroundStyleConfig.PLAIN ->
            NowPlayingSolidBackground(
                backgroundColor = MaterialTheme.colorScheme.background,
                content = content
            )
    }
}

@Composable
private fun NowPlayingSolidBackground(
    backgroundColor: Color,
    content: @Composable () -> Unit,
) {
    SetStatusBarColor(color = backgroundColor)
    Box(
        modifier = Modifier
            .size(
                width = LocalConfiguration.current.screenWidthDp.dp,
                height = LocalConfiguration.current.screenHeightDp.dp
            )
            .drawWithCache {
                onDrawBehind {
                    drawRect(backgroundColor)
                }
            }
    ) { content() }
}

@Composable
private fun NowPlayingDiagonalGradientBackground(
    mainColor: Color,
    endColor: Color,
    content: @Composable () -> Unit
) {
    SetStatusBarColor(color = mainColor)
    Box(
        modifier = Modifier
            .size(
                width = LocalConfiguration.current.screenWidthDp.dp,
                height = LocalConfiguration.current.screenHeightDp.dp
            )
            .drawWithCache {
                onDrawBehind {
                    val brush = Brush.linearGradient(
                        colorStops = arrayOf(
                            0.5f to mainColor,
                            1f to endColor
                        )
                    )
                    drawRect(brush = brush)
                }
            }
    ) { content() }
}

@Composable
private fun NowPlayingVerticalGradientBackground(
    mainColor: Color,
    content: @Composable () -> Unit
) {
    val themeBackground = MaterialTheme.colorScheme.background
    SetStatusBarColor(color = mainColor)
    Box(
        modifier = Modifier
            .size(
                width = LocalConfiguration.current.screenWidthDp.dp,
                height = LocalConfiguration.current.screenHeightDp.dp
            )
            .drawWithCache {
                val brush = Brush.verticalGradient(
                    colors = listOf(mainColor, themeBackground)
                )
                onDrawBehind {
                    drawRect(brush = brush)
                }
            }
    ) { content() }
}

@Preview(showBackground = true)
@Composable
private fun NowPlayingDefaultPlainBackgroundPreview() {
    NowPlayingSolidBackground(Color.White) {
        Text(text = "Title")
    }
}

@Preview(showBackground = true)
@Composable
private fun NowPlayingDefaultSolidBackgroundPreview() {
    NowPlayingSolidBackground(Color.Red) {
        Text(text = "Title")
    }
}

@Preview(showBackground = true)
@Composable
private fun NowPlayingDiagonalGradientBackgroundPreview() {
    NowPlayingDiagonalGradientBackground(
        mainColor = Color.Black,
        endColor = Color.White,
        content = { Text(text = "Hello") },
    )
}

@Preview(showBackground = true)
@Composable
fun NowPlayingVerticalGradientBackgroundPreview() {
    NowPlayingVerticalGradientBackground(
        mainColor = Color.Red,
        content = { Text(text = "Hello") }
    )
}