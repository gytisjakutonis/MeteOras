package gj.meteoras.ui.place.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import gj.meteoras.data.Forecast
import java.time.ZoneId

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
                Day(date)
            }

            items(timestamps) { timestamp ->
                Timestamp(timestamp)
            }
        }
    }

    LaunchedEffect(items) {
        listState.scrollToItem(0)
    }
}
