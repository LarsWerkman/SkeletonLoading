package com.larswerkman.skeletonloading

import android.graphics.drawable.Drawable

/**
 * Binds a group of [ISkeletonView]'s that will be *shown* and *hidden* together
 * to a [SkeletonLoading] instance. A *SkeletonLoading* can have multiple
 *
 * @see ISkeletonView
 * @see SkeletonLoading
 * @param drawable function to retrieve a new instance of the loading drawable
 * @param views collections of views that will be bound together
 */
class SkeletonBinder internal constructor(
    private val drawable: () -> Drawable,
    private val views: MutableList<ISkeletonView>
) {

    /**
     * Returns whether the binder is currently showing its views.
     */
    var isShowing = false
        private set

    /**
     * Returns whether the binder has been unbound.
     * Meaning all reference to its views are cleared and cannot be shown again.
     */
    var isUnbound = false
        private set

    init {
        views.forEach {
            it.setup(drawable())
        }
    }

    /**
     * Trigger the *animate* on all bound views regardless if the views isShowing
     *
     * @see ISkeletonView.animate
     * @see isShowing
     * @param progress current progress of the animation
     */
    internal fun update(progress: SkeletonAnimation.Progress) {
        views.forEach {
            it.animate(progress)
        }
    }

    /**
     * Show all bound views for loading
     */
    fun show() {
        if (isShowing) {
            return
        }

        views.forEach {
            it.show()
        }

        isShowing = true
    }

    /**
     * Hide all bound views for loading and restores their state
     */
    fun hide() {
        if (!isShowing) {
            return
        }

        views.forEach {
            it.hide()
        }

        isShowing = false
    }

    /**
     * Hide all views and remove their references to prevent memory leaks.
     * This will be called by the *SkeletonLoading* instance if it's registered to the lifecycle of your app.
     *
     * @see hide
     * @see SkeletonLoading.register
     */
    fun unbind() {
        hide()
        views.clear()

        isUnbound = true
    }

    /**
     * Builder used to construct the list of views that need to be bound.
     */
    class Builder constructor(val views: ArrayList<ISkeletonView> = arrayListOf()) {

        /**
         * Add the *ISkeletonView* view to the list of to be bound views.
         *
         * @param view view that needs to be bound
         */
        fun ISkeletonView.bind() {
            views += this
        }
    }
}
