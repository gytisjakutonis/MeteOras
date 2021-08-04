package gj.meteoras.ui

import gj.meteoras.ui.places.PlacesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {

    viewModel {
        PlacesViewModel(get())
    }
}
