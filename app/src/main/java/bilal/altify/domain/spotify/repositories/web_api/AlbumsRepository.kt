package bilal.altify.domain.spotify.repositories.web_api

import bilal.altify.domain.model.Album
import bilal.altify.domain.model.MediaItemsList

interface AlbumsRepository {

    suspend fun getAlbum(id: String): Album

    suspend fun getAlbums(ids: List<String>): List<Album>

    suspend fun getSavedAlbums(): MediaItemsList<Album>

    suspend fun saveAlbums(ids: List<String>)

    suspend fun unSaveAlbums(ids: List<String>)

    suspend fun checkAlbumIsSaved(ids: List<String>): List<Boolean>

    suspend fun getNewReleases(): MediaItemsList<Album>

}