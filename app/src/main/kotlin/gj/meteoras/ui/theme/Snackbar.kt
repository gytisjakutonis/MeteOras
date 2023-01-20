package gj.meteoras.ui.theme

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult

suspend fun SnackbarHostState.show(
    state: SnackbarState,
) {
    showSnackbar(
        state.message,
        state.action,
        false,
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
