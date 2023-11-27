package bilal.altify.data.spotify.respoitories

import bilal.altify.data.spotify.sources.AlbumNetworkSource
import bilal.altify.domain.model.ExtendedItem
import bilal.altify.domain.model.RemoteId
import bilal.altify.domain.model.toIdsList
import bilal.altify.domain.spotify.repositories.AlbumsRepository
import bilal.altify.util.getISO3166code
import javax.inject.Inject

class AlbumsRepositoryImpl @Inject constructor(
    private val source: AlbumNetworkSource,
) : AlbumsRepository {

    override suspend fun getAlbum(token: String, id: RemoteId): ExtendedItem.Album =
        source.getAlbum(authorization = token, market = getISO3166code(), id = id.id).toExtendedAlbum()

    override suspend fun getAlbums(token: String, ids: List<RemoteId>): List<ExtendedItem.Album> =
        source.getAlbums(authorization = token, market = getISO3166code(), ids = ids.toIdsList()).map { it.toExtendedAlbum() }

    override suspend fun getSavedAlbums(token: String): List<ExtendedItem.Album> =
        source.getSavedAlbums(token).map { it.toExtendedAlbum() }

    override suspend fun saveAlbums(token: String, ids: List<RemoteId>) =
        source.saveAlbums(authorization = token, market = getISO3166code(), ids = ids.toIdsList())

    override suspend fun unSaveAlbums(token: String, ids: List<RemoteId>) =
        source.unSaveAlbums(authorization = token, market = getISO3166code(), ids = ids.toIdsList())

    override suspend fun checkAlbumsAreSaved(token: String, ids: List<RemoteId>): List<Boolean> =
        source.checkAlbumIsSaved(authorization = token, market = getISO3166code(), ids = ids.toIdsList())

    override suspend fun getNewReleases(token: String): List<ExtendedItem.Album> =
        source.getNewReleases(token, getISO3166code()).map { it.toExtendedAlbum() }
}