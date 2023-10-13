package bilal.altify.domain.spotify.remote.web_api

import java.time.Instant

sealed interface TokenState {

    object Empty : TokenState

    data class Token(
        val accessToken: String,
        val expiry: Instant
    ) : TokenState
}
