package bilal.altify.data.spotify

import android.graphics.Bitmap
import com.spotify.android.appremote.api.ImagesApi
import com.spotify.protocol.types.Image
import com.spotify.protocol.types.ImageUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import java.util.concurrent.TimeUnit

class Images(
    private val imagesApi: ImagesApi
) {

    private lateinit var artworkCallback: (Bitmap?) -> Unit

    val artwork = callbackFlow {
        artworkCallback = { trySend(it) }
        awaitClose{ this.cancel() }
    }
        .flowOn(IO)
        .stateIn(CoroutineScope(IO), SharingStarted.Eagerly, null)

    fun getLargeImage(uri: String) {
        imagesApi.getImage(ImageUri(uri), Image.Dimension.LARGE)
            .setResultCallback { artworkCallback(it) }
            .setErrorCallback { throw Exception("Error callback") }
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