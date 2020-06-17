package com.larswerkman.skeletonloading

import android.graphics.drawable.Drawable

/**
 * The base interface of which all Skeleton-supporting views should build from.
 */
interface ISkeletonView {

    /**
     * Called when a new instance of [SkeletonBinder] is created,
     * this is only called once and should be used to prepare the view.
     *
     * @see SkeletonBinder
     * @param drawable a new instance specifically for use with this view
     */
    fun setup(drawable: Drawable)

    /**
     * Called when the view should *show* its loading state.
     *
     * It is generally also used to save the current state of view before loading.
     */
    fun show()

    /**
     * Called when the view should *hide* its loading state.
     *
     * It is generally also used to restore the saved state that was recorded
     * before it has showed the loading state.
     */
    fun hide()

    /**
     * Called on every step of the animation to animate the drawables used be the view
     */
    fun animate(progress: SkeletonAnimation.Progress)
}