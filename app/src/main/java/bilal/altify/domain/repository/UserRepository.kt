package bilal.altify.domain.repository

import bilal.altify.domain.model.AltLibraryState
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val currentTrackLibraryState: Flow<AltLibraryState?>

    fun updateCurrentTrackState(uri: String)

    val browserLibraryState: Flow<Map<String, AltLibraryState>>

    fun updateBrowserLibraryState(uris: List<String>)

    fun addToLibrary(uri: String)

    fun removeFromLibrary(uri: String)

}