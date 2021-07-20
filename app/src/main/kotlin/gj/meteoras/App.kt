package gj.meteoras

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import gj.meteoras.ext.koin.TimberLogger
import gj.meteoras.net.netModule
import gj.meteoras.net.restModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import java.io.File
import java.util.*

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        FirebaseCrashlytics.getInstance().sendUnsentReports()

        Timber.plant(TimberTree())

        startKoin {
            androidContext(this@App)

            logger(TimberLogger())

            modules(
                listOf(
                    netModule(cacheDir = File(cacheDir, "http")),
                    restModule(meteoUrl = BuildConfig.METEO_URL)
                )
            )
        }
    }
}
