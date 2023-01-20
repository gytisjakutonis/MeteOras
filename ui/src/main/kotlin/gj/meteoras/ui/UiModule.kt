package gj.meteoras.ui

import android.content.Context
import gj.meteoras.domain.domainModule
import gj.meteoras.ui.forecast.ForecastModel
import gj.meteoras.ui.place.PlaceModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

@ExperimentalTime
val uiModule = module {

    single {
        androidApplication().resources
    }

    single {
        UiPreferences(
            androidContext().getSharedPreferences(UiConfig.preferencesName, Context.MODE_PRIVATE)
        )
    }

    single {
        PlaceModel(get(), get(), get(), get())
    }

    single {
        ForecastModel(get(), get())
    }
} + domainModule
