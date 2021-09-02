package gj.meteoras

import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import org.koin.dsl.module

val appModule = module {
    single {
        AppUpdateManagerFactory.create(get())
    }

    single {
        Updater(get())
    }
}
