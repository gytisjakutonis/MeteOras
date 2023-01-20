package gj.meteoras.ui

import gj.meteoras.repo.repoModule
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

@ExperimentalTime
val uiModule = module {

    single {
        androidApplication().resources
    }

    single {
        gj.meteoras.ui.place.PlaceModel(get(), get())
    }

    single {
        gj.meteoras.ui.forecast.ForecastModel(get(), get())
    }
} + repoModule
