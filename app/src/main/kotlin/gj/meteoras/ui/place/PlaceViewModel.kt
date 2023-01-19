package gj.meteoras.ui.place

import android.content.res.Resources
import gj.meteoras.repo.PlacesRepo
import gj.meteoras.ui.BaseViewModel
import kotlin.time.ExperimentalTime

@ExperimentalTime
class PlaceViewModel(
    private val resources: Resources,
    private val repo: PlacesRepo
) : BaseViewModel<PlaceViewState, PlaceViewAction>(resources, PlaceViewState()) {

    override fun PlaceViewState.kopy(busy: Boolean) = copy(busy = busy)

    suspend fun resume(code: String) {
        if (busy) return

//        work {
//            repo.getForecast(code)
//        }.onSuccess { result ->
//            state.value.copy(forecast = result).emit()
//        }.onFailure { error ->
//            PlaceViewAction.ShowMessage(
//                message = error.translate(),
//                action = resources.getString(R.string.action_retry)
//            ) {
//                resume(code)
//            }.emit()
//        }
    }
}
