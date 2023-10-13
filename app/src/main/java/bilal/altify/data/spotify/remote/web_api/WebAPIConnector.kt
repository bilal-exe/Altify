package bilal.altify.data.spotify.remote.web_api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

class WebAPIConnector(
    private val altPreferences: DataStore<Preferences>
) {

    private val accessTokenKey = stringPreferencesKey("ACCESS_TOKEN")
    private val expiryDateKey = longPreferencesKey("TOKEN_EXPIRY")



}