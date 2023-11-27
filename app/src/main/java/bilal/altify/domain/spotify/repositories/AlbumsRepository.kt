package bilal.altify.domain.spotify.repositories

import bilal.altify.domain.model.ExtendedItem.Album
import bilal.altify.domain.model.RemoteId

interface AlbumsRepository {

    suspend fun getAlbum(token: String, id: RemoteId): Album

    suspend fun getAlbums(token: String, ids: List<RemoteId>): List<Album>

    suspend fun getSavedAlbums(token: String): List<Album>

    suspend fun saveAlbums(token: String, ids: List<RemoteId>)

    suspend fun unSaveAlbums(token: String, ids: List<RemoteId>)

    suspend fun checkAlbumsAreSaved(token: String, ids: List<RemoteId>): List<Boolean>

    suspend fun getNewReleases(token: String): List<Album>

}