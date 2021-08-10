package gj.meteoras.repo.places

import gj.meteoras.data.Place
import gj.meteoras.db.dao.PlacesDao
import gj.meteoras.ext.lang.runCatchingCancelable
import gj.meteoras.ext.lang.then
import gj.meteoras.net.api.MeteoApi
import gj.meteoras.repo.RepoConfig
import gj.meteoras.repo.RepoPreferences
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import java.time.Duration
import java.time.Instant
import java.util.concurrent.TimeoutException
import kotlin.time.ExperimentalTime
import kotlin.time.toKotlinDuration

class PlacesRepo(
    private val preferences: RepoPreferences,
    private val dao: PlacesDao,
    private val api: MeteoApi
) {

    suspend fun filterByName(name: String): Result<List<Place>> = runCatchingCancelable {
        dao.findByName("$name%")
    }

    suspend fun syncPlaces(): Result<Boolean> = runCatchingCancelable {
        checkPlacesTimeout().then {
            loadPlaces()
        }
    }

    private fun checkPlacesTimeout(): Boolean {
        val now = Instant.now()
        val lastLoad = preferences.placesTimestamp
        val duration = Duration.between(lastLoad, now)

        return duration > RepoConfig.placesTimeout
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun loadPlaces() {
        // workaround for TimeoutCancellationException not being propagated to outer scope
        val placesNet = withTimeoutOrNull(RepoConfig.apiTimeout.toKotlinDuration()) {
            api.places()
        } ?: throw TimeoutException()

        Timber.d("Loaded ${placesNet.size} places from api")

        val placesDao = placesNet.toDao()
        dao.setAll(placesDao)

        Timber.d("Cached ${placesDao.size} places into db")

        preferences.placesTimestamp = Instant.now()
    }
}
