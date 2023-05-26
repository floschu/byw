package at.florianschuster.byw

import android.app.WallpaperManager
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.getSystemService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.seconds

class AppStore(
    private val context: Context,
    private val scope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val previousStore: PreviousWallpaperStore = PreviousWallpaperStore(context)
) {
    private val wallpaperManager: WallpaperManager = checkNotNull(context.getSystemService()) {
        "No WallpaperManager?"
    }

    sealed interface Action {
        data class SetEditSeparately(val editSeparately: Boolean) : Action
        data class SetHome(val uri: Uri) : Action
        data class SetHomeBlur(val blur: Float?) : Action
        data class SetLockScreen(val uri: Uri) : Action
        data class SetLockScreenBlur(val blur: Float?) : Action
        object Apply : Action
    }

    data class State(
        val editSeparately: Boolean = false,

        val selectedHome: Uri? = null,
        val homeBlurRadius: Float? = null,
        val homeSetResult: Result = Result.Uninitialized,

        val selectedLockScreen: Uri? = null,
        val lockScreenBlurRadius: Float? = null,
        val lockScreenSetResult: Result = Result.Uninitialized,

        val loading: Boolean = false
    ) {
        private val canApplyHome: Boolean
            get() = selectedHome != null
        private val canApplyLockScreen: Boolean
            get() = selectedLockScreen != null
        val canApply: Boolean
            get() = !loading && (canApplyHome || canApplyLockScreen)

        sealed interface Result {
            object Uninitialized : Result
            object Success : Result
            data class Error(val error: Throwable) : Result

            val show: Boolean get() = this is Success || this is Error
        }
    }

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    init {
        scope.launch(ioDispatcher) {
            _state.update { it.copy(loading = true) }
            awaitAll(
                async {
                    val home = previousStore.previousHome.first()
                        ?: defaultHomeWallpaper()
                        ?: return@async
                    _state.update { it.copy(selectedHome = home) }
                },
                async {
                    val lockScreen = previousStore.previousLockScreen.first()
                        ?: defaultLockScreenWallpaper()
                        ?: return@async
                    _state.update { it.copy(selectedLockScreen = lockScreen) }
                }
            )
            _state.update { it.copy(loading = false) }
        }
    }

    fun dispatch(action: Action) {
        when (action) {
            is Action.SetEditSeparately -> _state.update {
                it.copy(editSeparately = action.editSeparately)
            }

            is Action.SetHome -> _state.update {
                it.copy(selectedHome = action.uri)
            }

            is Action.SetLockScreen -> _state.update {
                it.copy(selectedLockScreen = action.uri)
            }

            is Action.SetHomeBlur -> _state.update {
                it.copy(homeBlurRadius = action.blur)
            }

            is Action.SetLockScreenBlur -> _state.update {
                it.copy(lockScreenBlurRadius = action.blur)
            }

            is Action.Apply -> scope.launch {
                _state.update { it.copy(loading = true) }
                withContext(ioDispatcher) {
                    launch { applyHome() }
                    launch { applyLockScreen() }
                }
                _state.update { it.copy(loading = false) }
            }
        }
    }

    private suspend fun defaultHomeWallpaper() = withTimeout(3.seconds) {
        val defaultHome = wallpaperManager.getDefaultHomeWallpaper()
            ?: return@withTimeout null
        context.cache(defaultHome)
    }

    private suspend fun defaultLockScreenWallpaper() = withTimeout(3.seconds) {
        val defaultLockScreen = wallpaperManager.getDefaultLockScreenWallpaper()
            ?: return@withTimeout null
        context.cache(defaultLockScreen)
    }

    private suspend fun applyLockScreen() {
        val currentState = _state.value
        val result = kotlin.runCatching {
            val selectedLockScreen = checkNotNull(currentState.selectedLockScreen)
            val lockscreenBitmap = if (currentState.lockScreenBlurRadius == null) {
                context.bitmap(selectedLockScreen)
            } else {
                context.blurredBitmap(
                    selectedLockScreen,
                    currentState.lockScreenBlurRadius
                )
            }
            wallpaperManager.setLockScreenWallpaper(lockscreenBitmap)
            previousStore.setPreviousLockScreen(context.storeInternal(lockscreenBitmap))
        }.fold(
            onSuccess = { State.Result.Success },
            onFailure = { error ->
                Log.e("AppStore", "Cannot apply Lockscreen: $error")
                State.Result.Error(error)
            }
        )
        _state.update { it.copy(lockScreenSetResult = result) }
        delay(1500)
        _state.update { it.copy(lockScreenSetResult = State.Result.Uninitialized) }
    }

    private suspend fun applyHome() {
        val currentState = _state.value
        val result = kotlin.runCatching {
            val selectedHome = checkNotNull(currentState.selectedHome)
            val homeBitmap = if (currentState.homeBlurRadius == null) {
                context.bitmap(selectedHome)
            } else {
                context.blurredBitmap(
                    selectedHome,
                    currentState.homeBlurRadius
                )
            }
            wallpaperManager.setHomeWallpaper(homeBitmap)
            previousStore.setPreviousHome(context.storeInternal(homeBitmap))
        }.fold(
            onSuccess = { State.Result.Success },
            onFailure = { error ->
                Log.e("AppStore", "Cannot apply Home: $error")
                State.Result.Error(error)
            }
        )
        _state.update { it.copy(homeSetResult = result) }
        delay(1500)
        _state.update { it.copy(homeSetResult = State.Result.Uninitialized) }
    }
}
