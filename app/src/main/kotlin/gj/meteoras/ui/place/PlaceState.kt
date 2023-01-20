package gj.meteoras.ui.place

import gj.meteoras.data.Place

data class PlaceState(
    val places: List<Place> = emptyList(),
    val favourites: List<String> = emptyList(),
    val busy: Boolean = false,
    val filter: String = ""
) {
    val favouritePlaces: List<Place>
        get() = if (filter.isEmpty()) {
            favourites.mapNotNull { code ->
                places.firstOrNull { it.code == code }
            }
        } else {
            emptyList()
        }
}
