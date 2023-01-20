package gj.meteoras.ui.place

import android.content.res.Resources
import gj.meteoras.data.Place
import gj.meteoras.repo.PlacesRepo
import gj.meteoras.ui.R
import gj.meteoras.ui.theme.SnackbarState
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
    private val repo: PlacesRepo,
) {

    private lateinit var scope: CoroutineScope
    private val actionsChannel = Channel<PlaceAction>(Channel.BUFFERED)
    private val filter = MutableStateFlow("")
    val state = MutableStateFlow(PlaceState())
    val actions = actionsChannel.receiveAsFlow()

    fun resume(scope: CoroutineScope) {
        this.scope = scope

        state.value = state.value.copy(favourites = repo.favourites)

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
                repo.syncPlaces()
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
        repo.filterByName(name).onSuccess { list ->
            state.value = state.value.copy(places = list, filter = name)
        }.onFailure {
            state.value = state.value.copy(filter = name)
        }
    }

    private fun checkDisclaimer() {
        if (!repo.disclaimer) {
            showMessage(
                message = resources.getString(R.string.disclaimer),
                action = resources.getString(R.string.action_accept)
            ) {
                repo.disclaimer = true
            }
        }
    }

    private fun addFavourite(place: Place) {
        repo.addFavourite(place)
        state.value = state.value.copy(favourites = repo.favourites)
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
