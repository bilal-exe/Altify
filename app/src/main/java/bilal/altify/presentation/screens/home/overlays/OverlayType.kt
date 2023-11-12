package bilal.altify.presentation.screens.home.overlays

import bilal.altify.domain.model.Item

sealed interface OverlayType {
    data class Volume(val volume: Float) : OverlayType
    data class Time(val value: Float) : OverlayType
    data class AddToPlaylist(val track: Item.Track) : OverlayType
}