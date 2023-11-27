package bilal.altify.domain.spotify.repositories

import bilal.altify.domain.model.ExtendedItem
import bilal.altify.domain.model.Item
import bilal.altify.domain.model.RemoteId

interface ArtistsRepository {

    suspend fun getArtist(token: String, id: RemoteId): ExtendedItem.Artist

    suspend fun getArtists(token: String, ids: List<RemoteId>): List<ExtendedItem.Artist>

    suspend fun getTopTracksForArtist(token: String, id: RemoteId, market: String): List<Item.Track>

    suspend fun getRelatedArtists(token: String, id: RemoteId): List<ExtendedItem.Artist>

    suspend fun getAlbumsForArtist(token: String, id: RemoteId, market: String): List<ExtendedItem.Album>

}