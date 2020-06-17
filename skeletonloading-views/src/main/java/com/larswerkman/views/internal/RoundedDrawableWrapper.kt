package com.larswerkman.views.internal

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.graphics.drawable.Drawable

/**
 * A [DrawableWrapper] which clips the corners of the drawable when drawing with a certain radius.
 */
open class RoundedDrawableWrapper(
    drawable: Drawable,
    private val radius: Float
) : DrawableWrapper(drawable) {

    private val path = Path()

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.clipPath(path)
        super.draw(canvas)
        canvas.restore()
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)

        path.reset()
        path.addRoundRect(
            bounds.left.toFloat(),
            bounds.top.toFloat(),
            bounds.right.toFloat(),
            bounds.bottom.toFloat(),
            radius, radius,
            Path.Direction.CW
        )
    }
}