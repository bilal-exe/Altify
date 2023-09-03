package bilal.altify.domain.model

import android.graphics.Bitmap

data class AltTrack(
    val artist: String,
    val album: String,
    val duration: Long,
    val name: String,
    val uri: String,
    val imageUri: String?,
    val artwork: Bitmap? = null,
) {
    companion object {
        val example = AltTrack(
            artist = "Artist",
            album = "Album",
            duration = 10000L,
            name = "Title",
            uri = "",
            imageUri = null
        )
    }
}