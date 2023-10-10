package bilal.altify.data.spotify.remote

import android.content.Context
import android.util.Log
import bilal.altify.data.spotify.repositories.ContentRepositoryImpl
import bilal.altify.data.spotify.repositories.ImagesRepositoryImpl
import bilal.altify.data.spotify.repositories.PlayerRepositoryImpl
import bilal.altify.data.spotify.repositories.UserRepositoryImpl
import bilal.altify.data.spotify.repositories.VolumeRepositoryImpl
import bilal.altify.domain.spotify.remote.SpotifyConnector
import bilal.altify.domain.spotify.remote.SpotifyConnectorResponse
import bilal.altify.domain.spotify.remote.SpotifySource
import bilal.altify.domain.spotify.repositories.AltifyRepositories
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.error.SpotifyAppRemoteException
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpotifySourceImpl(
    private val context: Context
) : SpotifySource, SpotifyConnector {

    private val connectionParams: ConnectionParams = ConnectionParams
        .Builder(clientId)
        .setRedirectUri(redirectUri)
        .showAuthView(true)
        .build()

    private val connectRequestChannel = Channel<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val connectorFlow = connectRequestChannel
        .consumeAsFlow()
        .onStart {
            emit(Unit)
        }
        .flatMapLatest {

            callbackFlow {

                var spotifyAppRemote: SpotifyAppRemote? = null

                val listener = object : Connector.ConnectionListener {

                    override fun onConnected(sar: SpotifyAppRemote) {
                        Log.d("SpotifyAppRemote", "Connected")
                        launch {
                            send(
                                SpotifyConnectorResponse.Connected(
                                    AltifyRepositories(
                                        player = PlayerRepositoryImpl(sar.playerApi),
                                        content = ContentRepositoryImpl(sar.contentApi),
                                        images = ImagesRepositoryImpl(sar.imagesApi),
                                        volume = VolumeRepositoryImpl(sar.connectApi),
                                        user = UserRepositoryImpl(sar.userApi)
                                    )
                                )
                            )
                        }
                        spotifyAppRemote = sar
                    }

                    override fun onFailure(throwable: Throwable) {
                        Log.d("SpotifyAppRemote", throwable.toString())
                        launch {
                            send(
                                SpotifyConnectorResponse.ConnectionFailed(throwable as SpotifyAppRemoteException)
                            )
                        }
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

    companion object {
        private const val clientId = "50109e10614941e596e264af1e7b3685"
        private const val redirectUri = "altify://now_playing"
    }
}