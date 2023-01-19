package gj.meteoras.repo

import gj.meteoras.data.Forecast
import gj.meteoras.data.Place
import gj.meteoras.db.dao.PlacesDao
import gj.meteoras.ext.lang.then
import gj.meteoras.net.api.MeteoApi
import gj.meteoras.repo.mappers.toData
import gj.meteoras.repo.mappers.toDb
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import java.time.Duration
import java.time.Instant
import java.util.concurrent.TimeoutException
import kotlin.time.ExperimentalTime
import kotlin.time.toKotlinDuration

@ExperimentalTime
class PlacesRepo(
    private val preferences: RepoPreferences,
    private val dao: PlacesDao,
    private val api: MeteoApi
) {

    suspend fun filterByName(name: String): List<Place> =
        dao.findByName("$name%").map { it.toData() }

    suspend fun syncPlaces() {
        checkPlacesTimeout().then {
            loadPlaces()
        }
    }

    suspend fun getForecast(code: String): Forecast? =
        api.forecast(code).toData()

    private suspend fun checkPlacesTimeout(): Boolean {
        val now = Instant.now()
        val lastLoad = preferences.placesTimestamp
        val duration = Duration.between(lastLoad, now)

        return duration > RepoConfig.placesTimeout || dao.countAll() <= 0
    }

    private suspend fun loadPlaces() {
        // workaround for TimeoutCancellationException not being propagated to outer scope
        val placesNet = withTimeoutOrNull(RepoConfig.apiTimeout.toKotlinDuration()) {
            api.places()
        } ?: throw TimeoutException()

        val placesDao = placesNet.mapNotNull { it.toDb() }
        dao.setAll(placesDao)
        Timber.d("Loaded ${placesDao.size} places")

        preferences.placesTimestamp = Instant.now()
    }
}
