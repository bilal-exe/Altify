package bilal.altify.data.spotify.repositories

import android.graphics.Bitmap
import bilal.altify.data.mappers.toSpotifyUri
import bilal.altify.domain.model.RemoteId
import bilal.altify.domain.spotify.repositories.appremote.ImagesRepository
import com.spotify.android.appremote.api.ImagesApi
import com.spotify.protocol.types.Image.Dimension
import com.spotify.protocol.types.ImageUri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ImagesRepositoryImpl(
    private val imagesApi: ImagesApi
) : ImagesRepository {

    private val _artworkFlow = MutableStateFlow<Bitmap?>(null)
    override val artworkFlow = _artworkFlow.asStateFlow()
    private fun artworkCallback(bmp: Bitmap?) { _artworkFlow.value = bmp }

    private val _thumbnailFlow = MutableStateFlow<Map<RemoteId, Bitmap>>(emptyMap())
    override val thumbnailFlow = _thumbnailFlow.asStateFlow()
    private fun thumbnailCallback(bmp: Bitmap, remoteId: RemoteId) {
        _thumbnailFlow.update {
            thumbnailFlow.value + (remoteId to bmp)
        }
    }

    override fun getArtwork(remoteId: RemoteId) {
        imagesApi.getImage(ImageUri(remoteId.toSpotifyUri()), Dimension.LARGE)
            .setResultCallback(::artworkCallback)
            .setErrorCallback {
                throw ImagesRepository.ImagesSourceException(it.localizedMessage)
            }
    }

    override fun getThumbnail(remoteId: RemoteId) {
        imagesApi.getImage(ImageUri(remoteId.toSpotifyUri()), Dimension.THUMBNAIL)
            .setResultCallback {
                thumbnailCallback(it, remoteId)
            }
            .setErrorCallback {
                throw ImagesRepository.ImagesSourceException(it.localizedMessage)
            }
    }

    override fun clearThumbnails() {
        _thumbnailFlow.value = emptyMap()
    }
}