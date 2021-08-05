package gj.meteoras.ui.places

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import gj.meteoras.data.Place
import gj.meteoras.repo.places.PlacesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class PlacesViewModel(
    private val repo: PlacesRepo
) : ViewModel() {

    private val nameFilter = MutableStateFlow("")
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
                .debounce(filterDelayMillis)
                .distinctUntilChanged()
                // https://medium.com/mobile-app-development-publication/kotlin-flow-buffer-is-like-a-fashion-adoption-31630a9cdb00
                .collectLatest { name ->
                    work {
                        repo.filterByName(name).handle()
                    }
                }
        }
    }

    fun filter(name: String) {
        viewModelScope.launch(Dispatchers.Default) {
            nameFilter.emit(name)
        }
    }

    fun retry() {
        filter(nameFilter.value)
    }

    private suspend fun Result<List<Place>>.handle() {
        Timber.d("VM result $this")

        stateFlow.value.copy(
            places = getOrNull() ?: stateFlow.value.places,
        ).emit()

        exceptionOrNull()?.translate()?.let { error ->
            PlacesViewAction.ShowMessage(
                message = error,
                action = "Retry"
            ) {
                retry()
            }.emit()
        }
    }

    private suspend fun work(block: suspend () -> Unit) {
        try {
            busy()
            block()
        } finally {
            idle()
        }
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
        "Something wrong. Check network and retry"
}

private const val filterDelayMillis = 200L
