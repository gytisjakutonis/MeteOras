package gj.meteoras.ui.place

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gj.meteoras.R
import gj.meteoras.repo.places.PlacesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@ExperimentalTime
class PlaceViewModel(
    private val resources: Resources,
    private val repo: PlacesRepo
) : ViewModel() {

    val state = MutableStateFlow(PlaceViewState())
    val action = MutableSharedFlow<PlaceViewAction>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun resume(code: String) {
        viewModelScope.launch(Dispatchers.Default) {
            work {
                repo.getPlace(code)
            }.onSuccess { result ->
                if (result != null) {
                    state.value.copy(place = result).emit()
                } else {
                    PlaceViewAction.GoBack.emit()
                }
            }.onFailure { error ->
                PlaceViewAction.ShowMessage(
                    message = error.translate(),
                    action = resources.getString(R.string.action_retry)
                ) {
                    resume(code)
                }.emit()
            }
        }
    }

    private suspend fun <T> work(block: suspend () -> T): T = try {
        busy()
        block()
    } finally {
        idle()
    }

    private suspend fun busy(busy: Boolean = true) {
        state.value.copy(busy = busy).emit()
    }

    private suspend fun idle() {
        busy(false)
    }

    private suspend fun PlaceViewState.emit() {
        state.emit(this)
    }

    private suspend fun PlaceViewAction.emit() {
        action.emit(this)
    }

    private fun Throwable.translate(): String =
        resources.getString(R.string.error_network)
}
