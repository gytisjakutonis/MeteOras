package gj.meteoras.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import gj.meteoras.ui.theme.Navigation
import gj.meteoras.ui.theme.Theme
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            Theme {
                Navigation()
            }
        }
    }
}
