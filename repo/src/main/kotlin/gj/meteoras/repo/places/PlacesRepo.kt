package gj.meteoras.repo.places

import gj.meteoras.data.Place
import gj.meteoras.db.dao.PlacesDao
import gj.meteoras.ext.lang.runCatchingCancelable
import gj.meteoras.ext.lang.timber
import gj.meteoras.net.api.MeteoApi
import gj.meteoras.repo.RepoConfig
import gj.meteoras.repo.RepoPreferences
import kotlinx.coroutines.*
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

    // last resort handler to avoid app crash
    private val handler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable, "Unhandled error!")
    }
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob() + handler)
    private var loader: Deferred<Unit>? = null

    suspend fun filterByName(name: String): Result<List<Place>> = runCatchingCancelable {
        syncPlaces()?.await()
        dao.findByName("$name%")
    }.also { result ->
        result.exceptionOrNull()?.timber()
    }

    // creates job instead on simple suspend, in order to avoid multiple sync running in parallel
    @Synchronized
    fun syncPlaces(): Deferred<Unit>? {
        if (checkPlacesValid()) {
            return null
        } else if (loader?.isActive != true) {
            loader = scope.async {
                loadPlaces()
            }
        }

        return loader
    }

    private fun checkPlacesValid(): Boolean {
        val now = Instant.now()
        val lastLoad = preferences.placesTimestamp
        val duration = Duration.between(lastLoad, now)

        return duration < RepoConfig.placesTimeout
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
