package gj.meteoras.ui.places

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import gj.meteoras.repo.places.PlacesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class PlacesViewModel(
    private val repo: PlacesRepo
) : ViewModel() {

    private val nameFilter = MutableStateFlow("")
    private val repoFilter = repo.filter()
    private val stateFlow = MutableStateFlow(PlacesViewState())
    // flow backed livedata, so we can emit states from non-main thread
    val state: LiveData<PlacesViewState> = stateFlow.asLiveData(Dispatchers.Default)
    val actions = MutableStateFlow<PlacesViewAction>(PlacesViewAction.None)

    init {
        viewModelScope.launch(Dispatchers.Default) {
            nameFilter
                .debounce(filterDelayMillis)
                .distinctUntilChanged()
                .collect { name ->
                    repoFilter.filterByName(name)
                }
        }

        viewModelScope.launch(Dispatchers.Default) {
            repoFilter.flow
                .collect { result ->
                    stateFlow.emit(
                        stateFlow.value.copy(
                            places = result.data ?: stateFlow.value.places,
                            busy = result.busy,
                        )
                    )


                }
        }
    }

    fun filter(name: String) {
        viewModelScope.launch(Dispatchers.Default) {
            nameFilter.emit(name)
        }
    }

    fun retry() {
        viewModelScope.launch(Dispatchers.Default) {
            repoFilter.filterByName(nameFilter.value)
        }
    }

    private fun Throwable.translate(): String =
        "Something wrong. Check network and retry"
}

private const val filterDelayMillis = 200L