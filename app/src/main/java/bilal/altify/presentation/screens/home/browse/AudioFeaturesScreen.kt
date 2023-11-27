package bilal.altify.presentation.screens.home.browse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bilal.altify.domain.model.AudioFeatures
import bilal.altify.domain.model.ContentType
import bilal.altify.domain.model.Item
import bilal.altify.domain.model.RemoteId
import bilal.altify.domain.model.SimpleItem
import kotlin.time.Duration.Companion.milliseconds

fun LazyListScope.audioFeaturesScreen(audioFeatures: AudioFeatures, track: Item.Track) {
    item { AudioFeaturesScreen(audioFeatures, track) }
}

@Composable
private fun AudioFeaturesScreen(audioFeatures: AudioFeatures, track: Item.Track) {
    Column (
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = track.name,
            style = MaterialTheme.typography.titleLarge,
        )
        Divider()
        AudioFeaturesSection("acousticness", audioFeatures.acousticness)
        AudioFeaturesSection("danceability", audioFeatures.danceability)
        AudioFeaturesSection("durationMs", audioFeatures.durationMs)
        AudioFeaturesSection("energy", audioFeatures.energy)
        AudioFeaturesSection("instrumentalness", audioFeatures.instrumentalness)
        AudioFeaturesSection("key", audioFeatures.key)
        AudioFeaturesSection("liveness", audioFeatures.liveness)
        AudioFeaturesSection("loudness", audioFeatures.loudness)
        AudioFeaturesSection("mode", audioFeatures.mode)
        AudioFeaturesSection("speechiness", audioFeatures.speechiness)
        AudioFeaturesSection("tempo", audioFeatures.tempo)
        AudioFeaturesSection("timeSignature", audioFeatures.timeSignature)
        AudioFeaturesSection("valence", audioFeatures.valence)
    }
}

@Composable
fun AudioFeaturesSection(title: String, value: Any) {
    Row {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
    Divider()
}

@Preview
@Composable
fun AudioFeaturesScreenPreview() {
    val audioFeatures = AudioFeatures(
        danceability = 0.683,
        energy = 0.772,
        key = 7,
        loudness = -7.907,
        mode = 0,
        speechiness = 0.0311,
        acousticness = 0.0222,
        instrumentalness = 0.0149,
        liveness = 0.203,
        valence = 0.737,
        tempo = 123.871,
        id = "2ahnofp2LbBWDXcJbMaSTu",
        analysisUrl = "https=//api.spotify.com/v1/audio-analysis/2ahnofp2LbBWDXcJbMaSTu",
        durationMs = 270373,
        timeSignature = 4
    )
    val track = SimpleItem.Track(
        remoteId = RemoteId("", ContentType.Track),
        name = "sweet victory",
        artist = SimpleItem.Artist(
            RemoteId("", ContentType.Artist),
            "Spongebob",
        ),
        album = SimpleItem.Album(
            RemoteId("", ContentType.Album),
            "Bangerz",
        ),
        artists = emptyList(),
        duration = 5000.milliseconds,
        imageId = null
    )
    AudioFeaturesScreen(audioFeatures = audioFeatures, track = track)
}