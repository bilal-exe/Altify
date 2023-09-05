package bilal.altify.data.spotify

import android.util.Log
import bilal.altify.data.spotify.mappers.toAlt
import bilal.altify.domain.model.AltLibraryState
import bilal.altify.domain.repository.UserRepository
import com.spotify.android.appremote.api.UserApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserRepositoryImpl(
    private val userApi: UserApi
) : UserRepository {

    private val _currentTrackLibraryState = MutableStateFlow<AltLibraryState?>(null)
    override val currentTrackLibraryState = _currentTrackLibraryState.asStateFlow()

    override fun updateCurrentTrackState(uri: String) {
        userApi.getLibraryState(uri)
            .setResultCallback {
                _currentTrackLibraryState.value = it.toAlt()
            }
            .setErrorCallback {
                Log.d("Error", it.localizedMessage ?: "")
            }
    }

    private val _browserLibraryState = MutableStateFlow<Map<String, AltLibraryState>>(emptyMap())
    override val browserLibraryState = _browserLibraryState.asStateFlow()

    override fun updateBrowserLibraryState(uris: List<String>) {

        _browserLibraryState.value = emptyMap()
        val newLibraryStates = mutableMapOf<String, AltLibraryState>()

        uris.forEach { uri ->
            userApi.getLibraryState(uri)
                .setResultCallback {
                    newLibraryStates[it.uri] = it.toAlt()
                    _browserLibraryState.update { newLibraryStates }
                }
                .setErrorCallback {
                    Log.d("Error", it.localizedMessage ?: "")
                }
        }
    }

    override fun addToLibrary(uri: String) {
        userApi.addToLibrary(uri)
            .setResultCallback {
                updateStates(uri = uri)
            }
            .setErrorCallback {
                Log.d("Error", it.localizedMessage ?: "")
            }
    }

    override fun removeFromLibrary(uri: String) {
        userApi.removeFromLibrary(uri)
            .setResultCallback {
                updateStates(uri = uri)
            }
            .setErrorCallback {
                Log.d("Error", it.localizedMessage ?: "")
            }
    }

    private fun updateStates(uri: String) {
        when (uri) {
            currentTrackLibraryState.value?.uri -> {
                updateCurrentTrackState(uri = uri)
            }
            in browserLibraryState.value.keys -> {
                userApi.getLibraryState(uri)
                    .setResultCallback { ls ->
                        _browserLibraryState.update {
                            val mm = it.toMutableMap()
                            mm[uri] = ls.toAlt()
                            mm.toMap()
                        }
                    }
                    .setErrorCallback {
                        Log.d("Error", it.localizedMessage ?: "")
                    }
            }
        }
    }
}