package bilal.altify.data.spotify.model

import bilal.altify.domain.model.Device
import kotlinx.serialization.Serializable

@Serializable
data class NetworkDevice(
    val id: String,
    val is_active: Boolean,
    val is_private_session: Boolean,
    val is_restricted: Boolean,
    val name: String,
    val supports_volume: Boolean,
    val type: String,
    val volume_percent: Int
) {

    fun toDevice() =
        Device(
            id = id,
            isActive = is_active,
            isPrivateSession = is_private_session,
            isRestricted = is_restricted,
            name = name,
            supportsVolume = supports_volume,
            type = type,
            volumePercent = volume_percent,
        )

}

