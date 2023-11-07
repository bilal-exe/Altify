package bilal.altify.data.spotify.remote.web_api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import bilal.altify.domain.spotify.remote.web_api.TokenState
import bilal.altify.domain.spotify.remote.web_api.AccessTokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

class AccessTokenRepositoryImpl(
    private val altPreferences: DataStore<Preferences>
) : AccessTokenRepository {

    private val accessTokenKey = stringPreferencesKey("ACCESS_TOKEN")
    private val expiryDateKey = longPreferencesKey("TOKEN_EXPIRY")

    override val state: Flow<TokenState> = altPreferences.data.map { preferences ->
        TokenState.Token(
            accessToken = preferences[accessTokenKey] ?: return@map TokenState.Empty,
            expiry = Instant.ofEpochSecond(preferences[expiryDateKey]?: return@map TokenState.Empty)
        )
    }

    override suspend fun setToken(accessToken: TokenState.Token) {
        altPreferences.edit {
            it[accessTokenKey] = accessToken.accessToken
            it[expiryDateKey] = accessToken.expiry.epochSecond
        }
    }
}