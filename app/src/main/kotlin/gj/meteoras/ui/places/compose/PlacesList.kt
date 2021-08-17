package gj.meteoras.ui.places.compose

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import gj.meteoras.data.Place
import gj.meteoras.ui.theme.body0
import gj.meteoras.ui.theme.paddings

@Composable
fun PlacesList(
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

@Composable
private fun PlaceItem(
    place: Place,
    color: Color = Color.Unspecified,
    onClick: (Place) -> Unit
) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(place.name)
            }
            append(" (${place.administrativeDivision ?: ""}) ${place.countryCode}")

        },
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.body0,
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = MaterialTheme.paddings.medium,
                bottom = MaterialTheme.paddings.medium
            )
            .clickable { onClick(place) }
    )

    Divider()
}
