package gj.meteoras.ui.places

import android.content.res.Resources
import androidx.lifecycle.viewModelScope
import gj.meteoras.R
import gj.meteoras.data.Place
import gj.meteoras.repo.PlacesRepo
import gj.meteoras.ui.BaseViewModel
import gj.meteoras.ui.UiConfig
import gj.meteoras.ui.UiPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@ExperimentalTime
class PlacesViewModel(
    private val resources: Resources,
    private val preferences: UiPreferences,
    private val repo: PlacesRepo
) : BaseViewModel<PlacesViewState, PlacesViewAction>(resources, PlacesViewState()) {

    private val nameFilter = MutableStateFlow("")

    init {
        viewModelScope.launch(Dispatchers.Default) {
            // https://medium.com/mobile-app-development-publication/kotlin-flow-buffer-is-like-a-fashion-adoption-31630a9cdb00
            nameFilter.collectLatest { filterByName(it) }
        }
    }

    override fun PlacesViewState.kopy(busy: Boolean) = copy(busy = busy)

    suspend fun resume() {
        if (busy) return

        if (!preferences.disclaimerAccepted) {
            PlacesViewAction.ShowMessage(
                message = resources.getString(R.string.disclaimer),
                action = resources.getString(R.string.action_accept)
            ) {
                accept()
            }.emit()
        }

        state.value.copy(favourites = preferences.favouritePlaces).emit()

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
        addFavourite(place)
        PlacesViewAction.OpenPlace(place = place).emit()
    }

    suspend fun accept() {
        preferences.disclaimerAccepted = true
    }

    private suspend fun addFavourite(place: Place) {
        val favourites = state.value.favourites.toMutableList()
        favourites.remove(place.code)
        favourites.add(0, place.code)
        state.value.copy(favourites = favourites.take(UiConfig.favouritesLimit)).emit()
        preferences.favouritePlaces = state.value.favourites
    }

    private suspend fun filterByName(name: String) {
        repo.filterByName(name)
            .onSuccess { value ->
                state.value.copy(places = value, filter = name).emit()
            }.onFailure { error ->
                state.value.copy(filter = name).emit()
            }
    }
}
