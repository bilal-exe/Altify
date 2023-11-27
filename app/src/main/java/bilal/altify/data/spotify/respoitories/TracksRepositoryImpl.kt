package bilal.altify.data.spotify.respoitories

import bilal.altify.data.spotify.sources.TrackNetworkSource
import bilal.altify.domain.model.ExtendedItem
import bilal.altify.domain.model.RemoteId
import bilal.altify.domain.model.toIdsList
import bilal.altify.domain.spotify.repositories.TracksRepository
import bilal.altify.util.getISO3166code
import javax.inject.Inject

class TracksRepositoryImpl @Inject constructor(
    private val source: TrackNetworkSource
) : TracksRepository {

    override suspend fun getTrack(token: String, id: RemoteId): ExtendedItem.Track =
        source.getTrack(authorization = token, market = getISO3166code(), id = id.id).toExtendedTrack()

    override suspend fun getTracks(token: String, ids: List<RemoteId>): List<ExtendedItem.Track> =
        source.getTracks(authorization = token, market = getISO3166code(), ids = ids.toIdsList()).map { it.toExtendedTrack() }

    override suspend fun getSavedTracks(token: String): List<ExtendedItem.Track> =
        source.getSavedTracks(authorization = token, market = getISO3166code()).map { it.toExtendedTrack() }

    override suspend fun saveTrack(token: String, remoteIds: List<RemoteId>) =
        source.saveTrack(token, remoteIds.toIdsList())

    override suspend fun unSaveTrack(token: String, remoteIds: List<RemoteId>) =
        source.unSaveTrack(token, remoteIds.toIdsList())

    override suspend fun checkTracksAreSaved(token: String, remoteIds: List<RemoteId>) =
        source.checkTracksAreSaved(token, remoteIds.toIdsList())
}