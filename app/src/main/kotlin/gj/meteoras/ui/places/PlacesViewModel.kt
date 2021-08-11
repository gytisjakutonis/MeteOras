package gj.meteoras.ui.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gj.meteoras.ext.coroutines.Forced
import gj.meteoras.ext.coroutines.ForcedStateFlow
import gj.meteoras.ext.coroutines.distinct
import gj.meteoras.ext.lang.timber
import gj.meteoras.repo.places.PlacesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class PlacesViewModel(
    private val repo: PlacesRepo
) : ViewModel() {

    private val nameFilter = ForcedStateFlow("")
    val state = MutableStateFlow(PlacesViewState())
    val action = MutableSharedFlow<PlacesViewAction>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        viewModelScope.launch(Dispatchers.Default) {
            PlacesViewAction.ShowMessage(message = "Init", action = "Dismiss").emit()
        }

        viewModelScope.launch(Dispatchers.Default) {
            nameFilter
                .distinct(filterDelay)
                // https://medium.com/mobile-app-development-publication/kotlin-flow-buffer-is-like-a-fashion-adoption-31630a9cdb00
                .collectLatest { name ->
                    repo.filterByName(name)
                        .onSuccess { value ->
                            state.value.copy(places = value, filter = name).emit()
                        }.onFailure { error ->
                            error.timber()
                            state.value.copy(filter = name).emit()
                        }

                    if (name == "aa") {
                        PlacesViewAction.ShowMessage(message = "Test", action = "Dismiss").emit()
                    }
                }
        }
    }

    fun resume() {
        viewModelScope.launch(Dispatchers.Default) {
            work {
                repo.syncPlaces()
            }.onSuccess { result ->
                if (result) {
                    nameFilter.emit(Forced(value = state.value.filter, forced = true))
                }
            }.onFailure { error ->
                error.timber()

                PlacesViewAction.ShowMessage(
                    message = error.translate(),
                    action = "Retry"
                ) {
                    resume()
                }.emit()
            }
        }
    }

    fun filter(name: String) {
        viewModelScope.launch(Dispatchers.Default) {
            nameFilter.emit(Forced(value = name))
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
        "Something wrong. Check network"
}

@OptIn(ExperimentalTime::class)
private val filterDelay = Duration.milliseconds(200L)
