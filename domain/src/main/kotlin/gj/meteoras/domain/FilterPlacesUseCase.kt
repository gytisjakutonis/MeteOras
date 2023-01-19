package gj.meteoras.domain

import gj.meteoras.data.Place
import gj.meteoras.ext.lang.result
import gj.meteoras.repo.PlacesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.ExperimentalTime

@ExperimentalTime
class FilterPlacesUseCase(
    private val repo: PlacesRepo,
) {
    suspend operator fun invoke(name: String): Result<List<Place>> = result {
        withContext(Dispatchers.IO) {
            repo.filterByName(name)
        }
    }
}
