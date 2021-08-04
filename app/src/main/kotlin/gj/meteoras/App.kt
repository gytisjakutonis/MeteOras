package gj.meteoras

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import gj.meteoras.db.dbModule
import gj.meteoras.ext.koin.TimberLogger
import gj.meteoras.ext.timber.CrashlyticsTree
import gj.meteoras.net.netModule
import gj.meteoras.net.restModule
import gj.meteoras.repo.repoModule
import gj.meteoras.ui.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import java.util.*

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        FirebaseCrashlytics.getInstance().sendUnsentReports()

        Timber.plant(CrashlyticsTree())

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
