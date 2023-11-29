package bilal.altify.data.spotify.sources

import androidx.annotation.IntRange
import bilal.altify.data.spotify.model.NetworkDevice
import bilal.altify.data.spotify.model.NetworkQueue
import bilal.altify.data.spotify.model.NetworkTracks
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Query
import javax.inject.Inject

class PlayerNetworkSource @Inject constructor(
    private val retrofit: Retrofit
) {

    private val networkApi by lazy {
        retrofit.create(RetrofitPlayerApi::class.java)
    }

    suspend fun getAvailableDevices(
        token: String,
    ) = networkApi.getAvailableDevices(token)

    suspend fun transferPlaybackToDevice(
        token: String,
        deviceId: List<String>,
        play: Boolean,
    ) = networkApi.transferPlaybackToDevice(token, deviceId, play)

    suspend fun setVolume(
        token: String,
        deviceId: String,
        @IntRange(0, 100) volumePercent: Int
    ) = networkApi.setVolume(token, volumePercent)

    suspend fun getRecentlyPlayedTracks(
        token: String,
    ) = networkApi.getRecentlyPlayedTracks(token)

    suspend fun getQueue(
        token: String,
    ) = networkApi.getQueue(token)

}

private interface RetrofitPlayerApi {

    @GET("v1/me/player/devices")
    suspend fun getAvailableDevices(
        @Header("Authorization") token: String,
    ): NetworkDevice

    @PUT("v1/me/player")
    suspend fun transferPlaybackToDevice(
        @Header("Authorization") token: String,
        @Query("device_id") deviceId: List<String>,
        @Query("play") play: Boolean,
    )

    @PUT("v1/me/player/volume")
    suspend fun setVolume(
        @Header("Authorization") token: String,
        @Query("volume_percent") @IntRange(0, 100) volumePercent: Int
    )

    @GET("v1/me/player/recently-played")
    suspend fun getRecentlyPlayedTracks(
        @Header("Authorization") token: String,
    ): NetworkTracks

    @GET("/v1/me/player/queue")
    suspend fun getQueue(
        @Header("Authorization") token: String,
    ): NetworkQueue
}