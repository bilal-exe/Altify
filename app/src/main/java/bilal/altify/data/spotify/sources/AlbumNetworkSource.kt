package bilal.altify.data.spotify.sources

import bilal.altify.data.spotify.model.NetworkAlbum
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
        ids: List<String>
    ) =
        networkApi.saveAlbums(authorization, market, ids)

    suspend fun unSaveAlbums(
        authorization: String,
        market: String,
        ids: List<String>
    ) =
        networkApi.unSaveAlbums(authorization, market, ids)

    suspend fun checkAlbumIsSaved(
        authorization: String,
        market: String,
        ids: List<String>
    ): List<Boolean> =
        networkApi.checkAlbumIsSaved(authorization, market, ids)

    suspend fun getNewReleases(
        authorization: String,
        market: String,
    ): List<NetworkAlbum> =
        networkApi.getNewReleases(authorization, market)


}

private interface RetrofitAlbumApi {

    @GET("v1/albums/{id}")
    suspend fun getAlbum(
        @Header("Authorization") authorization: String,
        @Query("market") market: String,
        @Path("id") id: String
    ): NetworkAlbum

    @GET("v1/albums")
    suspend fun getAlbums(
        @Header("Authorization") authorization: String,
        @Query("market") market: String,
        @Query("ids") ids: List<String>
    ): List<NetworkAlbum>

    @GET("v1/me/albums")
    suspend fun getSavedAlbums(
        @Header("Authorization") authorization: String
    ): List<NetworkAlbum>

    @PUT("v1/me/albums")
    suspend fun saveAlbums(
        @Header("Authorization") authorization: String,
        @Query("market") market: String,
        @Query("ids") ids: List<String>
    )

    @DELETE("v1/me/albums")
    suspend fun unSaveAlbums(
        @Header("Authorization") authorization: String,
        @Query("market") market: String,
        @Query("ids") ids: List<String>
    )

    @GET("v1/me/albums/contains")
    suspend fun checkAlbumIsSaved(
        @Header("Authorization") authorization: String,
        @Query("market") market: String,
        @Query("ids") ids: List<String>
    ): List<Boolean>

    @GET("v1/browse/new-releases")
    suspend fun getNewReleases(
        @Header("Authorization") authorization: String,
        @Query("market") market: String,
    ): List<NetworkAlbum>

}