package bilal.altify.presentation.screens.nowplaying

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bilal.altify.presentation.util.WhiteText
import com.spotify.protocol.types.PlayerContext

@Composable
fun NowPlayingTopBar(
    player: PlayerContext, modifier: Modifier = Modifier
) {
    val title = player.title
    val subtitle = player.subtitle
    val type = player.type
    NowPlayingTopBar(title, subtitle, type, modifier)
}

@Composable
private fun NowPlayingTopBar(
    title: String,
    subtitle: String,
    type: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(75.dp)
            .fillMaxWidth()
            .padding(6.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WhiteText(
            text = type,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
        WhiteText(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        WhiteText(text = subtitle, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true, backgroundColor = 0L)
@Composable
private fun NowPlayingTopBarPreview() {
    NowPlayingBackground {
        NowPlayingTopBar("Title", "Subtitle", "Type")
    }
}