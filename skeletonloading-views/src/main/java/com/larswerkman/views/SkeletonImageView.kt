package com.larswerkman.views

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.larswerkman.skeletonloading.ISkeletonView
import com.larswerkman.skeletonloading.SkeletonAnimation
import com.larswerkman.views.internal.RoundedDrawableWrapper

/**
 * Convenient method of creating a [SkeletonImageView] instance for a [ImageView]
 *
 * @param radius extra radius that can be added to the skeleton drawable
 */
fun ImageView.skeleton(radius: Float = 0F): ISkeletonView {
    return SkeletonImageView(this, radius)
}

/**
 * Basic skeleton implementation for [ImageView]'s
 *
 * @param view to be put in a loading state
 * @param radius extra radius that can be added to the skeleton drawable
 */
class SkeletonImageView(
    private val view: ImageView,
    private val radius: Float = 0F
) : ISkeletonView {

    private lateinit var drawable: Drawable

    /**
     * state of the [ImageView] before the loading state is shown.
     */
    private var image: Drawable? = null

    override fun setup(drawable: Drawable) {
        this.drawable =
            if (radius > 0)
                RoundedDrawableWrapper(drawable, radius)
            else
                drawable
    }

    override fun show() {
        image = view.drawable
        view.setImageDrawable(drawable)
    }

    override fun hide() {
        if (view.drawable == drawable) {
            view.setImageDrawable(image)
        }
    }

    override fun animate(progress: SkeletonAnimation.Progress) {
        progress.update(drawable)
        view.invalidateDrawable(drawable)
    }
}