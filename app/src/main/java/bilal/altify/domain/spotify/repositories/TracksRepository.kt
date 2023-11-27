package bilal.altify.domain.spotify.repositories

import bilal.altify.domain.model.AudioFeatures
import bilal.altify.domain.model.ExtendedItem
import bilal.altify.domain.model.RemoteId

interface TracksRepository {

    suspend fun getTrack(token: String, id: RemoteId): ExtendedItem.Track

    suspend fun getTracks(token: String, ids: List<RemoteId>): List<ExtendedItem.Track>

    suspend fun getSavedTracks(token: String): List<ExtendedItem.Track>

    suspend fun saveTrack(token: String, remoteIds: List<RemoteId>)

    suspend fun unSaveTrack(token: String, remoteIds: List<RemoteId>)

    suspend fun checkTracksAreSaved(token: String, remoteIds: List<RemoteId>): List<Boolean>

    suspend fun getAudioFeatures(token: String, remoteId: RemoteId): AudioFeatures

    /* TODO:
    * add remaining endpoints
    * */

}