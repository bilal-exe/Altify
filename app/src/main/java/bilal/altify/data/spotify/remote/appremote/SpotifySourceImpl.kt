package bilal.altify.data.spotify.remote.appremote

import android.content.Context
import android.util.Log
import bilal.altify.data.spotify.repositories.ContentRepositoryImpl
import bilal.altify.data.spotify.repositories.ImagesRepositoryImpl
import bilal.altify.data.spotify.repositories.PlayerRepositoryImpl
import bilal.altify.data.spotify.repositories.UserRepositoryImpl
import bilal.altify.data.spotify.repositories.VolumeRepositoryImpl
import bilal.altify.domain.spotify.repositories.appremote.SpotifyConnector
import bilal.altify.domain.spotify.repositories.appremote.SpotifyConnector.Companion.CLIENT_ID
import bilal.altify.domain.spotify.repositories.appremote.SpotifyConnector.Companion.REDIRECT_URI
import bilal.altify.domain.spotify.repositories.appremote.SpotifyConnectorResponse
import bilal.altify.domain.spotify.repositories.appremote.SpotifySource
import bilal.altify.domain.spotify.repositories.appremote.util.AltifyRepositories
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext


class SpotifySourceImpl(
    private val context: Context
) : SpotifySource, SpotifyConnector {

    private val connectionParams: ConnectionParams = ConnectionParams
        .Builder(CLIENT_ID)
        .setRedirectUri(REDIRECT_URI)
        .showAuthView(true)
        .build()

    private val connectRequestChannel = Channel<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val connectorFlow = connectRequestChannel
        .consumeAsFlow()
        .onStart { emit(Unit) }
        .flatMapLatest {

            callbackFlow {

                var spotifyAppRemote: SpotifyAppRemote? = null

                val listener = object : Connector.ConnectionListener {

                    override fun onConnected(sar: SpotifyAppRemote) {
                        Log.d("SpotifyAppRemote", "Connected")
                        trySend(SpotifyConnectorResponse.Connected(sar.toAltifyRepositories()))
                        spotifyAppRemote = sar
                    }

                    override fun onFailure(throwable: Throwable) {
                        Log.d("SpotifyAppRemote", throwable.toString())
                        trySend(SpotifyConnectorResponse.ConnectionFailed(throwable))

                    }

                }

                withContext(Dispatchers.Main) {
                    SpotifyAppRemote.connect(context, connectionParams, listener)
                }

                awaitClose {
                    SpotifyAppRemote.disconnect(spotifyAppRemote)
                }

            }
        }
        .shareIn(
            scope = CoroutineScope(IO),
            started = SharingStarted.Eagerly,
            replay = 1
        )

    override val data: Flow<SpotifyConnectorResponse>
        get() = connectorFlow

    override fun connect() {
        connectRequestChannel.trySend(Unit)
    }
}

private fun SpotifyAppRemote.toAltifyRepositories(): AltifyRepositories =
    AltifyRepositories(
        player = PlayerRepositoryImpl(this.playerApi),
        content = ContentRepositoryImpl(this.contentApi),
        images = ImagesRepositoryImpl(this.imagesApi),
        volume = VolumeRepositoryImpl(this.connectApi),
        user = UserRepositoryImpl(this.userApi)
    )