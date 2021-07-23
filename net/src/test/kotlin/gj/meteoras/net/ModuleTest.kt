package gj.meteoras.net

import okhttp3.OkHttpClient
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

class ModuleTest : KoinTest {

    @Test
    fun checkKoin() {
        checkModules {
            modules(
                module {
                   single { OkHttpClient.Builder().build() }
                },
                restModule
            )
        }
    }
}
