package com.larswerkman.views

import android.graphics.drawable.Drawable
import android.view.View
import com.larswerkman.skeletonloading.ISkeletonView
import com.larswerkman.skeletonloading.SkeletonAnimation
import com.larswerkman.views.internal.RoundedDrawableWrapper

/**
 * Convenient method of creating a [SkeletonVisibilityView] instance for a [View]
 *
 * @param visibility One of [View.VISIBLE], [View.INVISIBLE] or [View.GONE]
 */
fun View.skeleton(visibility: Int): ISkeletonView {
    return SkeletonVisibilityView(this, visibility)
}

/**
 * Base skeleton implementation for [View]'s to set the Visibility based on the loading state
 *
 * @param view to be put in a loading state
 * @param visibility One of [View.VISIBLE], [View.INVISIBLE] or [View.GONE]
 */
class SkeletonVisibilityView(
    private val view: View,
    private val visibility: Int
) : ISkeletonView {

    private var oldVisibility: Int = View.VISIBLE

    override fun setup(drawable: Drawable) {
        oldVisibility = view.visibility
    }

    override fun show() {
        view.visibility = visibility
    }

    override fun hide() {
        view.visibility = oldVisibility
    }

    override fun animate(progress: SkeletonAnimation.Progress) {
    }

}

/**
 * Convenient method of creating a [SkeletonBackgroundView] instance for a [View]
 *
 * @param radius extra radius that can be added to the skeleton drawable
 */
fun View.skeleton(radius: Float = 0F): ISkeletonView {
    return SkeletonBackgroundView(this, radius)
}

/**
 * Base skeleton implementation for [View]'s for the background
 *
 * @param view to be put in a loading state
 * @param radius extra radius that can be added to the skeleton drawable
 */
class SkeletonBackgroundView(
    private val view: View,
    private val radius: Float = 0F
) : ISkeletonView {

    private lateinit var drawable: Drawable
    private var background: Drawable? = null

    override fun setup(drawable: Drawable) {
        this.drawable = if (radius > 0) RoundedDrawableWrapper(drawable, radius) else drawable
    }

    override fun show() {
        background = view.background
        view.background = drawable
    }

    override fun hide() {
        if (view.background == drawable) {
            view.background = background
        }
    }

    override fun animate(progress: SkeletonAnimation.Progress) {
        progress.update(drawable)
        view.invalidateDrawable(drawable)
    }
}