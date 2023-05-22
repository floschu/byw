package at.florianschuster.byw.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import at.florianschuster.byw.AppStore
import at.florianschuster.byw.R
import at.florianschuster.byw.Screen

@Composable
fun DetailScreen(
    type: Screen.Detail.Type,
    state: AppStore.State,
    action: (AppStore.Action) -> Unit,
    onFinished: () -> Unit
) {
    val initialBlur = remember {
        when (type) {
            Screen.Detail.Type.Home -> state.homeBlurRadius
            Screen.Detail.Type.Lockscreen -> state.lockScreenBlurRadius
        }
    }
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxHeight()
            .systemBarsPadding()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.detail_title),
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        PhoneScreenImage(
            modifier = Modifier
                .height(512.dp),
            src = when (type) {
                Screen.Detail.Type.Home -> state.selectedHome
                Screen.Detail.Type.Lockscreen -> state.selectedLockScreen
            },
            blurRadius = when (type) {
                Screen.Detail.Type.Home -> state.homeBlurRadius
                Screen.Detail.Type.Lockscreen -> state.lockScreenBlurRadius
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Slider(
            value = when (type) {
                Screen.Detail.Type.Home -> state.homeBlurRadius
                Screen.Detail.Type.Lockscreen -> state.lockScreenBlurRadius
            } ?: 0f,
            onValueChange = {
                val blur = if (it == 0f) null else it
                when (type) {
                    Screen.Detail.Type.Home -> action(AppStore.Action.SetHomeBlur(blur))
                    Screen.Detail.Type.Lockscreen -> action(AppStore.Action.SetLockScreenBlur(blur))
                }
            },
            valueRange = 0f..250f
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            IconTextButton(
                text = stringResource(id = R.string.detail_button_cancel),
                icon = Icons.Rounded.Close,
                onClick = {
                    when (type) {
                        Screen.Detail.Type.Home -> {
                            action(AppStore.Action.SetHomeBlur(initialBlur))
                        }

                        Screen.Detail.Type.Lockscreen -> {
                            action(AppStore.Action.SetLockScreenBlur(initialBlur))
                        }
                    }
                    onFinished()
                }
            )
            IconTextButton(
                text = stringResource(id = R.string.detail_button_done),
                icon = Icons.Rounded.Check,
                onClick = onFinished
            )
        }
    }
}

@Composable
private fun IconTextButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Row {
            Icon(modifier = Modifier.size(20.dp), imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
