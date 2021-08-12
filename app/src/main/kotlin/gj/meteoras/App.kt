package gj.meteoras

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import gj.meteoras.db.dbModule
import gj.meteoras.ext.koin.TimberLogger
import gj.meteoras.net.netModule
import gj.meteoras.net.restModule
import gj.meteoras.repo.repoModule
import gj.meteoras.ui.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.*
import kotlin.time.ExperimentalTime

class App : Application() {

    @ExperimentalTime
    override fun onCreate() {
        super.onCreate()

        FirebaseCrashlytics.getInstance().sendUnsentReports()

        startKoin {
            androidContext(this@App)

            logger(TimberLogger())

            modules(
                netModule,
                restModule,
                dbModule,
                repoModule,
                uiModule
            )
        }
    }
}
