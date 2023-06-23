package bilal.altify.data.spotify

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.error.SpotifyAppRemoteException
import com.spotify.protocol.types.Image
import com.spotify.protocol.types.ImageUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SpotifyController private constructor(
    private val spotifyAppRemote: SpotifyAppRemote
) {

    private val _largeImage = MutableStateFlow<Bitmap?>(null)
    val largeImage = _largeImage.asStateFlow()

    val player = Player(spotifyAppRemote.playerApi)

    val volume = Volume(spotifyAppRemote.connectApi)

    val content = Content(spotifyAppRemote.contentApi)

    fun getLargeImage(uri: String) {
        CoroutineScope(IO).launch {
            spotifyAppRemote.imagesApi.getImage(ImageUri(uri)).setResultCallback {
                _largeImage.value = it
            }.setErrorCallback {
                throw Exception()
            }
        }
    }

//    suspend fun geSmallImage(uri: String): Bitmap =
//        spotifyAppRemote.imagesApi.getImage(ImageUri(uri), Image.Dimension.THUMBNAIL).await().data

    class SpotifyControllerFactory @Inject constructor(
        private val context: Context
    ) {

        private val clientId = "50109e10614941e596e264af1e7b3685"
        private val redirectUri = "altify://now_playing"

        private val connectionParams: ConnectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()

        suspend fun connect(): Result<SpotifyController> = suspendCoroutine { continuation ->

            val listener = object : Connector.ConnectionListener {

                override fun onConnected(appRemote: SpotifyAppRemote) =
                    continuation.resume(Result.success(SpotifyController(appRemote)))

                override fun onFailure(throwable: Throwable) {
                    Log.d("SpotifyAppRemote", throwable.toString())
                    continuation.resume(Result.failure(throwable as SpotifyAppRemoteException))
                }

            }

            SpotifyAppRemote.connect(context, connectionParams, listener)
        }
    }

}