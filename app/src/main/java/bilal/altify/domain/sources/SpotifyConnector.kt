package bilal.altify.domain.sources

import bilal.altify.domain.controller.AltifySources
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

interface SpotifyConnector {

    fun connect(): Flow<SpotifyConnectorResponse>

}

sealed interface SpotifyConnectorResponse {

    data class Connected(val repositories: AltifySources): SpotifyConnectorResponse

    data class ConnectionFailed(val exception: Exception): SpotifyConnectorResponse

}