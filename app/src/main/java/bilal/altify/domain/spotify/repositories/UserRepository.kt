package bilal.altify.domain.spotify.repositories

import bilal.altify.domain.spotify.model.AltLibraryState
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val currentTrackLibraryState: Flow<AltLibraryState?>

    fun updateCurrentTrackState(uri: String)

    val browserLibraryState: Flow<Map<String, AltLibraryState>>

    fun updateBrowserLibraryState(uris: List<String>)

    fun toggleLibraryStatus(uri: String, added: Boolean)

    class UserSourceException(override val message: String?): Exception(message)

}