package com.larswerkman.skeletonloading.animations;

import android.animation.TimeInterpolator
import android.graphics.drawable.Drawable
import androidx.annotation.FloatRange
import com.larswerkman.skeletonloading.SkeletonAnimation
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

/**
 * Animates the skeleton loading drawables alpha value.
 *
 * @param start value between 0 ... 1 to which it will animate from
 * @param end value between 1 ... 0 to which it will animate to
 * @param duration pair denoting the duration of the animation
 * @param interpolator defines the rate of change of the animation
 * @param repeat if the animation should repeat forever
 */
class AlphaAnimation(
    @FloatRange(from = 0.0, to = 1.0) start: Float,
    @FloatRange(from = 0.0, to = 1.0) end: Float,
    duration: Pair<Long, TimeUnit> = Pair(1, TimeUnit.SECONDS),
    interpolator: TimeInterpolator? = null,
    repeat: Boolean = true
) : SkeletonAnimation(start, end, duration, interpolator, repeat) {

    override fun animate(drawable: Drawable, progress: Float) {
        drawable.alpha = (progress * 255f).roundToInt()
    }

}