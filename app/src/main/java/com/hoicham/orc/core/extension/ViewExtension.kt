package com.hoicham.orc.core.extension

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.Interpolator
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.time.Duration

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.disableView() {
    this.isClickable = false
    this.postDelayed({ this.isClickable = true }, 300)
}

class SafeClickListener(
    private val interval: Int = 300,
    private val onSafeClick: (View) -> Unit,
) : View.OnClickListener {
    private var lastClickTime: Long = 0L

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastClickTime < interval) {
            return
        }

        lastClickTime = SystemClock.elapsedRealtime()
        onSafeClick(v)
    }
}

fun View.setOnSafeClick(
    onSafeClick: (View) -> Unit,
) {
    setOnClickListener(SafeClickListener { v ->
        onSafeClick(v)
    })
}

fun View.setOnSafeClickListener(
    interval: Int,
    onSafeClick: (View) -> Unit,
) {
    setOnClickListener(SafeClickListener(interval) { v ->
        onSafeClick(v)
    })
}


/**
 * Animates the appearance or disappearance of this [View] with fade effect depending on
 * the [isVisible] value using the current state of the View as the initial state.
 *
 * @param isVisible indicates whether the View should appear or disappear with the fade animation.
 * @param duration [Duration] of the animation. Defaults to
 * `android.R.integer.config_shortAnimTime` value.
 * @param fadeOutTargetVisibility visibility that will be set when the disappearing animation ends.
 * Defaults to [View.GONE].
 * @param interpolator [TimeInterpolator] that will be used by the underlying animator. Defaults
 * to `null` that results in linear interpolation.
 * @param listener [Animator.AnimatorListener] that receives notifications from the animation.
 */
fun View.fade(
    isVisible: Boolean,
    duration: Duration = context.getSystemAnimationDuration(SystemAnimationDuration.SHORT),
    fadeOutTargetVisibility: Int = View.GONE,
    interpolator: TimeInterpolator? = null,
    listener: Animator.AnimatorListener? = null,
) {
    if (isVisible) {
        this.fadeIn(duration, interpolator, listener)
    } else {
        this.fadeOut(duration, fadeOutTargetVisibility, interpolator, listener)
    }
}

/**
 * Animates the appearance of this [View] with fade-in effect using the current state of the
 * View as the initial state.
 *
 * @param duration [Duration] of the animation. Defaults to
 * `android.R.integer.config_shortAnimTime` value.
 * @param interpolator [TimeInterpolator] that will be used by the underlying animator. Defaults
 * to `null` that results in linear interpolation.
 * @param listener [Animator.AnimatorListener] that receives notifications from the animation.
 */
fun View.fadeIn(
    duration: Duration = context.getSystemAnimationDuration(SystemAnimationDuration.SHORT),
    interpolator: TimeInterpolator? = null,
    listener: Animator.AnimatorListener? = null,
) {
    val currentProgress = this.alpha
    val remainingTimeMillis = duration.inWholeMilliseconds * (1 - currentProgress)

    this.animate().apply {
        // Cancel current animation
        cancel()
    }.setListener(listener).setInterpolator(interpolator)
        .setDuration(remainingTimeMillis.roundToLong()).withStartAction {
            toVisible()
        }.alpha(1f).start()
}

/**
 * Animates the disappearance of this [View] with fade-out effect using the current state of the
 * View as the initial state.
 *
 * @param duration [Duration] of the animation. Defaults to
 * `android.R.integer.config_shortAnimTime` value.
 * @param targetVisibility visibility that will be set when the animation ends.
 * Defaults to [View.GONE].
 * @param interpolator [TimeInterpolator] that will be used by the underlying animator. Defaults
 * to `null` that results in linear interpolation.
 * @param listener [Animator.AnimatorListener] that receives notifications from the animation.
 */
fun View.fadeOut(
    duration: Duration = context.getSystemAnimationDuration(SystemAnimationDuration.SHORT),
    targetVisibility: Int = View.GONE,
    interpolator: TimeInterpolator? = null,
    listener: Animator.AnimatorListener? = null,
) {
    val currentProgress = 1 - this.alpha
    val remainingTimeMillis = duration.inWholeMilliseconds * (1 - currentProgress)

    this.animate().apply { cancel() } // Cancel current animation
        .setListener(listener).setInterpolator(interpolator)
        .setDuration(remainingTimeMillis.roundToLong())
        .withEndAction { visibility = targetVisibility }.alpha(0f).start()
}

/**
 * Requests to show a keyboard with given [flags].
 *
 * @param flags additional operating flags required by [InputMethodManager.showSoftInput].
 */
fun View.showKeyboard(flags: Int = InputMethodManager.SHOW_IMPLICIT) {
    requestFocus()
    val inputMethodManager: InputMethodManager? =
        this.context.getSystemService<InputMethodManager>()
    Log.e("showKeyboard()", "${inputMethodManager?.showSoftInput(this, flags)}")
    inputMethodManager?.showSoftInput(this, flags)
}

/**
 * Requests to show a keyboard with given [flags].
 *
 * @param flags additional operating flags required by [InputMethodManager.showSoftInput].
 */
fun View.showKeyboardWithCallback(
    callback: (() -> Unit)? = null,
    flags: Int = InputMethodManager.SHOW_IMPLICIT,
) {
    Handler(Looper.getMainLooper()).postDelayed(
        {
            val downTime = SystemClock.uptimeMillis()
            val motionEventDown =
                MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, 0f, 0f, 0)
            val motionEventUp =
                MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_UP, 0f, 0f, 0)

            dispatchTouchEvent(motionEventDown)
            dispatchTouchEvent(motionEventUp)

            motionEventDown.recycle()
            motionEventUp.recycle()

            callback?.invoke()
        },
        250,
    )
}

/**
 * Requests to hide a keyboard with given [flags].
 *
 * @param flags additional operating flags required by [InputMethodManager.hideSoftInputFromWindow].
 */
fun View.hideKeyboard(flags: Int = 0) {
    val inputMethodManager = this.context.getSystemService<InputMethodManager>()
    inputMethodManager?.hideSoftInputFromWindow(this.windowToken, flags)
    clearFocus()
}

class CustomInterpolator : Interpolator {
    override fun getInterpolation(input: Float): Float {
        // Adjust this curve as needed for desired animation feel
        return ((input - 1).toDouble().pow(5.0) + 1).toFloat()
    }
}

fun View.popup() {
    val animator = ValueAnimator.ofFloat(0.75f, 1f)
    animator.addUpdateListener { animation: ValueAnimator ->
        val value = animation.animatedValue as Float
        this.scaleX = value
        this.scaleY = value
    }
    animator.interpolator = CustomInterpolator()
    animator.setDuration(300)
    animator.start()
}