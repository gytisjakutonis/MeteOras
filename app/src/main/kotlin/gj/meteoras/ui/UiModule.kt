package gj.meteoras.ui

import android.content.Context
import gj.meteoras.ui.place.PlaceViewModel
import gj.meteoras.ui.places.PlacesViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
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

    viewModel {
        PlacesViewModel(get(), get(), get())
    }

    viewModel {
        PlaceViewModel(get(), get())
    }
}
