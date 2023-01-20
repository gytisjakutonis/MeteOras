package gj.meteoras.ui.forecast

import gj.meteoras.data.Forecast
import gj.meteoras.data.Place

data class ForecastState(
    val forecast: Forecast? = null,
    val busy: Boolean = false,
) {
    val place: Place?
        get() = forecast?.place
    val timestamps: List<Forecast.Timestamp>
        get() = forecast?.timestamps ?: emptyList()
}
