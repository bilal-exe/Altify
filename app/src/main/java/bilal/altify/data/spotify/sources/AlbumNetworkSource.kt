package bilal.altify.data.spotify.sources

import bilal.altify.data.spotify.model.NetworkAlbum
import bilal.altify.domain.model.RemoteId
import retrofit2.Retrofit
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

class AlbumNetworkSource @Inject constructor(
    retrofit: Retrofit,
) {

    private val networkApi by lazy {
        retrofit.create(RetrofitAlbumApi::class.java)
    }

    suspend fun getAlbum(
        authorization: String,
        market: String,
        id: String
    ) = networkApi.getAlbum(authorization, market, id)

    suspend fun getAlbums(
        authorization: String,
        market: String,
        ids: List<String>
    ): List<NetworkAlbum> =
        networkApi.getAlbums(authorization, market, ids)

    suspend fun getSavedAlbums(
        authorization: String
    ): List<NetworkAlbum> =
        networkApi.getSavedAlbums(authorization)

    suspend fun saveAlbums(
        authorization: String,
        market: String,
        ids: List<RemoteId>
    ) =
        networkApi.saveAlbums(authorization, market, ids)

    suspend fun unSaveAlbums(
        authorization: String,
        market: String,
        ids: List<RemoteId>
    ) =
        networkApi.unSaveAlbums(authorization, market, ids)

    suspend fun checkAlbumIsSaved(
        authorization: String,
        market: String,
        ids: List<RemoteId>
    ): List<Boolean> =
        networkApi.checkAlbumIsSaved(authorization, market, ids)

    @GET("v1/browse/new-releases")
    suspend fun getNewReleases(
        authorization: String,
        market: String,
    ): List<NetworkAlbum> =
        networkApi.getNewReleases(authorization, market)


}

//private fun TokenState.

private interface RetrofitAlbumApi {

    @GET("v1/albums/{id}")
    suspend fun getAlbum(
        @Header("Authorization") authorization: String,
        @Query("market") market: String,
        @Path("id") id: String
    ): NetworkAlbum

    @GET("v1/albums?ids={ids}")
    suspend fun getAlbums(
        @Header("Authorization") authorization: String,
        @Query("market") market: String,
        @Path("ids") ids: List<String>
    ): List<NetworkAlbum>

    @GET("v1/me/albums")
    suspend fun getSavedAlbums(
        @Header("Authorization") authorization: String
    ): List<NetworkAlbum>

    @PUT("v1/me/albums?ids={ids}")
    suspend fun saveAlbums(
        @Header("Authorization") authorization: String,
        @Query("market") market: String,
        @Path("ids") ids: List<RemoteId>
    )

    @DELETE("v1/me/albums?ids={ids}")
    suspend fun unSaveAlbums(
        @Header("Authorization") authorization: String,
        @Query("market") market: String,
        @Path("ids") ids: List<RemoteId>
    )

    @GET("v1/me/albums/contains?ids={ids}")
    suspend fun checkAlbumIsSaved(
        @Header("Authorization") authorization: String,
        @Query("market") market: String,
        @Path("ids") ids: List<RemoteId>
    ): List<Boolean>

    @GET("v1/browse/new-releases")
    suspend fun getNewReleases(
        @Header("Authorization") authorization: String,
        @Query("market") market: String,
    ): List<NetworkAlbum>

}