package gj.meteoras

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import gj.meteoras.ext.koin.TimberLogger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

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
                )
            )
        }
    }
}
