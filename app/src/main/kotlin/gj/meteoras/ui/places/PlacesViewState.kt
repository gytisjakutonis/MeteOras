package gj.meteoras.ui.places

import gj.meteoras.data.Place

data class PlacesViewState(
    val places: List<Place> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
) {
    val retry: Boolean = error != null
}
