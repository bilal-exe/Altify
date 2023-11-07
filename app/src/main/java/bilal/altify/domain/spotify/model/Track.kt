package bilal.altify.domain.spotify.model

data class Track(
    val artist: Artist,
    val artists: Artists,
    val album: Album,
    val duration: Long,
    val name: String,
    val uri: String,
    val imageUri: String?,
) {
    companion object {
        val example = Track(
            artist = Artist("Artist", ""),
            artists = listOf(Artist("Artist", "")),
            album = Album("Album", ""),
            duration = 10000L,
            name = "Title",
            uri = "",
            imageUri = null
        )
    }
}