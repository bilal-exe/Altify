package bilal.altify.domain.repository

import kotlinx.coroutines.flow.Flow

interface VolumeRepository {

    fun getVolume(): Flow<Float>

    fun increaseVolume()

    fun decreaseVolume()

    fun setVolume(volume: Float)

}