package gj.meteoras.ui.place.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import gj.meteoras.data.Forecast
import gj.meteoras.ui.theme.paddings
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun Timestamp(
    timestamp: Forecast.Timestamp
) {
    val timeText = derivedStateOf {
        timestamp.time.atZone(ZoneId.systemDefault()).toLocalTime().format(timeFormatter)
    }

    Text(
        text = timeText.value,
        maxLines = 1,
        style = MaterialTheme.typography.body1,
        modifier = Modifier.fillMaxWidth().padding(bottom = MaterialTheme.paddings.small)
    )
}

private val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault())
