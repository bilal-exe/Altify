package bilal.altify.data.spotify

import android.graphics.Bitmap
import android.util.Log
import bilal.altify.domain.repository.ImagesRepository
import com.spotify.android.appremote.api.ImagesApi
import com.spotify.protocol.types.Image
import com.spotify.protocol.types.ImageUri
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.TimeUnit

class ImagesRepositoryImpl(
    private val imagesApi: ImagesApi
) : ImagesRepository {

    private lateinit var artworkCallback: (Bitmap?) -> Unit

    override fun getArtwork(): Flow<Bitmap?> = callbackFlow {
        trySend(null)
        artworkCallback = { trySend(it) }
        awaitClose { this.cancel() }
    }
        .flowOn(IO)

    override fun getArtwork(uri: String) {
        imagesApi.getImage(ImageUri(uri), Image.Dimension.LARGE)
            .setResultCallback { artworkCallback(it) }
            .setErrorCallback {
                Log.d("Error", it.localizedMessage?: "")
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