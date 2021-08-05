package gj.meteoras.ui.places

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import gj.meteoras.data.Place
import gj.meteoras.repo.RepoResult
import gj.meteoras.repo.busy
import gj.meteoras.repo.data
import gj.meteoras.repo.error
import gj.meteoras.repo.places.PlacesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class PlacesViewModel(
    private val repo: PlacesRepo
) : ViewModel() {

    private val nameFilter = MutableStateFlow("")
    private val stateFlow = MutableStateFlow(PlacesViewState())
    // flow backed livedata, so we can emit states from non-main thread
    val state: LiveData<PlacesViewState> = stateFlow.asLiveData(Dispatchers.Default)
    val actions = MutableSharedFlow<PlacesViewAction>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        viewModelScope.launch(Dispatchers.Default) {
            nameFilter
                .debounce(filterDelayMillis)
                .distinctUntilChanged()
                .flatMapLatest { name ->
                    repo.filterByName(name)
                }
                // https://medium.com/mobile-app-development-publication/kotlin-flow-buffer-is-like-a-fashion-adoption-31630a9cdb00
                .collectLatest { result ->
                    result.handle()
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

    private suspend fun RepoResult<List<Place>>.handle() {
        stateFlow.emit(
            stateFlow.value.copy(
                places = data ?: stateFlow.value.places,
                busy = busy,
            )
        )

        error?.let {

        }
    }

    private fun Throwable.translate(): String =
        "Something wrong. Check network and retry"
}

private const val filterDelayMillis = 200L
