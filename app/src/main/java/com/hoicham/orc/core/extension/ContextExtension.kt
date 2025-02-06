package com.hoicham.orc.core.extension

import android.app.Activity
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.content.pm.ShortcutManager
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.provider.Settings
import android.telecom.TelecomManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.hoicham.orc.R
import com.hoicham.orc.core.utils.isOreoPlus
import com.hoicham.orc.core.utils.isRPlus
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()

val Context.getPowerManager: PowerManager get() = getSystemService(Context.POWER_SERVICE) as PowerManager
val Context.windowManager: WindowManager get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager
val Context.telecomManager: TelecomManager get() = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
val Context.notificationManager: NotificationManager get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
val Context.shortcutManager: ShortcutManager
    @RequiresApi(Build.VERSION_CODES.N_MR1) get() = getSystemService(ShortcutManager::class.java) as ShortcutManager

val Context.clipboardManager: ClipboardManager
    get() = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

fun Context.showToast(id: Int, length: Int = Toast.LENGTH_LONG) {
    showToast(getString(id), length)
}

private fun Context.showToastInternal(
    message: String,
    length: Int,
) {
    if (this is Activity && (isFinishing || isDestroyed)) {
        return
    }

    Toast.makeText(this, message, length).show()
}

fun Context.showToast(msg: String, length: Int = Toast.LENGTH_LONG) {
    try {
        if (isOnMainThread()) {
            showToastInternal(msg, length)
        } else {
            Handler(Looper.getMainLooper()).post {
                showToastInternal(msg, length)
            }
        }
    } catch (e: Exception) {
        Log.e("App", "toast: ${e.message}")
    }
}


fun Context.showErrorToast(msg: String, length: Int = Toast.LENGTH_LONG) {
    val error = getString(R.string.error)
    showToast(String.format(Locale.US, error, msg), length)
}

fun Context.showErrorToast(exception: Exception, length: Int = Toast.LENGTH_LONG) {
    showErrorToast(exception.toString(), length)
}

val Context.actionBarHeight: Int
    get() {
        val styledAttributes =
            theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        val actionBarHeight = styledAttributes.getDimension(0, 0f)
        styledAttributes.recycle()
        return actionBarHeight.toInt()
    }

fun Context.launchActivityIntent(intent: Intent) {
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        showToast(R.string.no_app_found)
    } catch (e: Exception) {
        showErrorToast(e)
    }
}

@Suppress("DEPRECATION")
val Context.usableScreenSize: Point
    get() {
        val size = Point()

        if (isRPlus()) {
            val metrics = windowManager.currentWindowMetrics
            val insets =
                metrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            size.x = metrics.bounds.width() - insets.left - insets.right
            size.y = metrics.bounds.height() - insets.top - insets.bottom
        } else {
            val display = windowManager.defaultDisplay
            val displayMetrics = DisplayMetrics()
            display.getMetrics(displayMetrics)

            size.x = displayMetrics.widthPixels
            size.y = displayMetrics.heightPixels
        }

        return size
    }

@Suppress("DEPRECATION")
val Context.realScreenSize: Point
    get() {
        val size = Point()

        if (isRPlus()) {
            val metrics = windowManager.currentWindowMetrics
            size.x = metrics.bounds.width()
            size.y = metrics.bounds.height()
        } else {
            val display = windowManager.defaultDisplay
            val displayMetrics = DisplayMetrics()
            display.getRealMetrics(displayMetrics)

            size.x = displayMetrics.widthPixels
            size.y = displayMetrics.heightPixels
        }

        return size
    }


fun Context.getProperBackgroundColor() = resources.getColor(R.color.you_background_color, theme)

fun Context.sendEmail(toEmail: String, feedBackString: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    val data: Uri = Uri.parse(
        "mailto:$toEmail?subject=$feedBackString&body="
    )
    intent.data = data
    try {
        startActivity(intent)
    } catch (ex: Exception) {
        showToast("Not have email app to send email!")
        ex.printStackTrace()
    }
}

fun Context.shareApp() {
    val subject = "Install app: "
    val sharingIntent = Intent(Intent.ACTION_SEND)
    sharingIntent.type = "text/plain"
    val shareBody = "https://play.google.com/store/apps/details?id=$packageName"
    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
    this.startActivity(Intent.createChooser(sharingIntent, "Share to"))
}

fun Context.openApplicationSettings() {
    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", packageName, null)
    }

    try {
        startActivity(intent)
    } catch (e: Exception) {
        showErrorToast(e)
    }
}

fun Context.openNotificationSettings() {
    val intent = if (isOreoPlus()) {
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        }
    } else {
        Intent(Settings.ACTION_SETTINGS)
    }

    try {
        startActivity(intent)
    } catch (e: Exception) {
        showErrorToast(e)
    }
}


/**
 * Represents animation durations specified in system resources.
 * The corresponding value can be retrieved with [Context.getSystemAnimationDuration] function.
 */
enum class SystemAnimationDuration {
    SHORT, MEDIUM, LONG,
}

/**
 * Returns [Duration] of the system animation specified in system resources.
 *
 * @param duration [SystemAnimationDuration] that represents the system animation duration
 * specified in system resources.
 */
fun Context.getSystemAnimationDuration(duration: SystemAnimationDuration): Duration {
    val resId = when (duration) {
        SystemAnimationDuration.SHORT -> android.R.integer.config_shortAnimTime
        SystemAnimationDuration.MEDIUM -> android.R.integer.config_mediumAnimTime
        SystemAnimationDuration.LONG -> android.R.integer.config_longAnimTime
    }
    return resources.getInteger(resId).milliseconds
}

/**
 * Read asset file content
 *
 * @param fileName Asset file name
 * @return String file content
 */
fun Context.readAssetFile(fileName: String): String {
    return assets.open(fileName).bufferedReader().use {
        it.readText()
    }
}