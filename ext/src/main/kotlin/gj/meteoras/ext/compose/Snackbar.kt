package gj.meteoras.ext.compose

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult

fun SnackbarResult.then(block: () -> Unit) {
    if (this == SnackbarResult.ActionPerformed) {
        block()
    }
}

suspend fun SnackbarHostState.showSnackbar(
    message: String,
    action: String? = null,
    callback: (() -> Unit)? = null
) {
    showSnackbar(
        message,
        action,
        if (action != null) SnackbarDuration.Indefinite else SnackbarDuration.Short
    ).then {
        callback?.invoke()
    }
}
