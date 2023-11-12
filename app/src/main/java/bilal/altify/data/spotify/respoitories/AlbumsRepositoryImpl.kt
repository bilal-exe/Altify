package bilal.altify.data.spotify.respoitories

import bilal.altify.domain.model.ExtendedItem
import bilal.altify.domain.model.ExtendedItemList
import bilal.altify.domain.model.RemoteId
import bilal.altify.domain.spotify.repositories.AlbumsRepository
import retrofit2.Retrofit
import javax.inject.Inject

class AlbumsRepositoryImpl: AlbumsRepository {

    override suspend fun getAlbum(id: RemoteId): ExtendedItem.Album {
        TODO("Not yet implemented")
    }

    override suspend fun getAlbums(ids: List<RemoteId>): List<ExtendedItem.Album> {
        TODO("Not yet implemented")
    }

    override suspend fun getSavedAlbums(): ExtendedItemList<ExtendedItem.Album> {
        TODO("Not yet implemented")
    }

    override suspend fun saveAlbums(ids: List<RemoteId>) {
        TODO("Not yet implemented")
    }

    override suspend fun unSaveAlbums(ids: List<RemoteId>) {
        TODO("Not yet implemented")
    }

    override suspend fun checkAlbumIsSaved(ids: List<RemoteId>): List<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getNewReleases(): ExtendedItemList<ExtendedItem.Album> {
        TODO("Not yet implemented")
    }
}