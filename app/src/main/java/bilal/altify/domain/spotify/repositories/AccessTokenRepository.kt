package bilal.altify.domain.spotify.repositories

import bilal.altify.domain.model.TokenState
import kotlinx.coroutines.flow.Flow

interface AccessTokenRepository {

    val state: Flow<TokenState>

    suspend fun setToken(accessToken: TokenState.Token)

}