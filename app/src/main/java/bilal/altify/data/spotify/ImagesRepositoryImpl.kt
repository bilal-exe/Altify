package bilal.altify.data.spotify

import android.graphics.Bitmap
import android.util.Log
import bilal.altify.domain.repository.ImagesRepository
import com.spotify.android.appremote.api.ImagesApi
import com.spotify.protocol.types.Image
import com.spotify.protocol.types.ImageUri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ImagesRepositoryImpl(
    private val imagesApi: ImagesApi
) : ImagesRepository {

    private val _artworkFlow = MutableStateFlow<Bitmap?>(null)
    private val artworkFlow = _artworkFlow.asStateFlow()
    private fun artworkCallback(bmp: Bitmap?) { _artworkFlow.value = bmp }
    override fun getArtworkFlow(): Flow<Bitmap?> = artworkFlow

    private val _thumbnailFlow = MutableStateFlow<Map<String, Bitmap>>(emptyMap())
    private val thumbnailFlow = _thumbnailFlow.asStateFlow()
    private fun thumbnailCallback(bmp: Bitmap, uri: String) {
        _thumbnailFlow.update {
            val map = thumbnailFlow.value.toMutableMap()
            map[uri] = bmp
            map.toMap()
        }
    }
    override fun getThumbnailFlow(): Flow<Map<String, Bitmap>> = thumbnailFlow

    override fun getArtwork(uri: String) {
        imagesApi.getImage(ImageUri(uri), Image.Dimension.LARGE)
            .setResultCallback {
                artworkCallback(it)
            }
            .setErrorCallback {
                Log.d("Error", it.localizedMessage ?: "")
            }
    }

    override suspend fun getThumbnail(uri: String) {
        imagesApi.getImage(ImageUri(uri), Image.Dimension.THUMBNAIL)
            .setResultCallback {
                thumbnailCallback(it, uri)
            }
            .setErrorCallback {
                Log.d("Error", it.localizedMessage ?: "")
            }
    }

    override fun clearThumbnails() {
        _thumbnailFlow.value = emptyMap()
    }
}