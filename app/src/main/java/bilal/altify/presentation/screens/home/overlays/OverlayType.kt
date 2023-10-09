package bilal.altify.presentation.screens.home.overlays

sealed interface OverlayType {
    data class Volume(val volume: Float) : OverlayType
    data class Time(val value: Float) : OverlayType
}