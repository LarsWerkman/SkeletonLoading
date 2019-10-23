package com.larswerkman.views;

import android.graphics.drawable.Drawable
import android.widget.TextView
import com.larswerkman.skeletonloading.SkeletonAnimation
import com.larswerkman.skeletonloading.SkeletonView

fun TextView.skeleton(width: SkeletonTextView.TextWidth, value: Double): SkeletonView {
    return SkeletonTextView(this, width, value)
}

class SkeletonTextView(
    private val view: TextView,
    private val width: TextWidth,
    private val value: Double
) : SkeletonView {
    override fun setup(drawable: Drawable) {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun show() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hide() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun animate(progress: SkeletonAnimation.Progress) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    enum class TextWidth {
        FULL_LINES, WEIGHT, CHARACTERS
    }
}
