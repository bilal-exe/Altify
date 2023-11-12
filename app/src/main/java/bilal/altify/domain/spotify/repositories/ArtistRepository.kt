package bilal.altify.domain.spotify.repositories

import bilal.altify.domain.model.ExtendedItem
import bilal.altify.domain.model.RemoteId

interface ArtistRepository {

    suspend fun getArtist(id: RemoteId): ExtendedItem.Artist

    suspend fun getArtists(ids: List<RemoteId>): ExtendedItem.Artist

    suspend fun getArtistsAlbums(id: RemoteId): List<ExtendedItem.Album>

    suspend fun getArtistsTopTracks(id: RemoteId): List<ExtendedItem.Album>

    suspend fun getRelatedArtists(id: RemoteId): List<ExtendedItem.Album>

}