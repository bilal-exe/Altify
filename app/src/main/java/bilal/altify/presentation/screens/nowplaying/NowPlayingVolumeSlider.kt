package bilal.altify.presentation.screens.nowplaying

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bilal.altify.R
import bilal.altify.presentation.Command
import bilal.altify.presentation.PlaybackCommand
import bilal.altify.presentation.VolumeCommand
import bilal.altify.presentation.util.AltSlider

@Composable
fun NowPlayingVolumeSlider(
    volume: Float,
    executeCommand: (Command) -> Unit,
    darkTheme: Boolean
) {

    SliderDefaults.colors(
        thumbColor = Color.Red,
        activeTrackColor = Color.White,
        inactiveTrackColor = Color.Gray
    )

    Row(
        modifier = Modifier
            .width(LocalConfiguration.current.screenWidthDp.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painterResource(id = R.drawable.volume_mute), "", tint = bodyColor)
        AltSlider(
            progress = volume,
            duration = 1f,
            onSliderMoved = { executeCommand(VolumeCommand.SetVolume(it)) },
            darkTheme = darkTheme
        )
    }

}

@Preview
@Composable
private fun NowPlayingVolumeSliderPreview() {
    NowPlayingVolumeSlider(0.5f, {}, false)
}