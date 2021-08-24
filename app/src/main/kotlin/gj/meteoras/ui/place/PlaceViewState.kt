package gj.meteoras.ui.place

import gj.meteoras.data.Forecast
import gj.meteoras.ui.BaseViewState

data class PlaceViewState(
    val forecast: Forecast? = null,
    override val busy: Boolean = false,
) : BaseViewState
