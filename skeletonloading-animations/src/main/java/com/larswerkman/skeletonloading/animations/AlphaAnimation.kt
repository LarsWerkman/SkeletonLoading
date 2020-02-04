package com.larswerkman.skeletonloading.animations;

import android.animation.TimeInterpolator
import android.graphics.drawable.Drawable
import androidx.annotation.FloatRange
import com.larswerkman.skeletonloading.SkeletonAnimation
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

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