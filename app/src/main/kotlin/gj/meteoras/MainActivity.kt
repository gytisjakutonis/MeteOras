package gj.meteoras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import gj.meteoras.ui.theme.Navigation
import gj.meteoras.ui.theme.Theme
import kotlin.time.ExperimentalTime

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalTime
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Theme {
                Navigation()
            }
        }
    }
}
