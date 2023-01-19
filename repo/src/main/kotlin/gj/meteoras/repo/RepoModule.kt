package gj.meteoras.repo

import android.content.Context
import gj.meteoras.db.dbModule
import gj.meteoras.net.netModule
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

@ExperimentalTime
val repoModule = module {

    single {
        RepoPreferences(
            androidContext().getSharedPreferences(RepoConfig.preferencesName, Context.MODE_PRIVATE)
        )
    }

    single {
        PlacesRepo(get(), get(), get())
    }
} + dbModule + netModule
