package bilal.altify.domain.model

import java.time.Instant

sealed interface TokenState {

    object Empty : TokenState

    data class Token(
        val accessToken: String,
        val expiry: Instant,
        val region: String
    ) : TokenState
}