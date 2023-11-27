package bilal.altify.data.spotify.sources

import bilal.altify.data.spotify.model.ExtendedNetworkTrack
import retrofit2.Retrofit
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

class TrackNetworkSource @Inject constructor(
    retrofit: Retrofit
) {

    private val networkApi by lazy {
        retrofit.create(RetrofitTracksApi::class.java)
    }

    suspend fun getTrack(
        authorization: String,
        market: String,
        id: String
    ) = networkApi.getTrack(authorization, market, id)

    suspend fun getTracks(
        authorization: String,
        market: String,
        ids: List<String>
    ) = networkApi.getTracks(authorization, market, ids)

    suspend fun getSavedTracks(
        authorization: String,
        market: String,
    ) = networkApi.getSavedTracks(authorization, market)

    suspend fun saveTrack(
        authorization: String,
        id: List<String>
    ) = networkApi.saveTrack(authorization, id)

    suspend fun unSaveTrack(
        authorization: String,
        id: List<String>
    ) = networkApi.unSaveTrack(authorization, id)

    suspend fun checkTracksAreSaved(
        authorization: String,
        id: List<String>
    ) = networkApi.checkTracksAreSaved(authorization, id)

}

private interface RetrofitTracksApi {

    @GET("/v1/tracks/{id}")
    suspend fun getTrack(
        @Header("Authorization") authorization: String,
        @Query("market") market: String,
        @Path("id") id: String
    ): ExtendedNetworkTrack

    @GET("/v1/tracks")
    suspend fun getTracks(
        @Header("Authorization") authorization: String,
        @Query("market") market: String,
        @Query("ids") id: List<String>
    ): List<ExtendedNetworkTrack>

    @GET("v1/me/tracks")
    suspend fun getSavedTracks(
        @Header("Authorization") authorization: String,
        @Query("market") market: String,
    ): List<ExtendedNetworkTrack>

    @PUT("v1/me/tracks")
    suspend fun saveTrack(
        @Header("Authorization") authorization: String,
        @Query("ids") id: List<String>
    )

    @DELETE("v1/me/tracks")
    suspend fun unSaveTrack(
        @Header("Authorization") authorization: String,
        @Query("ids") id: List<String>
    )

    @GET("v1/me/tracks/contains")
    suspend fun checkTracksAreSaved(
        @Header("Authorization") authorization: String,
        @Query("ids") id: List<String>
    ): List<Boolean>
}