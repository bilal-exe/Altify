package bilal.altify.data.spotify.model

import bilal.altify.domain.model.AudioFeatures
import kotlinx.serialization.Serializable

@Serializable
data class NetworkAudioFeatures(
    val id: String,
    val acousticness: Double,
    val analysis_url: String,
    val danceability: Double,
    val duration_ms: Int,
    val energy: Double,
    val instrumentalness: Double,
    val key: Int,
    val liveness: Double,
    val loudness: Double,
    val mode: Int,
    val speechiness: Double,
    val tempo: Double,
    val time_signature: Int,
    val valence: Double
) {

    fun toAudioFeatures() =
        AudioFeatures(
            id = id,
            acousticness = acousticness,
            analysisUrl = analysis_url,
            danceability = danceability,
            durationMs = duration_ms,
            energy = energy,
            instrumentalness = instrumentalness,
            key = key,
            liveness = liveness,
            loudness = loudness,
            mode = mode,
            speechiness = speechiness,
            tempo = tempo,
            timeSignature = time_signature,
            valence = valence,
        )

}