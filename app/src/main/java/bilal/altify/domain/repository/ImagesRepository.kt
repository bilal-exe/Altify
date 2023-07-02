package bilal.altify.domain.repository

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {

    fun getArtwork(): Flow<Bitmap?>

    fun getArtwork(uri: String)

    suspend fun getThumbnail(uri: String): Bitmap?

}