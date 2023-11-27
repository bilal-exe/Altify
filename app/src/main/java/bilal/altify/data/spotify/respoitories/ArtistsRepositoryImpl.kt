package bilal.altify.data.spotify.respoitories

import bilal.altify.data.spotify.sources.ArtistNetworkSource
import bilal.altify.domain.model.ExtendedItem
import bilal.altify.domain.model.RemoteId
import bilal.altify.domain.model.toIdsList
import bilal.altify.domain.spotify.repositories.ArtistsRepository
import javax.inject.Inject

class ArtistsRepositoryImpl @Inject constructor(
    private val source: ArtistNetworkSource
) : ArtistsRepository {

    override suspend fun getArtist(token: String, id: RemoteId): ExtendedItem.Artist =
        source.getArtist(token = token, id = id.id).toExtendedArtist()

    override suspend fun getArtists(token: String, ids: List<RemoteId>): List<ExtendedItem.Artist> =
        source.getArtists(token = token, ids = ids.toIdsList()).map { it.toExtendedArtist() }

    override suspend fun getTopTracksForArtist(token: String, id: RemoteId, market: String): List<ExtendedItem.Track> =
        source.getTopTracksForArtist(token = token, id = id.id, market = market).map { it.toExtendedTrack() }

    override suspend fun getRelatedArtists(token: String, id: RemoteId): List<ExtendedItem.Artist> =
        source.getRelatedArtists(token = token, id = id.id).map { it.toExtendedArtist() }

    override suspend fun getAlbumsForArtist(token: String, id: RemoteId, market: String): List<ExtendedItem.Album> =
        source.getAlbumsForArtist(token = token, id = id.id, market = market).map { it.toExtendedAlbum() }
}