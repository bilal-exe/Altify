package bilal.altify.presentation.screens.nowplaying

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import bilal.altify.R

@Composable
fun NowPlayingMusicControls(
    pauseResume: () -> Unit,
    skipPrevious: () -> Unit,
    skipNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MusicControlButton(onClick = skipPrevious, painter = painterResource(id = R.drawable.skip_previous))
        MusicControlButton(
            onClick = pauseResume,
            painter = painterResource(id = R.drawable.play_pause),
            iconColor = Color.Black,
            color = Color.White
        )
        MusicControlButton(onClick = skipNext, painter = painterResource(id = R.drawable.skip_next))
    }
}

@Composable
private fun MusicControlButton(
    onClick: () -> Unit,
    painter: Painter,
    color: Color = Color.Transparent,
    iconColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Icon(
            painter = painter,
            contentDescription = "",
            tint = iconColor
        )
    }
}

@Preview
@Composable
private fun NowPlayingMusicControlsPreview() {
    NowPlayingMusicControls({}, {}, {})
}