package gj.meteoras.ui.places.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import gj.meteoras.data.Place
import gj.meteoras.ui.compose.ItemDivider

@Composable
fun PlacesList(items: List<Place>) {
    val listState = rememberLazyListState()

    LazyColumn(state = listState,) {
        items(
            items = items,
            key = { place -> place.code }
        ) { place ->
            Text(
                "${place.name} (${place.administrativeDivision ?: ""}) ${place.countryCode}",
                style = MaterialTheme.typography.subtitle1,
            )

            ItemDivider()
        }
    }

    LaunchedEffect(items) {
        listState.scrollToItem(0)
    }
}
