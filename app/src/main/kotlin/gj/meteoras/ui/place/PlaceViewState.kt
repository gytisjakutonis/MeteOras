package gj.meteoras.ui.place

import gj.meteoras.data.Place

data class PlaceViewState(
    val place: Place? = null,
    val busy: Boolean = false,
)
