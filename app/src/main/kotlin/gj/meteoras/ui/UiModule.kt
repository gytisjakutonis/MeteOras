package gj.meteoras.ui

import gj.meteoras.ui.place.PlaceViewModel
import gj.meteoras.ui.places.PlacesViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

@ExperimentalTime
val uiModule = module {

    single {
        androidApplication().resources
    }

    viewModel {
        PlacesViewModel(get(), get())
    }

    viewModel {
        PlaceViewModel(get(), get())
    }
}
