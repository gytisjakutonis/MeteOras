package gj.meteoras.ui.forecast

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import gj.meteoras.data.Forecast
import gj.meteoras.data.Place
import gj.meteoras.ui.R
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.roundToInt

@Composable
fun Timestamp(
    place: Place,
    timestamp: Forecast.Timestamp
) {
    val time = derivedStateOf {
        timestamp.time
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
            .format(timeFormatter)
    }
    val temperature = derivedStateOf {
        "${timestamp.airTemperature.roundToInt()}°"
    }
    val wind = derivedStateOf {
        timestamp.windSpeed.toString()
    }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = time.value,
            maxLines = 1,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.secondaryVariant,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            place.coordinates?.let {
                Weather(
                    coordinates = it,
                    timestamp = timestamp,
                )
            }
        }

        Text(
            text = temperature.value,
            maxLines = 1,
            style = MaterialTheme.typography.body1,
            color = when {
                timestamp.airTemperature <= 0.0 -> coldColor
                timestamp.airTemperature > 0.0 -> hotColor
                else -> MaterialTheme.colors.primaryVariant
            },
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = buildAnnotatedString {
                append(wind.value)
                withStyle(
                    style = SpanStyle(
                        fontSize = MaterialTheme.typography.body1.fontSize.div(3),

                    )
                ) {
                    append(stringResource(R.string.timestamp_ms))
                }
            },
            maxLines = 1,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.secondaryVariant,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Outlined.Navigation,
            contentDescription = null,
            tint = MaterialTheme.colors.secondaryVariant,
            modifier = Modifier.rotate(timestamp.windDirection.toFloat() + 180)
        )
    }
}

private val timeFormatter = DateTimeFormatter
    .ofLocalizedTime(FormatStyle.SHORT)
    .withZone(ZoneId.systemDefault())

private val coldColor = Color(0xFF1565c0)
private val hotColor = Color(0xFFB00020)
