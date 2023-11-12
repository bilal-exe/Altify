package bilal.altify.data.spotify.sources

import bilal.altify.domain.model.ExtendedItem
import bilal.altify.domain.model.ExtendedItemList
import bilal.altify.domain.model.RemoteId
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

class AlbumNetworkSource {
}

private interface RetrofitAlbumApi {

    @GET("v1/albums/{id}")
    suspend fun getAlbum(@Path("id") id: String): ExtendedItem.Album

    @GET("v1/albums?ids={ids}")
    suspend fun getAlbums(@Path("ids") ids: List<String>): List<ExtendedItem.Album>

    @GET("v1/me/albums")
    suspend fun getSavedAlbums(): ExtendedItemList<ExtendedItem.Album>

    @PUT("v1/me/albums?ids={ids}")
    suspend fun saveAlbums(ids: List<RemoteId>)

    @DELETE("v1/me/albums?ids={ids}")
    suspend fun unSaveAlbums(ids: List<RemoteId>)

    @GET("v1/me/albums/contains?ids={ids}")
    suspend fun checkAlbumIsSaved(@Path("ids") ids: List<RemoteId>): List<Boolean>

    @GET("v1/browse/new-releases")
    suspend fun getNewReleases(): ExtendedItemList<ExtendedItem.Album>

}