package gj.meteoras.ext.compose

import androidx.compose.material.SnackbarResult

fun SnackbarResult.then(block: () -> Unit) {
    if (this == SnackbarResult.ActionPerformed) {
        block()
    }
}
