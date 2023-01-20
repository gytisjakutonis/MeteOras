package gj.meteoras.repo

import gj.meteoras.data.Forecast
import gj.meteoras.data.Place
import gj.meteoras.db.dao.PlacesDao
import gj.meteoras.ext.alias
import gj.meteoras.ext.lang.result
import gj.meteoras.ext.lang.then
import gj.meteoras.net.api.MeteoApi
import gj.meteoras.repo.mappers.toData
import gj.meteoras.repo.mappers.toDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    var disclaimer: Boolean by alias(preferences::disclaimerAccepted)
    var favourites: List<String> by alias(preferences::favouritePlaces)

    fun addFavourite(place: Place) {
        val codes = favourites.toMutableList()
        codes.remove(place.code)
        codes.add(0, place.code)
        favourites = codes.take(RepoConfig.favouritesLimit)
    }

    suspend fun filterByName(name: String): Result<List<Place>> = result {
        withContext(Dispatchers.IO) {
            dao.findByName("$name%").map { it.toData() }
        }
    }

    suspend fun syncPlaces(): Result<Unit> =  result {
        withContext(Dispatchers.IO) {
            checkPlacesTimeout().then {
                loadPlaces()
            }
        }
    }

    suspend fun getForecast(code: String): Result<Forecast> = result {
        withContext(Dispatchers.IO) {
            api.forecast(code).toData()!!
        }
    }

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
