package gj.meteoras.ui.place.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import gj.meteoras.ui.theme.paddings
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun LazyItemScope.Day(
    date: LocalDate
) {
    Divider(
        modifier = Modifier.padding(
            top = MaterialTheme.paddings.small,
            bottom = MaterialTheme.paddings.small,
        )
    )

    Text(
        text = date.format(dateFormatter),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.fillMaxWidth()
    )

    Divider(
        modifier = Modifier.padding(
            top = MaterialTheme.paddings.small,
            bottom = MaterialTheme.paddings.small,
        )
    )
}

private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
