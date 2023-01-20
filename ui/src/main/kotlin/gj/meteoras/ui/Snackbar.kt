package gj.meteoras.ui

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult

suspend fun SnackbarHostState.show(
    state: SnackbarState,
) {
    showSnackbar(
        state.message,
        state.action,
        if (state.action != null) SnackbarDuration.Indefinite else SnackbarDuration.Short
    ).takeIf {
        it == SnackbarResult.ActionPerformed
    }?.let {
        state.onAction?.invoke()
    }
}

data class SnackbarState(
    val message: String,
    val action: String? = null,
    val onAction: (() -> Unit)? = null
)
