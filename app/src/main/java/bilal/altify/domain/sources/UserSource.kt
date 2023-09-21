package bilal.altify.domain.sources

import bilal.altify.domain.model.AltLibraryState
import kotlinx.coroutines.flow.Flow

interface UserSource {

    val currentTrackLibraryState: Flow<AltLibraryState?>

    fun updateCurrentTrackState(uri: String)

    val browserLibraryState: Flow<Map<String, AltLibraryState>>

    fun updateBrowserLibraryState(uris: List<String>)

    fun addToLibrary(uri: String)

    fun removeFromLibrary(uri: String)

}