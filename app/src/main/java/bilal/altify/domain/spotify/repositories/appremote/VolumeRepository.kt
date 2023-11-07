package bilal.altify.domain.spotify.repositories.appremote

import kotlinx.coroutines.flow.Flow

interface VolumeRepository {

    val volume: Flow<Float>

    fun increaseVolume()

    fun decreaseVolume()

    fun setVolume(volume: Float)

    class VolumeSourceException(override val message: String?): Exception(message)

}