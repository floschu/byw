package at.florianschuster.byw.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.valentinilk.shimmer.shimmer

data class PhoneScreen(
    val width: Int,
    val height: Int
) {
    val ratio = width.toFloat() / height.toFloat()

    companion object {
        val DEFAULT = PhoneScreen(2160, 3840)
    }
}

fun Context.phoneScreen(): PhoneScreen {
    val metrics = resources.displayMetrics
    return PhoneScreen(metrics.widthPixels, metrics.heightPixels)
}

fun Modifier.phoneScreenRatio(input: PhoneScreen): Modifier {
    return this.then(aspectRatio(input.ratio))
}

@Composable
fun PhoneScreenImage(
    modifier: Modifier = Modifier,
    src: Any?,
    blurRadius: Float?,
    onClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val phoneScreen = remember(context) { context.phoneScreen() }
    if (src == null) {
        Box(
            modifier = modifier
                .shimmer()
                .phoneScreenRatio(phoneScreen)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.onSurface)
                .clickable(enabled = onClick != null) { onClick?.invoke() }
        )
    } else {
        AsyncImage(
            modifier = modifier
                .phoneScreenRatio(phoneScreen)
                .clip(MaterialTheme.shapes.medium)
                .graphicsLayer(
                    renderEffect = if (blurRadius == null) {
                        null
                    } else {
                        BlurEffect(blurRadius, blurRadius)
                    }
                )
                .clickable(enabled = onClick != null) { onClick?.invoke() },
            model = src,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}
