package bilal.altify.domain.spotify.repositories

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {

    val artworkFlow: Flow<Bitmap?>

    fun getArtwork(uri: String)

    val thumbnailFlow: Flow<Map<String, Bitmap>>

    fun getThumbnail(uri: String)

    fun clearThumbnails()

    class ImagesSourceException(override val message: String?): Exception(message)

}