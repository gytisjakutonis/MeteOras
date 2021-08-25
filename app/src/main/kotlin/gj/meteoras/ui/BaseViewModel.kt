package gj.meteoras.ui

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import gj.meteoras.R
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.ExperimentalTime

@ExperimentalTime
abstract class BaseViewModel<State : BaseViewState, Action>(
    private val resources: Resources,
    initialState: State
) : ViewModel() {

    val state = MutableStateFlow(initialState)
        val action = MutableSharedFlow<Action>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    internal val busy: Boolean get() = state.value.busy

    internal abstract fun State.kopy(busy: Boolean): State

    internal suspend fun <T> work(block: suspend () -> Result<T>): Result<T> {
        return try {
            busy()
            block()
        } finally {
            idle()
        }
    }

    private suspend fun busy(busy: Boolean = true) {
        state.value.kopy(busy = busy).emit()
    }

    private suspend fun idle() {
        busy(false)
    }

    internal open fun Throwable.translate(): String =
        resources.getString(R.string.error_network)

    internal suspend fun State.emit() {
        state.emit(this)
    }

    internal suspend fun Action.emit() {
        action.emit(this)
    }
}
