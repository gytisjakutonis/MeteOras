package gj.meteoras.ui.place

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import gj.meteoras.data.Place

@Composable
fun PlaceList(
    items: List<Place>,
    favourites: List<Place>,
    onClick: (Place) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(state = listState,) {
        items(
            items = favourites,
            key = { place -> "favourite-" + place.code }
        ) { place ->
            PlaceItem(
                place = place,
                color = MaterialTheme.colors.secondaryVariant,
                onClick = onClick
            )
        }

        items(
            items = items,
            key = { place -> place.code }
        ) { place ->
            PlaceItem(
                place = place,
                onClick = onClick
            )
        }
    }

    LaunchedEffect(items) {
        listState.scrollToItem(0)
    }
}
