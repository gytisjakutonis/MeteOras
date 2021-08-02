package gj.meteoras.repo

import android.content.Context
import gj.meteoras.repo.places.PlacesRepo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repoModule = module {
    single {
        RepoPreferences(
            androidContext().getSharedPreferences(RepoConfig.preferencesName, Context.MODE_PRIVATE)
        )
    }

    single {
        PlacesRepo(get(), get(), get())
    }
}
