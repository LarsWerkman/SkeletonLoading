package com.larswerkman.skeletonloading

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import java.util.concurrent.TimeUnit

/**
 * Abstraction for an animation that can be applied to [ISkeletonView]'s.
 *
 * @see ISkeletonView
 * @param start value used where the animation should start from
 * @param end value used where the animation will end at
 * @param duration pair denoting the duration of the animation
 * @param interpolator defines the rate of change of the animation
 * @param repeat if the animation should repeat forever
 */
abstract class SkeletonAnimation(
    private val start: Float,
    private val end: Float,
    private val duration: Pair<Long, TimeUnit> = Pair(1, TimeUnit.SECONDS),
    private val interpolator: TimeInterpolator? = null,
    private val repeat: Boolean = true
) {

    private var animator: ValueAnimator? = null

    /**
     * The current progress of the animation
     */
    val progress = Progress(start)

    private fun create() {
        animator = ValueAnimator.ofFloat(start, end).also {
            it.interpolator = interpolator
            it.duration = duration.second.toMillis(duration.first)
            it.repeatMode = ValueAnimator.REVERSE
            it.repeatCount = if (repeat) ValueAnimator.INFINITE else 0
        }
    }

    /**
     * Starts the animation if the animation isn't already running
     *
     * @param update called on every animation update with the new progress
     */
    fun start(update: (progress: Progress) -> Unit) {
        animator ?: create()

        animator?.addUpdateListener {
            progress.progress = it.animatedValue as Float
            update(progress)
        }

        animator?.let {
            if (!it.isRunning) {
                it.start()
            }
        }
    }

    /**
     * Stops the current animation and removes all update listeners
     */
    fun stop() {
        animator?.removeAllUpdateListeners()
        animator?.end()
    }

    /**
     * Adds the desired effect of the animation to the drawable for the current progress.
     * This will be called for every view on every animation step update,
     * so preferably the implementation is kept as light as possible.
     *
     * @param drawable that needs to be animated
     * @param progress progress of the animation
     */
    abstract fun animate(drawable: Drawable, progress: Float)

    /**
     * Animation Progress wrapping class
     */
    inner class Progress internal constructor(internal var progress: Float = 0f) {

        /**
         * Update the drawable for the current progress with the Animation state
         *
         * @param drawable that needs to be animated
         */
        fun update(drawable: Drawable) {
            animate(drawable, progress)
        }
    }
}