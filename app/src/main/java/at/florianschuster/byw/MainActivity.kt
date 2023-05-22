package at.florianschuster.byw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import at.florianschuster.byw.ui.BYWTheme
import at.florianschuster.byw.ui.DetailScreen
import at.florianschuster.byw.ui.InfoScreen
import at.florianschuster.byw.ui.OverviewScreen
import at.florianschuster.byw.ui.openInfoCustomTab
import at.florianschuster.byw.ui.sendFeedbackEmail
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.rememberNavController

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val store = AppStore(
            context = this,
            scope = lifecycleScope
        )

        setContent {
            BYWTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController<Screen>(
                        startDestination = Screen.Overview
                    )
                    NavBackHandler(navController)
                    val state by store.state.collectAsStateWithLifecycle()
                    var showInfo by rememberSaveable { mutableStateOf(false) }
                    AnimatedNavHost(controller = navController) { screen ->
                        when (screen) {
                            is Screen.Overview -> {
                                OverviewScreen(
                                    state = state,
                                    action = store::dispatch,
                                    onGoToDetail = { navController.navigate(Screen.Detail(it)) },
                                    onGoToInfo = { showInfo = true }
                                )
                            }

                            is Screen.Detail -> {
                                DetailScreen(
                                    type = screen.type,
                                    state = state,
                                    action = store::dispatch,
                                    onFinished = navController::pop
                                )
                            }
                        }
                    }
                    if (showInfo) {
                        InfoScreen(
                            onSendFeedback = { sendFeedbackEmail() },
                            onShowDevWebsite = { openInfoCustomTab() },
                            onDismiss = { showInfo = false }
                        )
                    }
                }
            }
        }
    }
}
