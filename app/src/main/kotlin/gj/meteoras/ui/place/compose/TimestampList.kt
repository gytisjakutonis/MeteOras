package gj.meteoras.ui.place.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import gj.meteoras.data.Forecast
import java.time.ZoneId

@Composable
fun PlacesList(
    items: List<Forecast.Timestamp>
) {
    val listState = rememberLazyListState()
    val grouped = derivedStateOf {
        items.groupBy { it.time.atZone(ZoneId.systemDefault()) }
    }

    LazyColumn(state = listState,) {
        grouped.value.forEach { zonedTime, timestamps ->
            item(key = zonedTime) {
                Text(text = zonedTime.toString())
                Divider()
            }
        }
    }

    LaunchedEffect(items) {
        listState.scrollToItem(0)
    }
}
