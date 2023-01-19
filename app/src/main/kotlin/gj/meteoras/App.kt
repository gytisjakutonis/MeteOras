package gj.meteoras

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import gj.meteoras.domain.domainModule
import gj.meteoras.ui.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
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
                appModule + domainModule + uiModule
            )
        }
    }
}
