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
import java.util.concurrent.TimeUnit

class ImagesRepositoryImpl(
    private val imagesApi: ImagesApi
) : ImagesRepository {

    private fun artworkCallback(bmp: Bitmap?) {
        _artworkFlow.value = bmp
    }

    private val _artworkFlow = MutableStateFlow<Bitmap?>(null)
    private val artworkFlow = _artworkFlow.asStateFlow()

    override fun getArtworkFlow(): Flow<Bitmap?> =
        artworkFlow

    override fun getArtwork(uri: String) {
        imagesApi.getImage(ImageUri(uri), Image.Dimension.LARGE)
            .setResultCallback {
                artworkCallback(it)
            }
            .setErrorCallback {
                Log.d("Error", it.localizedMessage ?: "")
            }
    }

    override suspend fun getThumbnail(uri: String): Bitmap? {
        val res = imagesApi.getImage(
            /* imageUri = */ ImageUri(uri),
            /* dimension = */ Image.Dimension.SMALL
        ).await(
            /* timeout = */ 5,
            /* timeUnit = */ TimeUnit.SECONDS
        )
        if (res.isSuccessful) return res.data
        else throw Exception("Error callback")
    }

}