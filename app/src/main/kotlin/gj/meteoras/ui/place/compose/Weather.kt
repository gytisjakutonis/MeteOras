package gj.meteoras.ui.place.compose

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import gj.meteoras.R
import gj.meteoras.data.Forecast

@Composable
fun Weather(
    condition: Forecast.Timestamp.Condition,
) {
    val image = derivedStateOf {
        when (condition) {
            Forecast.Timestamp.Condition.Clear -> R.drawable.ic_day
            Forecast.Timestamp.Condition.IsolatedClound,
            Forecast.Timestamp.Condition.ScatteredClouds -> R.drawable.ic_clouds_day
            Forecast.Timestamp.Condition.Overcast -> R.drawable.ic_clouds
            Forecast.Timestamp.Condition.LightRain -> R.drawable.ic_light_rain
            Forecast.Timestamp.Condition.ModerateRain -> R.drawable.ic_moderate_rain
            Forecast.Timestamp.Condition.HeavyRain -> R.drawable.ic_heavy_rain
            Forecast.Timestamp.Condition.Sleet -> R.drawable.ic_sleet
            Forecast.Timestamp.Condition.LightSnow -> R.drawable.ic_light_snow
            Forecast.Timestamp.Condition.ModerateSnow -> R.drawable.ic_moderate_snow
            Forecast.Timestamp.Condition.HeavySnow -> R.drawable.ic_heavy_snow
            Forecast.Timestamp.Condition.Fog -> R.drawable.ic_fog
            Forecast.Timestamp.Condition.NA -> R.drawable.ic_na
        }
    }

    Icon(
        painter = painterResource(image.value),
        contentDescription = null,
        tint = MaterialTheme.colors.primaryVariant,
        modifier = Modifier.size(24.dp)
    )
}
