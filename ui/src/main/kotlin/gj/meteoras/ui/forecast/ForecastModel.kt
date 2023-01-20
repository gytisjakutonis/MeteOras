package gj.meteoras.ui.forecast

import android.content.res.Resources
import gj.meteoras.domain.GetForecastUseCase
import gj.meteoras.ui.R
import gj.meteoras.ui.SnackbarState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@ExperimentalTime
class ForecastModel(
    private val resources: Resources,
    private val getForecastUseCase: GetForecastUseCase,
) {

    private lateinit var scope: CoroutineScope
    private val actionsChannel = Channel<ForecastAction>(Channel.BUFFERED)
    val state = MutableStateFlow(ForecastState())
    val actions = actionsChannel.receiveAsFlow()

    fun resume(scope: CoroutineScope) {
        this.scope = scope

        state.value = state.value.copy(forecast = null)
    }

    fun use(code: String) {
        scope.launch {
            work {
                getForecastUseCase(code)
            }.onSuccess { result ->
                state.value = state.value.copy(forecast = result)
            }.onFailure { error ->
                showMessage(
                    message = resources.getString(R.string.error_network),
                    action = resources.getString(R.string.action_retry)
                ) {
                    use(code)
                }
            }
        }
    }

    private fun ForecastAction.send() {
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
        ForecastAction.ShowMessage(
            state = SnackbarState(
                message = message,
                action = action,
                onAction = onAction
            )
        ).send()
    }
}
