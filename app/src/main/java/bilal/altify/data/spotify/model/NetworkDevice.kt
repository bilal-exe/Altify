package bilal.altify.data.spotify.model

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
)