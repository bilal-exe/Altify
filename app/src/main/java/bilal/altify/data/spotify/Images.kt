package bilal.altify.data.spotify

import android.graphics.Bitmap
import com.spotify.android.appremote.api.ImagesApi
import com.spotify.protocol.types.Image
import com.spotify.protocol.types.ImageUri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit

class Images(
    private val imagesApi: ImagesApi
) {

    private val _artwork = MutableStateFlow<Bitmap?>(null)
    val artwork = _artwork.asStateFlow()

    suspend fun getLargeImage(uri: String) {
        imagesApi.getImage(ImageUri(uri), Image.Dimension.LARGE)
            .setResultCallback {
                _artwork.value = it
            }
            .setErrorCallback {
                throw Exception("Error callback")
            }
    }

    suspend fun getSmallImage(uri: String): Bitmap? {
        val res = imagesApi.getImage(
            ImageUri(uri), Image.Dimension.SMALL
        )
            .await(5, TimeUnit.SECONDS)
        if (res.isSuccessful) return res.data
        else throw Exception("Error callback")
    }

}