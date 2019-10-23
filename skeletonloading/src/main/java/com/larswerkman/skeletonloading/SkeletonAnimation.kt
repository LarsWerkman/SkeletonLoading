package com.larswerkman.skeletonloading

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import android.view.View
import java.util.concurrent.TimeUnit


abstract class SkeletonAnimation(
    private val start: Float,
    private val end: Float,
    private val duration: Pair<Long, TimeUnit> = Pair(1, TimeUnit.SECONDS),
    private val interpolator: TimeInterpolator? = null,
    private val repeat: Boolean = true
) {

    private var animator: ValueAnimator? = null
    private val progress = Progress(start)

    private fun create() {
        animator = ValueAnimator.ofFloat(start, end)
        animator?.interpolator = interpolator
        animator?.duration = duration.second.toMillis(duration.first)
        animator?.repeatMode = ValueAnimator.REVERSE
        animator?.repeatCount = if (repeat) ValueAnimator.INFINITE else 0
    }

    fun start(update: (progress: Progress) -> Unit) {
        animator ?: create()

        animator?.addUpdateListener {
            progress.progress = it.animatedValue as Float
            update(progress)
        }

        if (animator?.isRunning == true) {
            animator?.start()
        }
    }

    fun stop() {
        animator?.end()
    }

    abstract fun animate(view: View, progress: Float)

    abstract fun animate(drawable: Drawable, progress: Float)

    inner class Progress internal constructor(
        internal var progress: Float = 0f
    ) {
        fun update(view: View) {
            animate(view, progress)
        }

        fun update(drawable: Drawable) {
            animate(drawable, progress)
        }
    }
}