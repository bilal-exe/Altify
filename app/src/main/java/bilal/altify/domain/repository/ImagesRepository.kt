package bilal.altify.domain.repository

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {

    fun getArtworkFlow(): Flow<Bitmap?>

    fun getArtwork(uri: String)

    fun getThumbnailFlow(): Flow<Map<String, Bitmap>>

    suspend fun getThumbnail(uri: String)

    fun clearThumbnails()

}