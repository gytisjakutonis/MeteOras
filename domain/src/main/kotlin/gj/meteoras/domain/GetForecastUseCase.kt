package gj.meteoras.domain

import gj.meteoras.data.Forecast
import gj.meteoras.ext.lang.result
import gj.meteoras.repo.PlacesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.ExperimentalTime

@ExperimentalTime
class GetForecastUseCase(
    private val repo: PlacesRepo,
) {
    suspend operator fun invoke(code: String): Result<Forecast> = result {
        withContext(Dispatchers.IO) {
            repo.getForecast(code)!!
        }
    }
}
