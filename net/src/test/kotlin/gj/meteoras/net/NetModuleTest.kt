package gj.meteoras.net

import okhttp3.OkHttpClient
import org.junit.Test
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

class NetModuleTest : KoinTest {

    @Test
    fun checkKoin() {
        checkModules {
            modules(
                module {
                   single { OkHttpClient.Builder().build() }
                },
                apiModule
            )
        }

        stopKoin()
    }
}
