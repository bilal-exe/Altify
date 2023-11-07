package bilal.altify.domain.spotify.repositories.web_api.access_token

import kotlinx.coroutines.flow.Flow

interface AccessTokenRepository {

    val state: Flow<TokenState>

    suspend fun setToken(accessToken: TokenState.Token)

}