package gj.meteoras.domain

import gj.meteoras.ext.lang.result
import gj.meteoras.repo.PlacesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.ExperimentalTime

@ExperimentalTime
class SyncPlacesUseCase(
    private val repo: PlacesRepo,
) {
    suspend operator fun invoke(): Result<Unit> = result {
        withContext(Dispatchers.IO) {
            repo.syncPlaces()
        }
    }
}
