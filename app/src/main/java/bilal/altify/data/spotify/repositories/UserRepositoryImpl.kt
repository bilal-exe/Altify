package bilal.altify.data.spotify.repositories

import bilal.altify.data.spotify.mappers.toModel
import bilal.altify.domain.spotify.model.LibraryState
import bilal.altify.domain.spotify.repositories.UserRepository
import com.spotify.android.appremote.api.UserApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserRepositoryImpl(
    private val userApi: UserApi
) : UserRepository {

    private val _currentTrackLibraryState = MutableStateFlow<LibraryState?>(null)
    override val currentTrackLibraryState = _currentTrackLibraryState.asStateFlow()

    override fun updateCurrentTrackState(uri: String) {
        userApi.getLibraryState(uri)
            .setResultCallback {
                _currentTrackLibraryState.value = it.toModel()
            }
            .setErrorCallback {
                throw UserRepository.UserSourceException(it.localizedMessage)
            }
    }

    private val _browserLibraryState = MutableStateFlow<Map<String, LibraryState>>(emptyMap())
    override val browserLibraryState = _browserLibraryState.asStateFlow()

    override fun updateBrowserLibraryState(uris: List<String>) {

        _browserLibraryState.value = emptyMap()
        val newLibraryStates = mutableMapOf<String, LibraryState>()

        uris.forEach { uri ->
            userApi.getLibraryState(uri)
                .setResultCallback {
                    newLibraryStates[it.uri] = it.toModel()
                    _browserLibraryState.update { newLibraryStates }
                }
                .setErrorCallback {
                    throw UserRepository.UserSourceException(it.localizedMessage)
                }
        }
    }

    override fun toggleLibraryStatus(uri: String, added: Boolean) {
        val addOrRemove = when (added) {
            true -> userApi::addToLibrary
            false -> userApi::removeFromLibrary
        }
        addOrRemove(uri)
            .setResultCallback {
                updateStates(uri = uri)
            }
            .setErrorCallback {
                throw UserRepository.UserSourceException(it.localizedMessage)
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
                            mm[uri] = ls.toModel()
                            mm.toMap()
                        }
                    }
                    .setErrorCallback {
                        throw UserRepository.UserSourceException(it.localizedMessage)
                    }
            }
        }
    }
}