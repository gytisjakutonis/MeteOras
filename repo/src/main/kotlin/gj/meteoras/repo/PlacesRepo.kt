package gj.meteoras.repo

import gj.meteoras.data.Forecast
import gj.meteoras.data.Place
import gj.meteoras.db.dao.PlacesDao
import gj.meteoras.ext.lang.runCatchingCancelable
import gj.meteoras.ext.lang.then
import gj.meteoras.ext.lang.timber
import gj.meteoras.net.api.MeteoApi
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

    suspend fun filterByName(name: String): Result<List<Place>> = runCatchingCancelable {
        dao.findByName("$name%")
    }.apply {
        exceptionOrNull()?.timber()
    }

    suspend fun syncPlaces(): Result<Boolean> = runCatchingCancelable {
        checkPlacesTimeout().then {
            loadPlaces()
        }
    }.apply {
        exceptionOrNull()?.timber()
    }

    suspend fun getPlace(code: String): Result<Place> = runCatchingCancelable {
        val old = dao.findByCode(code)

        when {
            old == null -> api.place(code).toDao()?.let { new ->
                Timber.d("Loaded $code place")
                new.copy(id = dao.insert(new).toInt())
            }
            !old.complete -> api.place(code).toDao()?.let { new ->
                Timber.d("Loaded $code place")
                new.copy(id = old.id).also { dao.update(it) }
            }
            else -> old
        }!!
    }.apply {
        exceptionOrNull()?.timber()
    }

    suspend fun getForecast(code: String): Result<Forecast> = runCatchingCancelable {
        Timber.d("Loaded $code forecast")
        api.forecast(code).toDao()!!
    }.apply {
        exceptionOrNull()?.timber()
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

        val placesDao = placesNet.toDao()
        dao.setAll(placesDao)
        Timber.d("Loaded ${placesDao.size} places")

        preferences.placesTimestamp = Instant.now()
    }
}
