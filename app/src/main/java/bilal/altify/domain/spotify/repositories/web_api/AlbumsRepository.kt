package bilal.altify.domain.spotify.repositories.web_api

import bilal.altify.domain.model.ExtendedItemList
import bilal.altify.domain.model.ExtendedItem.Album

interface AlbumsRepository {

    suspend fun getAlbum(id: String): Album

    suspend fun getAlbums(ids: List<String>): List<Album>

    suspend fun getSavedAlbums(): ExtendedItemList<Album>

    suspend fun saveAlbums(ids: List<String>)

    suspend fun unSaveAlbums(ids: List<String>)

    suspend fun checkAlbumIsSaved(ids: List<String>): List<Boolean>

    suspend fun getNewReleases(): ExtendedItemList<Album>

}