package gj.meteoras.ui.places

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gj.meteoras.R
import gj.meteoras.data.Place
import gj.meteoras.repo.PlacesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@ExperimentalTime
class PlacesViewModel(
    private val resources: Resources,
    private val repo: PlacesRepo
) : ViewModel() {

    private val nameFilter = MutableStateFlow("")
    val state = MutableStateFlow(PlacesViewState())
    val action = MutableSharedFlow<PlacesViewAction>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        viewModelScope.launch(Dispatchers.Default) {
            // https://medium.com/mobile-app-development-publication/kotlin-flow-buffer-is-like-a-fashion-adoption-31630a9cdb00
            nameFilter.collectLatest { filterByName(it) }
        }
    }

    suspend fun resume() {
        work {
            repo.syncPlaces()
        }.onSuccess { result ->
            if (result) {
                filterByName(state.value.filter)
            }
        }.onFailure { error ->
            PlacesViewAction.ShowMessage(
                message = error.translate(),
                action = resources.getString(R.string.action_retry)
            ) {
                resume()
            }.emit()
        }
    }

    suspend fun filter(name: String) {
        nameFilter.emit(name)
    }

    suspend fun use(place: Place) {
        PlacesViewAction.OpenPlace(place = place).emit()
    }

    private suspend fun filterByName(name: String) {
        repo.filterByName(name)
            .onSuccess { value ->
                state.value.copy(places = value, filter = name).emit()
            }.onFailure { error ->
                state.value.copy(filter = name).emit()
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

    private suspend fun PlacesViewState.emit() {
        state.emit(this)
    }

    private suspend fun PlacesViewAction.emit() {
        action.emit(this)
    }

    private fun Throwable.translate(): String =
        resources.getString(R.string.error_network)
}
