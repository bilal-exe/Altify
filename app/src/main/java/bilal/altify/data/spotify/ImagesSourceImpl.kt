package bilal.altify.data.spotify

import android.graphics.Bitmap
import android.util.Log
import bilal.altify.domain.sources.ImagesSource
import com.spotify.android.appremote.api.ImagesApi
import com.spotify.protocol.types.Image
import com.spotify.protocol.types.ImageUri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ImagesSourceImpl(
    private val imagesApi: ImagesApi
) : ImagesSource {

    private val _artworkFlow = MutableStateFlow<Bitmap?>(null)
    override val artworkFlow = _artworkFlow.asStateFlow()
    private fun artworkCallback(bmp: Bitmap?) { _artworkFlow.value = bmp }

    private val _thumbnailFlow = MutableStateFlow<Map<String, Bitmap>>(emptyMap())
    override val thumbnailFlow = _thumbnailFlow.asStateFlow()
    private fun thumbnailCallback(bmp: Bitmap, uri: String) {
        _thumbnailFlow.update {
            val map = thumbnailFlow.value.toMutableMap()
            map[uri] = bmp
            map.toMap()
        }
    }

    override fun getArtwork(uri: String) {
        imagesApi.getImage(ImageUri(uri), Image.Dimension.LARGE)
            .setResultCallback {
                artworkCallback(it)
            }
            .setErrorCallback {
                throw ImagesSource.ImagesSourceException(it.localizedMessage)
            }
    }

    override fun getThumbnail(uri: String) {
        imagesApi.getImage(ImageUri(uri), Image.Dimension.THUMBNAIL)
            .setResultCallback {
                thumbnailCallback(it, uri)
            }
            .setErrorCallback {
                throw ImagesSource.ImagesSourceException(it.localizedMessage)
            }
    }

    override fun clearThumbnails() {
        _thumbnailFlow.value = emptyMap()
    }
}