package gj.meteoras.ui.places.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import gj.meteoras.data.Place
import gj.meteoras.ui.theme.paddings

@Composable
fun PlaceHeader(
    place: Place?
) {
    Column(
        modifier = Modifier.padding(MaterialTheme.paddings.screenPadding)
    ) {
        Text(
            text = place?.name ?: "",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.h4,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            place?.administrativeDivision ?: "",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            place?.country ?: place?.countryCode ?: "",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.fillMaxWidth()
        )

        Divider(
            modifier = Modifier.padding(
                top = MaterialTheme.paddings.smallPadding,
                bottom = MaterialTheme.paddings.smallPadding,
            )
        )
    }
}
