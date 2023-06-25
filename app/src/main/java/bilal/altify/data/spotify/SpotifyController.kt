package bilal.altify.data.spotify

import android.content.Context
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.error.SpotifyAppRemoteException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SpotifyController private constructor(
    private val spotifyAppRemote: SpotifyAppRemote
) {

    val player = Player(spotifyAppRemote.playerApi)

    val volume = Volume(spotifyAppRemote.connectApi)

    val content = Content(spotifyAppRemote.contentApi)

    val image = Images(spotifyAppRemote.imagesApi)

    fun isConnected() =
        spotifyAppRemote.isConnected

    class SpotifyControllerFactory @Inject constructor(
        private val context: Context
    ) {

        private val clientId = "50109e10614941e596e264af1e7b3685"
        private val redirectUri = "altify://now_playing"

        private val connectionParams: ConnectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()

        lateinit var listener: Connector.ConnectionListener

        val connectionsFlow = callbackFlow {

            listener = object : Connector.ConnectionListener {

                override fun onConnected(appRemote: SpotifyAppRemote) {
                    Log.d("SpotifyAppRemote", "Connected")
                    trySend(Result.success(SpotifyController(appRemote)))
                }

                override fun onFailure(throwable: Throwable) {
                    Log.d("SpotifyAppRemote", throwable.toString())
                    trySend(Result.failure(throwable as SpotifyAppRemoteException))
                }

            }

            connect()

            awaitClose {}

        }


        fun connect() {
            Log.d("SpotifyAppRemote", "Connect called")
            SpotifyAppRemote.connect(context, connectionParams, listener)
        }
    }

}