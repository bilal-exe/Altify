package bilal.altify.presentation.screens.home.now_playing

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bilal.altify.R
import bilal.altify.domain.spotify.model.RepeatMode
import bilal.altify.domain.spotify.use_case.model.Command
import bilal.altify.domain.spotify.use_case.model.PlaybackCommand
import bilal.altify.presentation.screens.home.nowPlayingItemsPadding
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Composable
fun NowPlayingMusicControls(
    executeCommand: (Command) -> Unit,
    isPaused: Boolean,
    repeatMode: RepeatMode,
    isShuffled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(nowPlayingItemsPadding),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MusicControlButton(
            onClick = { executeCommand(PlaybackCommand.ToggleShuffle) },
            painter = painterResource(id = if (isShuffled) R.drawable.shuffle_on else R.drawable.shuffle_off),
            modifier = Modifier.size(30.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        MusicControlButton(
            onClick = { executeCommand(PlaybackCommand.SkipPrevious) },
            painter = painterResource(id = R.drawable.skip_previous),
            dragToSeekRelative = { executeCommand(PlaybackCommand.SeekRelative(-it)) },
            modifier = Modifier.size(65.dp),
        )
        Crossfade(
            targetState = isPaused, animationSpec = tween(durationMillis = 1000),
            label = ""
        ) { isPaused ->
            MusicControlButton(
                onClick = { executeCommand(PlaybackCommand.PauseResume(isPaused)) },
                painter = painterResource(id = if (isPaused) R.drawable.play else R.drawable.pause),
                containerColor = MaterialTheme.colorScheme.onBackground,
                iconColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.size(height = 60.dp, width = 80.dp),
            )
        }
        MusicControlButton(
            onClick = { executeCommand(PlaybackCommand.SkipNext) },
            painter = painterResource(id = R.drawable.skip_next),
            dragToSeekRelative = { executeCommand(PlaybackCommand.SeekRelative(it)) },
            modifier = Modifier.size(65.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        MusicControlButton(
            onClick = { executeCommand(PlaybackCommand.ToggleRepeat) },
            painter = painterResource(
                when (repeatMode) {
                    RepeatMode.OFF -> R.drawable.repeat
                    RepeatMode.CONTEXT -> R.drawable.repeat_on
                    RepeatMode.TRACK -> R.drawable.repeat_one_on
                }
            ),
            modifier = Modifier.size(30.dp),
        )
    }
}

@Composable
private fun MusicControlButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    painter: Painter,
    containerColor: Color = Color.Transparent,
    iconColor: Color = MaterialTheme.colorScheme.onBackground,
    dragToSeekRelative: ((Long) -> Unit)? = null
) {
    var timeInSecs by remember { mutableFloatStateOf(0f) }
    val setTime: (Float) -> Unit = { timeInSecs = it }
    Box(
        modifier = if (dragToSeekRelative == null) modifier
        else modifier.dragToSeekRelative(dragToSeekRelative, setTime),
        contentAlignment = Alignment.Center
    ) {
        if (timeInSecs == 0f) {
            IconButton(
                onClick = onClick,
                colors = IconButtonDefaults.iconButtonColors(containerColor = containerColor),
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    painter = painter,
                    contentDescription = "",
                    tint = iconColor,
                )
            }
        } else {
            Text(
                text = timeInSecs.roundToInt().toString(),
                fontSize = 30.sp
            )
        }
    }
}

private const val DISTANCE_MOVED_COEFFICIENT = 20

private fun Modifier.dragToSeekRelative(seekRelative: (Long) -> Unit, setTime: (Float) -> Unit) =
    this.pointerInput(Unit) {
        var xDistance = 0f
        var yDistance = 0f
        this.detectDragGestures(
            onDragEnd = {
                seekRelative(
                    DISTANCE_MOVED_COEFFICIENT * pythagoras(xDistance, yDistance).toLong()
                )
                xDistance = 0f
                yDistance = 0f
                setTime(0f)
            }
        ) { _, dragAmount ->
            xDistance += dragAmount.x
            yDistance += dragAmount.y
            setTime(DISTANCE_MOVED_COEFFICIENT * pythagoras(xDistance, yDistance) * 0.001f)
        }
    }

private fun pythagoras(x: Float, y: Float) =
    sqrt((x * x) + (y * y))

@Preview(showBackground = true)
@Composable
private fun NowPlayingMusicControlsPreview() {
    NowPlayingMusicControls({}, true, RepeatMode.OFF, true)
}