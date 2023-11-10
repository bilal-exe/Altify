package bilal.altify.domain.spotify.repositories.appremote

import android.graphics.Bitmap
import bilal.altify.domain.model.RemoteId
import kotlinx.coroutines.flow.Flow

/* this class could be decoupled from the Android framework by removing the Bitmap reference
    * and replacing it with a byte array however this would be expensive as the data layer would
    * convert to byte array and the UI layer would reconstruct */
interface ImagesRepository {

    val artworkFlow: Flow<Bitmap?>

    fun getArtwork(remoteId: RemoteId)

    val thumbnailFlow: Flow<Map<RemoteId, Bitmap>>

    fun getThumbnail(remoteId: RemoteId)

    fun clearThumbnails()

    class ImagesSourceException(override val message: String?) : Exception(message)

}