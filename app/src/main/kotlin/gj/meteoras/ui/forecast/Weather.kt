package gj.meteoras.ui.forecast

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import gj.meteoras.R
import gj.meteoras.data.Forecast
import gj.meteoras.data.Place
import org.shredzone.commons.suncalc.SunTimes
import java.time.ZoneId

@Composable
fun Weather(
    coordinates: Place.Coordinates,
    timestamp: Forecast.Timestamp,
) {
    val sun = derivedStateOf {
        SunTimes.compute()
            .on(timestamp.time.atZone(utcZoneId).toLocalDate())
            .at(coordinates.latitude, coordinates.longitude)
            .execute()
            .let { times ->
                times.isAlwaysUp
                        || times.rise?.toInstant()?.isBefore(timestamp.time) == true
                        && times.set?.toInstant()?.isAfter(timestamp.time) == true
            }
    }

    val image = derivedStateOf {
        when (timestamp.condition) {
            Forecast.Timestamp.Condition.Clear ->
                if (sun.value) R.drawable.ic_day else R.drawable.ic_night
            Forecast.Timestamp.Condition.Cloudy,
            Forecast.Timestamp.Condition.PartlyCloudy,
            Forecast.Timestamp.Condition.CloudyWithSunnyIntervals ->
                if (sun.value) R.drawable.ic_clouds_day else R.drawable.ic_clouds_night
            Forecast.Timestamp.Condition.LightRain -> R.drawable.ic_light_rain
            Forecast.Timestamp.Condition.Rain,
            Forecast.Timestamp.Condition.FreezingRain,
            Forecast.Timestamp.Condition.ModerateRain ->
                R.drawable.ic_moderate_rain
            Forecast.Timestamp.Condition.HeavyRain -> R.drawable.ic_heavy_rain
            Forecast.Timestamp.Condition.LightSnow -> R.drawable.ic_light_snow
            Forecast.Timestamp.Condition.Snow -> R.drawable.ic_moderate_snow
            Forecast.Timestamp.Condition.HeavySnow -> R.drawable.ic_heavy_snow
            Forecast.Timestamp.Condition.LightSleet -> R.drawable.ic_light_sleet
            Forecast.Timestamp.Condition.Sleet -> R.drawable.ic_sleet
            Forecast.Timestamp.Condition.Fog -> R.drawable.ic_fog
            Forecast.Timestamp.Condition.Hail -> R.drawable.ic_hail
            Forecast.Timestamp.Condition.Thunder -> R.drawable.ic_thunder
            Forecast.Timestamp.Condition.HeavyRainWithThunderstorms,
            Forecast.Timestamp.Condition.IsolatedThunderstorms,
            Forecast.Timestamp.Condition.Thunderstorms ->
                R.drawable.ic_thunderstorm
            else -> R.drawable.ic_na
        }
    }

    Icon(
        painter = painterResource(image.value),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.size(30.dp)
    )
}

private val utcZoneId = ZoneId.of("UTC")
