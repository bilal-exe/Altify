package bilal.altify.domain.spotify.remote.web_api

import kotlinx.coroutines.flow.Flow

interface AccessTokenRepository {

    val state: Flow<TokenState>

    suspend fun setToken(accessToken: TokenState.Token)

}