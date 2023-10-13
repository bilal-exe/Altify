package bilal.altify.domain.spotify.remote

import bilal.altify.domain.spotify.repositories.AltifyRepositories
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

interface SpotifySource {

    val data: Flow<SpotifyConnectorResponse>

}

interface SpotifyConnector: SpotifySource {

    override val data: Flow<SpotifyConnectorResponse>

    fun connect()

    companion object {
        const val CLIENT_ID = "50109e10614941e596e264af1e7b3685"
        const val REDIRECT_URI = "altify://now_playing"
        const val REQUEST_CODE = 1337
    }

}

sealed interface SpotifyConnectorResponse {

    data class Connected(val repositories: AltifyRepositories): SpotifyConnectorResponse

    data class ConnectionFailed(val exception: Exception): SpotifyConnectorResponse

}