package gj.meteoras.ui.forecast

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import gj.meteoras.data.Forecast
import gj.meteoras.data.Place
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@ExperimentalFoundationApi
@Composable
fun TimestampList(
    place: Place,
    timestamps: List<Forecast.Timestamp>,
    onRefresh: () -> Unit
) {
    val listState = rememberLazyListState()
    val refreshState = rememberSwipeRefreshState(false)
    val grouped = derivedStateOf {
        timestamps.groupBy { it.time.atZone(ZoneId.systemDefault()).toLocalDate() }
    }

    SwipeRefresh(
        state = refreshState,
        onRefresh = onRefresh
    ) {
        LazyColumn(state = listState,) {
            grouped.value.forEach { (date, timestamps) ->
                stickyHeader(key = date) {
                    Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
                        Divider(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp))

                        Text(
                            text = date.format(dateFormatter),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Divider(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp))
                    }
                }

                items(timestamps) { timestamp ->
                    Timestamp(
                        place = place,
                        timestamp = timestamp
                    )
                }
            }
        }
    }

    LaunchedEffect(timestamps) {
        listState.scrollToItem(0)
    }
}

private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
