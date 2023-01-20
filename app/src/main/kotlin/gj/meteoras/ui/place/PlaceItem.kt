package gj.meteoras.ui.place

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gj.meteoras.data.Place

@Composable
fun PlaceItem(
    place: Place,
    color: Color = Color.Unspecified,
    onClick: (Place) -> Unit
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(place.name)
            }
            withStyle(style = SpanStyle(fontSize = 15.sp)) {
                append(" (${place.administrativeDivision ?: ""}) ${place.countryCode}")
            }
        },
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                bottom = 10.dp
            )
            .clickable { onClick(place) }
    )

    Divider(
        color = MaterialTheme.colorScheme.secondaryContainer
    )
}
