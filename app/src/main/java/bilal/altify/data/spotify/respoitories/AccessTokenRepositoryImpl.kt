package bilal.altify.data.spotify.respoitories

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import bilal.altify.domain.model.TokenState
import bilal.altify.domain.spotify.repositories.AccessTokenRepository
import bilal.altify.util.getISO3166code
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.time.Instant

class AccessTokenRepositoryImpl(
    private val altPreferences: DataStore<Preferences>
) : AccessTokenRepository {

    private val accessTokenKey = stringPreferencesKey("ACCESS_TOKEN")
    private val expiryDateKey = longPreferencesKey("TOKEN_EXPIRY")

    override val state: Flow<TokenState> = altPreferences.data.map { preferences ->
        TokenState.Token(
            accessToken = preferences[accessTokenKey] ?: return@map TokenState.Empty,
            expiry = Instant.ofEpochSecond(preferences[expiryDateKey]?: return@map TokenState.Empty),
            region = getISO3166code()
        )
    }
        .onEach { if (it is TokenState.Token) Log.d("Token", it.accessToken) }

    override suspend fun setToken(accessToken: TokenState.Token) {
        altPreferences.edit {
            it[accessTokenKey] = accessToken.accessToken
            it[expiryDateKey] = accessToken.expiry.epochSecond
        }
    }
}