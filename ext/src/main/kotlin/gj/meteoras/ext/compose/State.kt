package gj.meteoras.ext.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect

@Composable
fun <T : R, R> MutableSharedFlow<T>.collectAsAction(initial: R): State<R> {
    val state = remember { mutableStateOf(initial) }

    LaunchedEffect(true) {
        collect { item ->
            state.value = item
            resetReplayCache()
        }
    }

    return state
}
