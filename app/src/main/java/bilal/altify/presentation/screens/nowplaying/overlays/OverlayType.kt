package bilal.altify.presentation.screens.nowplaying.overlays

sealed interface OverlayType {
    data class Volume(val volume: Float) : OverlayType
    data class Time(val value: Float) : OverlayType
}