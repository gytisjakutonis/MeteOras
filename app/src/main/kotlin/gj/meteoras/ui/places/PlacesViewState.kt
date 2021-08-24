package gj.meteoras.ui.places

import gj.meteoras.data.Place
import gj.meteoras.ui.BaseViewState

data class PlacesViewState(
    val places: List<Place> = emptyList(),
    val favourites: List<String> = emptyList(),
    override val busy: Boolean = false,
    val filter: String = "",
) : BaseViewState
