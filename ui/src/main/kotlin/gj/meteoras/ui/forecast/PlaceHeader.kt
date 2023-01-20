package gj.meteoras.ui.forecast

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import gj.meteoras.data.Place

@Composable
fun PlaceHeader(
    place: Place
) {
    Column {
        Text(
            text = place.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.h4,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            place.administrativeDivision ?: "",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            place.country ?: place.countryCode,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
