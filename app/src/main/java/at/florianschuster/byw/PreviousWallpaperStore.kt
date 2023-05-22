package at.florianschuster.byw

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class PreviousWallpaperStore(
    private val context: Context
) {
    private val mutex = Mutex()

    val previousHome: Flow<Uri?> = context.dataStore.data.map { preferences ->
        preferences[PREV_HOME_KEY]?.toUri()
    }

    suspend fun setPreviousHome(uri: Uri) = mutex.withLock {
        context.dataStore.edit { preferences ->
            preferences[PREV_HOME_KEY] = uri.toString()
        }
    }

    val previousLockScreen: Flow<Uri?> = context.dataStore.data.map { preferences ->
        preferences[PREV_LOCK_SCREEN_KEY]?.toUri()
    }

    suspend fun setPreviousLockScreen(uri: Uri) = mutex.withLock {
        context.dataStore.edit { preferences ->
            preferences[PREV_LOCK_SCREEN_KEY] = uri.toString()
        }
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = "previous_wallpaper_store"
        )
        private val PREV_HOME_KEY = stringPreferencesKey("key_prev_home")
        private val PREV_LOCK_SCREEN_KEY = stringPreferencesKey("key_prev_lock_screen")
    }
}
