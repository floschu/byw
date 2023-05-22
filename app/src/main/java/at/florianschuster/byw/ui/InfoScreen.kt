package at.florianschuster.byw.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import at.florianschuster.byw.R

@Composable
fun InfoScreen(
    onSendFeedback: () -> Unit,
    onShowDevWebsite: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .paint(
                            painterResource(id = R.mipmap.ic_launcher_foreground),
                            contentScale = ContentScale.FillBounds
                        )
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = stringResource(id = R.string.info_title),
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    onClick = {
                        onDismiss()
                        onSendFeedback()
                    }
                ) {
                    Text(text = stringResource(id = R.string.info_button_send_feedback))
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    onClick = {
                        onDismiss()
                        onShowDevWebsite()
                    }
                ) {
                    Text(text = stringResource(id = R.string.info_button_made_by_me))
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private const val devUrl = "https://florianschuster.at/"

fun Activity.openInfoCustomTab() {
    val customTab = CustomTabsIntent.Builder().apply {
        setShowTitle(true)
        setInstantAppsEnabled(true)
    }.build()
    runCatching {
        customTab.launchUrl(this, Uri.parse(devUrl))
    }.onFailure {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(devUrl))
        startActivity(intent)
    }
}

private const val feedbackEmailAddress = "blur.your.wallpaper@gmail.com"

fun Activity.sendFeedbackEmail() {
    try {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$feedbackEmailAddress")
            putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.feedback_email_subject))
        }
        startActivity(
            Intent.createChooser(
                emailIntent,
                resources.getString(R.string.feedback_email_chooser_title)
            )
        )
    } catch (ex: ActivityNotFoundException) {
        Toast.makeText(
            this,
            resources.getString(R.string.feedback_email_no_app),
            Toast.LENGTH_LONG
        ).show()
    }
}
