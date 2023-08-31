package bilal.altify.domain.repository

import bilal.altify.domain.controller.AltifyRepositories
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

interface SpotifyConnector {

    fun connect(): Flow<SpotifyConnectorResponse>

}

sealed interface SpotifyConnectorResponse {

    data class Connected(val repositories: AltifyRepositories): SpotifyConnectorResponse

    data class ConnectionFailed(val exception: Exception): SpotifyConnectorResponse

}