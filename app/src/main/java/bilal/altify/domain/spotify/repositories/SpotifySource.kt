package bilal.altify.domain.spotify.repositories

import kotlinx.coroutines.flow.Flow
import java.lang.Exception

interface SpotifySource {

    val data: Flow<SpotifyConnectorResponse>

}

interface SpotifyConnector {

    val data: Flow<SpotifyConnectorResponse>

    fun connect()

}

sealed interface SpotifyConnectorResponse {

    data class Connected(val repositories: AltifyRepositories): SpotifyConnectorResponse

    data class ConnectionFailed(val exception: Exception): SpotifyConnectorResponse

}