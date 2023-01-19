package gj.meteoras.domain

import gj.meteoras.repo.repoModule
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

@ExperimentalTime
val domainModule = module {

    single {
        SyncPlacesUseCase(get())
    }

    single {
        FilterPlacesUseCase(get())
    }

    single {
        GetForecastUseCase(get())
    }
} + repoModule
