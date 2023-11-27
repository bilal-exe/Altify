package bilal.altify.data.spotify.sources

import bilal.altify.data.spotify.model.ExtendedNetworkArtist
import bilal.altify.data.spotify.model.ExtendedNetworkTrack
import bilal.altify.data.spotify.model.NetworkAlbum
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

class ArtistNetworkSource @Inject constructor(
    retrofit: Retrofit,
) {

    private val networkApi by lazy {
        retrofit.create(RetrofitArtistApi::class.java)
    }

    suspend fun getArtist(token: String, id: String) =
        networkApi.getArtist(token, id)

    suspend fun getArtists(token: String, ids: List<String>) =
        networkApi.getArtists(token, ids)

    suspend fun getAlbumsForArtist(token: String, market: String, id: String) =
        networkApi.getAlbumsForArtist(token, id, market)

    suspend fun getTopTracksForArtist(token: String, id: String, market: String) =
        networkApi.getTopTracksForArtist(token, id, market)

    suspend fun getRelatedArtists(token: String, id: String) =
        networkApi.getRelatedArtists(token, id)
}

private interface RetrofitArtistApi {

    @GET("v1/artists/{id}")
    suspend fun getArtist(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): ExtendedNetworkArtist

    @GET("v1/artists")
    suspend fun getArtists(
        @Header("Authorization") token: String,
        @Query("ids") ids: List<String>
    ): List<ExtendedNetworkArtist>

    @GET("v1/artists/{id}/top-tracks")
    suspend fun getTopTracksForArtist(
        @Header("Authorization") token: String,
        @Path("id") artistId: String,
        @Query("market") market: String
    ): List<ExtendedNetworkTrack>

    @GET("v1/artists/{id}/related-artists")
    suspend fun getRelatedArtists(
        @Header("Authorization") token: String, @Path("id") artistId: String
    ): List<ExtendedNetworkArtist>

    @GET("v1/artists/{id}/albums")
    suspend fun getAlbumsForArtist(
        @Header("Authorization") token: String,
        @Path("id") artistId: String,
        @Query("market") market: String
    ): List<NetworkAlbum>

}