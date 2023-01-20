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
//            Forecast.Timestamp.Condition.Clear ->
//                if (sun.value) R.drawable.ic_day else R.drawable.ic_night
//            Forecast.Timestamp.Condition.IsolatedClound,
//            Forecast.Timestamp.Condition.ScatteredClouds ->
//                if (sun.value) R.drawable.ic_clouds_day else R.drawable.ic_clouds_night
//            Forecast.Timestamp.Condition.Overcast -> R.drawable.ic_clouds
//            Forecast.Timestamp.Condition.LightRain -> R.drawable.ic_light_rain
//            Forecast.Timestamp.Condition.ModerateRain -> R.drawable.ic_moderate_rain
//            Forecast.Timestamp.Condition.HeavyRain -> R.drawable.ic_heavy_rain
//            Forecast.Timestamp.Condition.Sleet -> R.drawable.ic_sleet
//            Forecast.Timestamp.Condition.LightSnow -> R.drawable.ic_light_snow
//            Forecast.Timestamp.Condition.ModerateSnow -> R.drawable.ic_moderate_snow
//            Forecast.Timestamp.Condition.HeavySnow -> R.drawable.ic_heavy_snow
//            Forecast.Timestamp.Condition.Fog -> R.drawable.ic_fog
            else -> R.drawable.ic_na
        }
    }

    Icon(
        painter = painterResource(image.value),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(24.dp)
    )
}

private val utcZoneId = ZoneId.of("UTC")
