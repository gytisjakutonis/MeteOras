package gj.meteoras.repo.places

import androidx.paging.Pager
import androidx.paging.PagingConfig
import gj.meteoras.data.Place
import gj.meteoras.db.dao.PlacesDao
import gj.meteoras.net.api.MeteoApi
import gj.meteoras.repo.RepoConfig
import gj.meteoras.repo.RepoPreferences
import kotlinx.coroutines.*
import java.time.Duration
import java.time.Instant

class PlacesRepo(
    private val preferences: RepoPreferences,
    private val dao: PlacesDao,
    private val api: MeteoApi
) {

    private var loadJob: Job? = null

    fun CoroutineScope.findPlaces(
        nameStartsWith: String,
        config: PagingConfig
    ): Pager<Int, Place> {
        //var dataSource: PagingSource<Int, Place>? = null

        if (checkPlacesExpiry() && loadJob?.isActive != true) {
            /*
            https://elizarov.medium.com/coroutine-context-and-scope-c8b255d59055
            Suspending functions, on the other hand,
            are designed to be non-blocking and should not have side-effects of launching any concurrent work.
            Suspending functions can and should wait for all their work to complete before returning to the caller³.
            */
            loadJob = launch {
                loadPlaces()
                //dataSource?.invalidate()
            }
        }

        return Pager(
            config = config
        ) {
            dao.findAll("$nameStartsWith%")//.also { dataSource = it }
        }
    }

    private fun checkPlacesExpiry(): Boolean {
        val now = Instant.now()
        val lastLoad = preferences.placesTimestamp
        val duration = Duration.between(lastLoad, now)

        return duration.seconds > RepoConfig.placesExpirySeconds
    }

    private suspend fun loadPlaces() = Dispatchers.IO.invoke {
        val placesNet = api.places()
        val placesDao = placesNet.toDao()

        dao.setAll(placesDao)

        preferences.placesTimestamp = Instant.now()
    }
}

