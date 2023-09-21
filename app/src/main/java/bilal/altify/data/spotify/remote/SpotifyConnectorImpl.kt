package bilal.altify.data.spotify.remote

import android.content.Context
import android.util.Log
import bilal.altify.data.spotify.ContentSourceImpl
import bilal.altify.data.spotify.ImagesSourceImpl
import bilal.altify.data.spotify.PlayerSourceImpl
import bilal.altify.data.spotify.UserSourceImpl
import bilal.altify.data.spotify.VolumeSourceImpl
import bilal.altify.domain.controller.AltifySources
import bilal.altify.domain.sources.SpotifyConnector
import bilal.altify.domain.sources.SpotifyConnectorResponse
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.error.SpotifyAppRemoteException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class SpotifyConnectorImpl(
    private val context: Context
) : SpotifyConnector {

    private val connectionParams: ConnectionParams = ConnectionParams
        .Builder(clientId)
        .setRedirectUri(redirectUri)
        .showAuthView(true)
        .build()

    override fun connect() = callbackFlow {

        var spotifyAppRemote: SpotifyAppRemote? = null

        val listener = object : Connector.ConnectionListener {

            override fun onConnected(sar: SpotifyAppRemote) {
                Log.d("SpotifyAppRemote", "Connected")
                trySend(
                    SpotifyConnectorResponse.Connected(
                        AltifySources(
                            player = PlayerSourceImpl(sar.playerApi),
                            content = ContentSourceImpl(sar.contentApi),
                            images = ImagesSourceImpl(sar.imagesApi),
                            volume = VolumeSourceImpl(sar.connectApi),
                            user = UserSourceImpl(sar.userApi)
                        )
                    )
                )
                spotifyAppRemote = sar
            }

            override fun onFailure(throwable: Throwable) {
                Log.d("SpotifyAppRemote", throwable.toString())
                trySend(
                    SpotifyConnectorResponse.ConnectionFailed(throwable as SpotifyAppRemoteException)
                )
            }

        }

        SpotifyAppRemote.connect(context, connectionParams, listener)

        awaitClose {
            SpotifyAppRemote.disconnect(spotifyAppRemote)
        }

    }

    companion object {
        private const val clientId = "50109e10614941e596e264af1e7b3685"
        private const val redirectUri = "altify://now_playing"
    }
}