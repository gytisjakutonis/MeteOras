package gj.meteoras.ui.places

import gj.meteoras.data.Place

data class PlacesViewState(
    val places: List<Place> = emptyList(),
    val favourites: List<String> = emptyList(),
    val busy: Boolean = false,
    val filter: String = "",
)
