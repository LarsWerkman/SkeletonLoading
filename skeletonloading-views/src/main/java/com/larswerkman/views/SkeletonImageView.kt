package com.larswerkman.views

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.larswerkman.skeletonloading.SkeletonAnimation
import com.larswerkman.skeletonloading.SkeletonView
import com.larswerkman.views.internal.RoundedDrawableWrapper

fun ImageView.skeleton(radius: Float = 0F): SkeletonView {
    return SkeletonImageView(this, radius)
}

class SkeletonImageView(
    private val view: ImageView,
    private val radius: Float = 0F
) : SkeletonView {

    private lateinit var drawable: Drawable
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