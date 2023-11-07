package bilal.altify.domain.spotify.model

data class Track(
    val artist: String,
    val album: String,
    val duration: Long,
    val name: String,
    val uri: String,
    val imageUri: String?,
)