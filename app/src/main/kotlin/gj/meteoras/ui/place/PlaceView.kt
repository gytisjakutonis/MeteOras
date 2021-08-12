package gj.meteoras.ui.places.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import gj.meteoras.ui.theme.paddings

@Composable
fun PlaceView(
    scaffoldState: ScaffoldState,
    navController: NavHostController,
) {
    Column(
        modifier = Modifier.padding(MaterialTheme.paddings.screenPadding)
    ) {
        Text("place")
    }
}
