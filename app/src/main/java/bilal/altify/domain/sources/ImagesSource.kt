package bilal.altify.domain.sources

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

interface ImagesSource {

    val artworkFlow: Flow<Bitmap?>

    fun getArtwork(uri: String)

    val thumbnailFlow: Flow<Map<String, Bitmap>>

    fun getThumbnail(uri: String)

    fun clearThumbnails()

    class ImagesSourceException(override val message: String?): Exception(message)

}