package gj.meteoras.ui.place

import android.content.res.Resources
import gj.meteoras.data.Place
import gj.meteoras.domain.FilterPlacesUseCase
import gj.meteoras.domain.SyncPlacesUseCase
import gj.meteoras.ui.R
import gj.meteoras.ui.SnackbarState
import gj.meteoras.ui.UiConfig
import gj.meteoras.ui.UiPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@ExperimentalTime
class PlaceModel(
    private val resources: Resources,
    private val preferences: UiPreferences,
    private val filterPlacesUseCase: FilterPlacesUseCase,
    private val syncPlacesUseCase: SyncPlacesUseCase,
) {

    private lateinit var scope: CoroutineScope
    private val actionsChannel = Channel<PlaceAction>(Channel.BUFFERED)
    private val filter = MutableStateFlow("")
    val state = MutableStateFlow(PlaceState())
    val actions = actionsChannel.receiveAsFlow()

    fun resume(scope: CoroutineScope) {
        this.scope = scope

        state.value = state.value.copy(favourites = preferences.favouritePlaces)

        this.scope.launch(Dispatchers.Default) {
            // https://medium.com/mobile-app-development-publication/kotlin-flow-buffer-is-like-a-fashion-adoption-31630a9cdb00
            filter.collectLatest(::applyFilter)
        }

        checkDisclaimer()
        sync()
    }

    fun filter(name: String) {
        filter.value = name
    }

    fun use(place: Place) {
        addFavourite(place)
        PlaceAction.OpenPlace(place = place).send()
    }

    fun sync() {
        scope.launch {
            work {
                syncPlacesUseCase()
            }.onSuccess {
                applyFilter(state.value.filter)
            }.onFailure {
                showMessage(
                    message = resources.getString(R.string.error_network),
                    action = resources.getString(R.string.action_retry),
                    ::sync
               )
            }
        }
    }

    private suspend fun applyFilter(name: String) {
        filterPlacesUseCase(name).onSuccess { list ->
            state.value = state.value.copy(places = list, filter = name)
        }.onFailure {
            state.value = state.value.copy(filter = name)
        }
    }

    private fun checkDisclaimer() {
        if (!preferences.disclaimerAccepted) {
            showMessage(
                message = resources.getString(R.string.disclaimer),
                action = resources.getString(R.string.action_accept)
            ) {
                preferences.disclaimerAccepted = true
            }
        }
    }

    private fun addFavourite(place: Place) {
        val favourites = state.value.favourites.toMutableList()
        favourites.remove(place.code)
        favourites.add(0, place.code)
        state.value = state.value.copy(favourites = favourites.take(UiConfig.favouritesLimit))
        preferences.favouritePlaces = state.value.favourites
    }

    private fun PlaceAction.send() {
        scope.launch(Dispatchers.Default) {
            actionsChannel.send(this@send)
        }
    }

    private suspend fun <T> work(block: suspend () -> T): T = try {
        state.value = state.value.copy(busy = true)
        block()
    } finally {
        state.value = state.value.copy(busy = false)
    }

    private fun showMessage(
        message: String,
        action: String? = null,
        onAction: (() -> Unit)? = null
    ) {
        PlaceAction.ShowMessage(
            state = SnackbarState(
                message = message,
                action = action,
                onAction = onAction
            )
        ).send()
    }
}
