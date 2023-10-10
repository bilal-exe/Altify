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

}

sealed interface SpotifyConnectorResponse {

    data class Connected(val repositories: AltifyRepositories): SpotifyConnectorResponse

    data class ConnectionFailed(val exception: Exception): SpotifyConnectorResponse

}