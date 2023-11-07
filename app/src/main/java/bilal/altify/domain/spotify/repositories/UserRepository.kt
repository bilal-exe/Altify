package bilal.altify.domain.spotify.repositories

import bilal.altify.domain.spotify.model.LibraryState
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val currentTrackLibraryState: Flow<LibraryState?>

    fun updateCurrentTrackState(uri: String)

    val browserLibraryState: Flow<Map<String, LibraryState>>

    fun updateBrowserLibraryState(uris: List<String>)

    fun toggleLibraryStatus(uri: String, added: Boolean)

    class UserSourceException(override val message: String?): Exception(message)

}