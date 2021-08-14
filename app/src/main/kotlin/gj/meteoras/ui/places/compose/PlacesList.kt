package gj.meteoras.ui.places.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
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
    onClick: (Place) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(state = listState,) {
        items(
            items = items,
            key = { place -> place.code }
        ) { place ->
            val scroll = rememberScrollState(0)

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
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scroll)
                    .padding(
                        top = MaterialTheme.paddings.mediumPadding,
                        bottom = MaterialTheme.paddings.mediumPadding
                    )
                    .clickable { onClick(place) }
            )

            Divider()
        }
    }

    LaunchedEffect(items) {
        listState.scrollToItem(0)
    }
}
