package bilal.altify.data.spotify.remote

import android.content.Context
import android.util.Log
import bilal.altify.data.spotify.ContentRepositoryImpl
import bilal.altify.data.spotify.ImagesRepositoryImpl
import bilal.altify.data.spotify.PlayerRepositoryImpl
import bilal.altify.data.spotify.VolumeRepositoryImpl
import bilal.altify.domain.controller.AltifyRepositories
import bilal.altify.domain.repository.SpotifyConnector
import bilal.altify.domain.repository.SpotifyConnectorResponse
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

    private lateinit var listener: Connector.ConnectionListener

    override val spotifyConnectorFlow = callbackFlow {

        var spotifyAppRemote: SpotifyAppRemote? = null

        listener = object : Connector.ConnectionListener {

            override fun onConnected(appRemote: SpotifyAppRemote) {
                Log.d("SpotifyAppRemote", "Connected")
                trySend(
                    SpotifyConnectorResponse.Connected(getRepositories(appRemote))
                )
                spotifyAppRemote = appRemote
            }

            override fun onFailure(throwable: Throwable) {
                Log.d("SpotifyAppRemote", throwable.toString())
                trySend(
                    SpotifyConnectorResponse.ConnectionFailed(throwable as SpotifyAppRemoteException)
                )
            }

        }

        connect()

        awaitClose {
            if (spotifyAppRemote != null) SpotifyAppRemote.disconnect(spotifyAppRemote)
        }

    }

    override fun connect() {
        SpotifyAppRemote.connect(context, connectionParams, listener)
    }

    private fun getRepositories(sar: SpotifyAppRemote): AltifyRepositories =
        AltifyRepositories(
            player = PlayerRepositoryImpl(sar.playerApi),
            content = ContentRepositoryImpl(sar.contentApi),
            images = ImagesRepositoryImpl(sar.imagesApi),
            volume = VolumeRepositoryImpl(sar.connectApi)
        )

    companion object {
        private const val clientId = "50109e10614941e596e264af1e7b3685"
        private const val redirectUri = "altify://now_playing"
    }
}