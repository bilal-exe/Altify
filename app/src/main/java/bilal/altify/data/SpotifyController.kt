package bilal.altify.data

import android.content.Context
import android.graphics.Bitmap
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.error.SpotifyAppRemoteException
import com.spotify.protocol.types.Image
import com.spotify.protocol.types.ImageUri
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SpotifyController private constructor(
    private val spotifyAppRemote: SpotifyAppRemote
) {

    val player = Player(spotifyAppRemote.playerApi)

    val volume = Volume(spotifyAppRemote.connectApi)

    val content = Content(spotifyAppRemote.contentApi)

    suspend fun getLargeImage(imageUri: ImageUri): Bitmap =
            spotifyAppRemote.imagesApi.getImage(imageUri).await().data

    suspend fun geSmallImage(imageUri: ImageUri): Bitmap =
        spotifyAppRemote.imagesApi.getImage(imageUri, Image.Dimension.THUMBNAIL).await().data

    class SpotifyControllerFactory @Inject constructor(
        private val context: Context
    ) {

        private val clientId = "12f6aacd893a4b66b4aa9d6948a45674"
        private val redirectUri = "altify"

        private val connectionParams: ConnectionParams =
            ConnectionParams.Builder(clientId).setRedirectUri(redirectUri).showAuthView(true)
                .build()

        suspend fun connect(): Result<SpotifyController> = suspendCoroutine { continuation ->

            val listener = object : Connector.ConnectionListener {

                override fun onConnected(appRemote: SpotifyAppRemote) =
                    continuation.resume(Result.success(SpotifyController(appRemote)))

                override fun onFailure(throwable: Throwable) =
                    continuation.resume(Result.failure(throwable as SpotifyAppRemoteException))

            }

            SpotifyAppRemote.connect(context, connectionParams, listener)
        }
    }

}