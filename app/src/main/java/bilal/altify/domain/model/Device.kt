package bilal.altify.domain.model

data class Device(
    val id: String,
    val isActive: Boolean,
    val isPrivateSession: Boolean,
    val isRestricted: Boolean,
    val name: String,
    val supportsVolume: Boolean,
    val type: String,
    val volumePercent: Int
)
