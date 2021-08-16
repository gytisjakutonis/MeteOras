package gj.meteoras.ui.place

import gj.meteoras.data.Forecast

data class PlaceViewState(
    val forecast: Forecast? = null,
    val busy: Boolean = false,
)
