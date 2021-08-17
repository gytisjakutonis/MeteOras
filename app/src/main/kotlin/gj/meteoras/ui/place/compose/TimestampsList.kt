package gj.meteoras.ui.place.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import gj.meteoras.data.Forecast
import java.time.ZoneId

@ExperimentalFoundationApi
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
            stickyHeader(key = date) {
                Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
                    Day(date)
                }
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
