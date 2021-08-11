package gj.meteoras.ui.places

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import gj.meteoras.ext.coroutines.Forced
import gj.meteoras.ext.coroutines.ForcedStateFlow
import gj.meteoras.ext.coroutines.distinct
import gj.meteoras.repo.places.PlacesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
class PlacesViewModel(
    private val repo: PlacesRepo
) : ViewModel() {

    private val nameFilter = ForcedStateFlow("")
    private val stateFlow = MutableStateFlow(PlacesViewState())
    // flow backed livedata, so we can emit states from non-main thread
    val state: LiveData<PlacesViewState> = stateFlow.asLiveData(Dispatchers.Default)
    val actions = MutableSharedFlow<PlacesViewAction>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        viewModelScope.launch(Dispatchers.Default) {
            nameFilter
                .distinct(filterDelay)
                // https://medium.com/mobile-app-development-publication/kotlin-flow-buffer-is-like-a-fashion-adoption-31630a9cdb00
                .collectLatest { name ->
                    repo.filterByName(name)
                        .onSuccess { value ->
                            stateFlow.value.copy(places = value, filter = name).emit()
                        }.onFailure { error ->
                            error.timber()
                            stateFlow.value.copy(filter = name).emit()
                        }

                    if (name == "aa") {
                        PlacesViewAction.ShowMessage(message = "Test", action = "Dismiss").emit()
                    }
                }
        }
    }

    fun resume() {
        viewModelScope.launch {
            Dispatchers.Default.invoke {
                work {
                    repo.syncPlaces()
                }
            }.onSuccess { result ->
                if (result) {
                    nameFilter.emit(Forced(value = stateFlow.value.filter, forced = true))
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
        stateFlow.value.copy(busy = busy).emit()
    }

    private suspend fun idle() {
        busy(false)
    }

    private suspend fun PlacesViewState.emit() {
        stateFlow.emit(this)
    }

    private suspend fun PlacesViewAction.emit() {
        actions.emit(this)
    }

    private fun Throwable.translate(): String =
        "Something wrong. Check network"
}

@OptIn(ExperimentalTime::class)
private val filterDelay = Duration.milliseconds(200L)
