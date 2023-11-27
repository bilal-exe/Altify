package bilal.altify.domain.model

data class AudioFeatures(
    val id: String,
    val acousticness: Double,
    val analysisUrl: String,
    val danceability: Double,
    val durationMs: Int,
    val energy: Double,
    val instrumentalness: Double,
    val key: Int,
    val liveness: Double,
    val loudness: Double,
    val mode: Int,
    val speechiness: Double,
    val tempo: Double,
    val timeSignature: Int,
    val valence: Double
)
