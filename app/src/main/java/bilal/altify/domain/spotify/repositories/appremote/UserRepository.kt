package bilal.altify.domain.spotify.repositories.appremote

import bilal.altify.domain.model.LibraryState
import bilal.altify.domain.model.RemoteId
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val currentTrackLibraryState: Flow<LibraryState?>

    val browserLibraryState: Flow<Map<RemoteId, LibraryState>>

    fun updateCurrentTrackState(remoteId: RemoteId)

    fun updateBrowserLibraryState(remoteIds: List<RemoteId>)

    fun toggleLibraryStatus(remoteId: RemoteId, added: Boolean)

    class UserSourceException(override val message: String?): Exception(message)

}