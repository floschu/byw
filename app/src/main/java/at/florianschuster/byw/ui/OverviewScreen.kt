package at.florianschuster.byw.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.LinkOff
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ThumbDown
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material.icons.rounded.Wallpaper
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import at.florianschuster.byw.AppStore
import at.florianschuster.byw.R
import at.florianschuster.byw.Screen
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions

@Composable
fun OverviewScreen(
    state: AppStore.State,
    action: (AppStore.Action) -> Unit,
    onGoToDetail: (Screen.Detail.Type) -> Unit,
    onGoToInfo: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        val (
            progress,
            linkButton,
            infoButton,
            title,
            card,
            applyButton,
            homeSetText,
            lockscreenSetText
        ) = createRefs()

        if (state.loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(28.dp)
                    .constrainAs(progress) {
                        top.linkTo(infoButton.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(infoButton.bottom)
                    }
            )
        }
        IconButton(
            modifier = Modifier.constrainAs(linkButton) {
                top.linkTo(parent.top)
                end.linkTo(infoButton.start)
            },
            enabled = !state.loading,
            onClick = {
                action(AppStore.Action.SetEditSeparately(!state.editSeparately))
            }
        ) {
            Icon(
                imageVector = if (state.editSeparately) {
                    Icons.Outlined.Link
                } else {
                    Icons.Outlined.LinkOff
                },
                contentDescription = stringResource(
                    id = R.string.overview_content_description_linked
                )
            )
        }
        IconButton(
            modifier = Modifier.constrainAs(infoButton) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            },
            enabled = !state.loading,
            onClick = onGoToInfo
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(
                    id = R.string.overview_content_description_info
                )
            )
        }

        Text(
            modifier = Modifier.constrainAs(title) {
                bottom.linkTo(card.top, margin = 16.dp)
                linkTo(parent.start, parent.end, bias = 0f)
            },
            text = stringResource(id = R.string.overview_title),
            style = MaterialTheme.typography.displaySmall
        )
        OverviewCard(
            modifier = Modifier.constrainAs(card) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                linkTo(parent.top, parent.bottom, bias = 0.6f)
            },
            state = state,
            action = action,
            onGoToDetail = onGoToDetail
        )

        WallpaperSetText(
            modifier = Modifier.constrainAs(homeSetText) {
                top.linkTo(card.bottom, margin = 16.dp)
                linkTo(parent.start, parent.end)
            },
            what = Screen.Detail.Type.Home,
            result = state.homeSetResult
        )
        WallpaperSetText(
            modifier = Modifier.constrainAs(lockscreenSetText) {
                top.linkTo(card.bottom, margin = 32.dp)
                linkTo(parent.start, parent.end)
            },
            what = Screen.Detail.Type.Lockscreen,
            result = state.lockScreenSetResult
        )
        if (state.canApply) {
            IconButton(
                modifier = Modifier.constrainAs(applyButton) {
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    linkTo(parent.start, parent.end)
                },
                onClick = { action(AppStore.Action.Apply) }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = stringResource(id = R.string.overview_button_apply)
                )
            }
        }
    }
}

@Composable
private fun OverviewCard(
    modifier: Modifier = Modifier,
    state: AppStore.State,
    action: (AppStore.Action) -> Unit,
    onGoToDetail: (Screen.Detail.Type) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OverviewTile(
                    modifier = Modifier.weight(1f),
                    title = stringResource(id = R.string.overview_subtitle_home),
                    selected = state.selectedHome,
                    blurRadius = state.homeBlurRadius,
                    clickEnabled = !state.loading && state.selectedHome != null,
                    onClick = { onGoToDetail(Screen.Detail.Type.Home) }
                )

                Spacer(modifier = Modifier.width(16.dp))

                OverviewTile(
                    modifier = Modifier.weight(1f),
                    title = stringResource(id = R.string.overview_subtitle_lockscreen),
                    selected = state.selectedLockScreen,
                    blurRadius = state.lockScreenBlurRadius,
                    clickEnabled = !state.loading && state.selectedLockScreen != null,
                    onClick = { onGoToDetail(Screen.Detail.Type.Lockscreen) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.editSeparately) {
                Row {
                    ChangeWallpaperButton(
                        title = stringResource(id = R.string.overview_button_change_home),
                        enabled = !state.loading,
                        onImagePicked = { uri ->
                            action(AppStore.Action.SetHome(uri))
                        }
                    )
                    ChangeWallpaperButton(
                        title = stringResource(id = R.string.overview_button_change_lockscreen),
                        enabled = !state.loading,
                        onImagePicked = { uri ->
                            action(AppStore.Action.SetLockScreen(uri))
                        }
                    )
                }
            } else {
                ChangeWallpaperButton(
                    title = stringResource(id = R.string.overview_button_wallpaper),
                    enabled = !state.loading,
                    onImagePicked = { uri ->
                        action(AppStore.Action.SetHome(uri))
                        action(AppStore.Action.SetLockScreen(uri))
                    }
                )
            }
        }
    }
}

@Composable
private fun OverviewTile(
    modifier: Modifier = Modifier,
    title: String,
    selected: Uri?,
    blurRadius: Float?,
    clickEnabled: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(8.dp))
        PhoneScreenImage(
            src = selected,
            blurRadius = blurRadius,
            onClick = if (clickEnabled) onClick else null
        )
    }
}

@Composable
private fun ChangeWallpaperButton(
    modifier: Modifier = Modifier,
    title: String,
    enabled: Boolean,
    onImagePicked: (Uri) -> Unit
) {
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme
    val cropImage = rememberLauncherForActivityResult(
        contract = CropImageContract(),
        onResult = { cropResult ->
            if (!cropResult.isSuccessful) return@rememberLauncherForActivityResult
            cropResult.uriContent?.let(onImagePicked)
        }
    )
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                val phoneScreen = context.phoneScreen()
                cropImage.launch(
                    CropImageContractOptions(
                        uri,
                        CropImageOptions(
                            fixAspectRatio = true,
                            aspectRatioX = phoneScreen.width,
                            aspectRatioY = phoneScreen.height,

                            toolbarColor = colorScheme.surface.toArgb(),
                            toolbarBackButtonColor = colorScheme.onSurface.toArgb(),
                            activityMenuTextColor = colorScheme.onSurface.toArgb(),
                            activityMenuIconColor = colorScheme.onSurface.toArgb(),

                            activityBackgroundColor = colorScheme.surface.toArgb()
                        )
                    )
                )
            }
        }
    )
    TextButton(
        modifier = modifier,
        enabled = enabled,
        onClick = {
            photoPicker.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Rounded.Wallpaper,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun WallpaperSetText(
    modifier: Modifier = Modifier,
    what: Screen.Detail.Type,
    result: AppStore.State.Result
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = result.show,
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        val success = when (result) {
            is AppStore.State.Result.Success -> true
            is AppStore.State.Result.Error -> false
            is AppStore.State.Result.Uninitialized -> return@AnimatedVisibility
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when {
                    success -> stringResource(
                        R.string.overview_wallpaper_set_success,
                        stringResource(what.displayName)
                    )

                    else -> stringResource(
                        R.string.overview_wallpaper_set_error,
                        stringResource(what.displayName)
                    )
                },
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                modifier = Modifier.size(12.dp),
                imageVector = if (success) Icons.Rounded.ThumbUp else Icons.Rounded.ThumbDown,
                contentDescription = null
            )
        }
    }
}
