package gj.meteoras.ui.place.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import gj.meteoras.data.Forecast
import gj.meteoras.ui.theme.paddings
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun TimestampsList(
    items: List<Forecast.Timestamp>
) {
    val listState = rememberLazyListState()
    val grouped = derivedStateOf {
        items.groupBy { it.time.atZone(ZoneId.systemDefault()).toLocalDate() }
    }

    LazyColumn(state = listState,) {
        grouped.value.forEach { (date, timestamps) ->
            item(key = date) {
                Divider(
                    modifier = Modifier.padding(
                        top = MaterialTheme.paddings.small,
                        bottom = MaterialTheme.paddings.small,
                    )
                )

                Text(
                    text = date.format(dateFormatter),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth()
                )

                Divider(
                    modifier = Modifier.padding(
                        top = MaterialTheme.paddings.small,
                        bottom = MaterialTheme.paddings.small,
                    )
                )
            }

            items(timestamps) { timestamp ->
                Text(
                    text = timestamp.time.atZone(ZoneId.systemDefault()).toLocalTime().format(timeFormatter),
                    maxLines = 1,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.fillMaxWidth().padding(bottom = MaterialTheme.paddings.small)
                )
            }
        }
    }

    LaunchedEffect(items) {
        listState.scrollToItem(0)
    }
}

private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
private val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault())
