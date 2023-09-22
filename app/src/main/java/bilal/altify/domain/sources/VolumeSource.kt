package bilal.altify.domain.sources

import kotlinx.coroutines.flow.Flow

interface VolumeSource {

    val volume: Flow<Float>

    fun increaseVolume()

    fun decreaseVolume()

    fun setVolume(volume: Float)

    class VolumeSourceException(override val message: String?): Exception(message)

}