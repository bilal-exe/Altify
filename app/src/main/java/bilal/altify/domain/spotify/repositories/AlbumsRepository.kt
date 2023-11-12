package bilal.altify.domain.spotify.repositories

import bilal.altify.domain.model.ExtendedItemList
import bilal.altify.domain.model.ExtendedItem.Album
import bilal.altify.domain.model.RemoteId

interface AlbumsRepository {

    suspend fun getAlbum(id: RemoteId): Album

    suspend fun getAlbums(ids: List<RemoteId>): List<Album>

    suspend fun getSavedAlbums(): ExtendedItemList<Album>

    suspend fun saveAlbums(ids: List<RemoteId>)

    suspend fun unSaveAlbums(ids: List<RemoteId>)

    suspend fun checkAlbumIsSaved(ids: List<RemoteId>): List<Boolean>

    suspend fun getNewReleases(): ExtendedItemList<Album>

}