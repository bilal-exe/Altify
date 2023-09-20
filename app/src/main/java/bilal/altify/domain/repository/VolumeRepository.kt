package bilal.altify.domain.repository

import kotlinx.coroutines.flow.Flow

interface VolumeRepository {

    val volume: Flow<Float>

    fun increaseVolume()

    fun decreaseVolume()

    fun setVolume(volume: Float)

}