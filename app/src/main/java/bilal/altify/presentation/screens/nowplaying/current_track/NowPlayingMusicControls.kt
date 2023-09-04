package bilal.altify.presentation.screens.nowplaying.current_track

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import bilal.altify.presentation.Command
import bilal.altify.presentation.PlaybackCommand
import bilal.altify.presentation.screens.nowplaying.complementColor
import bilal.altify.presentation.screens.nowplaying.nowPlayingItemsPadding
import bilal.altify.presentation.util.getComplementaryColor

@Composable
fun NowPlayingMusicControls(
    executeCommand: (Command) -> Unit,
    isPaused: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(nowPlayingItemsPadding),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MusicControlButton(
            onClick = { executeCommand(PlaybackCommand.SkipPrevious) },
            painter = painterResource(id = R.drawable.skip_previous)
        )
        Crossfade(targetState = isPaused, animationSpec = tween(durationMillis = 1000),
            label = ""
        ) { isPaused ->
            MusicControlButton(
                onClick = { executeCommand(PlaybackCommand.PauseResume(isPaused)) },
                painter = painterResource(id = if (isPaused) R.drawable.play else R.drawable.pause),
                color = complementColor,
                iconColor = getComplementaryColor(complementColor),
            )
        }
        MusicControlButton(
            onClick = { executeCommand(PlaybackCommand.SkipNext) },
            painter = painterResource(id = R.drawable.skip_next)
        )
    }
}

@Composable
private fun MusicControlButton(
    onClick: () -> Unit,
    painter: Painter,
    color: Color = Color.Transparent,
    iconColor: Color = complementColor
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
    NowPlayingMusicControls({}, true)
}