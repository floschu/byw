package at.florianschuster.byw

import android.app.WallpaperManager
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap

fun WallpaperManager.setHomeWallpaper(
    bitmap: Bitmap
) {
    setBitmap(
        bitmap,
        null,
        true,
        WallpaperManager.FLAG_SYSTEM
    )
}

fun WallpaperManager.getDefaultHomeWallpaper(): Bitmap? {
    return getBuiltInDrawable(WallpaperManager.FLAG_SYSTEM)?.toBitmap()
}

fun WallpaperManager.setLockScreenWallpaper(
    bitmap: Bitmap
) {
    setBitmap(
        bitmap,
        null,
        true,
        WallpaperManager.FLAG_LOCK
    )
}

fun WallpaperManager.getDefaultLockScreenWallpaper(): Bitmap? {
    return getBuiltInDrawable(WallpaperManager.FLAG_LOCK)?.toBitmap()
        ?: getDefaultHomeWallpaper()
}
