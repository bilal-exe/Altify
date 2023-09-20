package bilal.altify.domain.repository

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {

    val artworkFlow: Flow<Bitmap?>

    fun getArtwork(uri: String)

    val thumbnailFlow: Flow<Map<String, Bitmap>>

    fun getThumbnail(uri: String)

    fun clearThumbnails()

}