package at.florianschuster.byw

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.core.net.toUri
import com.hoko.blur.HokoBlur
import java.io.File
import kotlin.math.roundToInt

fun Context.cache(bitmap: Bitmap): Uri {
    val cacheFile = File(cacheDir, "${System.currentTimeMillis()}.jpg")
    cacheFile.outputStream().use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    }
    return cacheFile.toUri()
}

fun Context.storeInternal(bitmap: Bitmap): Uri {
    val cacheFile = File(filesDir, "${System.currentTimeMillis()}.jpg")
    cacheFile.outputStream().use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    }
    return cacheFile.toUri()
}

fun Context.bitmap(uri: Uri): Bitmap {
    val contentResolver = applicationContext.contentResolver
    val source = ImageDecoder.createSource(contentResolver, uri)
    return ImageDecoder.decodeBitmap(
        source
    ) { decoder, _, _ ->
        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
        decoder.isMutableRequired = true
    }
}

fun Context.blurredBitmap(uri: Uri, radius: Float): Bitmap {
    val original = bitmap(uri)
    val iterations = (if (radius <= 25) 1f else radius / 25f).roundToInt()
    val blurRadiusPerIteration = if (radius <= 25) radius.toInt() else 25
    var output = original
    repeat(iterations) {
        output = HokoBlur.with(this)
            .scheme(HokoBlur.SCHEME_NATIVE)
            .radius(blurRadiusPerIteration)
            .blur(output)
    }
    return output
}
