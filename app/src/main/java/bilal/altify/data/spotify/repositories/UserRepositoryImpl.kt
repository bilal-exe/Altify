package bilal.altify.data.spotify.repositories

import bilal.altify.data.mappers.spotifyUriToRemoteId
import bilal.altify.data.mappers.toModel
import bilal.altify.data.mappers.toSpotifyUri
import bilal.altify.domain.model.LibraryState
import bilal.altify.domain.model.RemoteId
import bilal.altify.domain.spotify.repositories.appremote.UserRepository
import com.spotify.android.appremote.api.UserApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserRepositoryImpl(
    private val userApi: UserApi
) : UserRepository {

    private val _currentTrackLibraryState = MutableStateFlow<LibraryState?>(null)
    override val currentTrackLibraryState = _currentTrackLibraryState.asStateFlow()

    override fun updateCurrentTrackState(remoteId: RemoteId) {
        userApi.getLibraryState(remoteId.toSpotifyUri())
            .setResultCallback {
                _currentTrackLibraryState.value = it.toModel()
            }
            .setErrorCallback {
                throw UserRepository.UserSourceException(it.localizedMessage)
            }
    }

    private val _browserLibraryState = MutableStateFlow<Map<RemoteId, LibraryState>>(emptyMap())
    override val browserLibraryState = _browserLibraryState.asStateFlow()

    override fun updateBrowserLibraryState(remoteIds: List<RemoteId>) {

        _browserLibraryState.value = emptyMap()

        remoteIds.forEach { uri ->
            userApi.getLibraryState(uri.toSpotifyUri())
                .setResultCallback { result ->
                    _browserLibraryState.update {
                        it + (result.uri.spotifyUriToRemoteId() to result.toModel())
                    }
                }
                .setErrorCallback {
                    throw UserRepository.UserSourceException(it.localizedMessage)
                }
        }
    }

    override fun toggleLibraryStatus(remoteId: RemoteId, added: Boolean) {
        val addOrRemove = when (added) {
            true -> userApi::addToLibrary
            false -> userApi::removeFromLibrary
        }
        addOrRemove(remoteId.toSpotifyUri())
            .setResultCallback {
                updateStates(remoteId)
            }
            .setErrorCallback {
                throw UserRepository.UserSourceException(it.localizedMessage)
            }
    }

    private fun updateStates(remoteId: RemoteId) {
        when (remoteId) {
            currentTrackLibraryState.value?.remoteId -> {
                updateCurrentTrackState(remoteId = remoteId)
            }
            in browserLibraryState.value.keys -> {
                userApi.getLibraryState(remoteId.toSpotifyUri())
                    .setResultCallback { ls ->
                        _browserLibraryState.update {
                            it + (remoteId to ls.toModel())
                        }
                    }
                    .setErrorCallback {
                        throw UserRepository.UserSourceException(it.localizedMessage)
                    }
            }
        }
    }
}