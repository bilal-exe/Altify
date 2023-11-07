package bilal.altify.domain.spotify.model

data class Artist(
    val name: String,
    val uri: String
)

typealias Artists = List<Artist>
