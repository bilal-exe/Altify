package bilal.altify.data.spotify.repositories

import android.graphics.Bitmap
import android.util.Log
import bilal.altify.data.mappers.toSpotifyImageUri
import bilal.altify.domain.model.ImageRemoteId
import bilal.altify.domain.spotify.repositories.appremote.ImagesRepository
import com.spotify.android.appremote.api.ImagesApi
import com.spotify.protocol.types.Image.Dimension
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ImagesRepositoryImpl(
    private val imagesApi: ImagesApi
) : ImagesRepository {

    private val _artworkFlow = MutableStateFlow<Bitmap?>(null)
    override val artworkFlow = _artworkFlow.asStateFlow()
    private fun artworkCallback(bmp: Bitmap?) { _artworkFlow.value = bmp }

    private val _thumbnailFlow = MutableStateFlow<Map<ImageRemoteId, Bitmap>>(emptyMap())
    override val thumbnailFlow = _thumbnailFlow.asStateFlow()
    private fun thumbnailCallback(bmp: Bitmap, imageRemoteId: ImageRemoteId) {
        _thumbnailFlow.update { thumbnailFlow.value + (imageRemoteId to bmp) }
    }

    override fun getArtwork(imageRemoteId: ImageRemoteId) {
        imagesApi.getImage(imageRemoteId.toSpotifyImageUri(), Dimension.LARGE)
            .setResultCallback(::artworkCallback)
            .setErrorCallback {
                Log.d("Error", "${it.message.toString()} $imageRemoteId")
                throw ImagesRepository.ImagesSourceException(it.localizedMessage)
            }
    }

    override fun getThumbnail(imageRemoteId: ImageRemoteId) {
        imagesApi.getImage(imageRemoteId.toSpotifyImageUri(), Dimension.THUMBNAIL)
            .setResultCallback {
                thumbnailCallback(it, imageRemoteId)
            }
            .setErrorCallback {
                Log.d("Error", "${it.message.toString()} $imageRemoteId")
                throw ImagesRepository.ImagesSourceException(it.localizedMessage)
            }
    }

    override fun clearThumbnails() {
        _thumbnailFlow.value = emptyMap()
    }
}