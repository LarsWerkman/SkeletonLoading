package com.larswerkman.views

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.Spannable
import android.text.style.LeadingMarginSpan
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.core.view.OneShotPreDrawListener
import androidx.core.view.doOnPreDraw
import com.larswerkman.skeletonloading.SkeletonAnimation
import com.larswerkman.skeletonloading.SkeletonView
import kotlin.math.ceil

fun TextView.skeleton(width: SkeletonTextView.TextWidth, value: Double): SkeletonView {
    return SkeletonTextView(this, width, value)
}

class SkeletonTextView(
    private val view: TextView,
    private val width: TextWidth,
    private val value: Double
) : SkeletonView {

    lateinit var onPreDraw: (view: View) -> Unit
    var listener: OneShotPreDrawListener? = null

    var text: CharSequence? = null

    override fun setup(drawable: Drawable) {
        onPreDraw = { draw(drawable) }
    }

    private fun draw(drawable: Drawable) {
        view.layout ?: view.onPreDraw()

        if (view.layout == null || view.paint == null) return

        val fontMetrics = view.paint.fontMetricsInt
        val textSize = view.paint.textSize

        view.text = buildSpannedString {
            when (width) {
                TextWidth.CHARACTERS -> {
                    val widthInPixels = calculateWidth(value, textSize)

                    append(
                        " ",
                        SkeletonTextSpan(drawable, widthInPixels, fontMetrics),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                TextWidth.LINES -> {
                    var weightedValue = value

                    val numberOfLines = ceil(weightedValue).toInt()
                    for (i in 1..numberOfLines) {
                        val widthInPixels = calculateWidth(weightedValue, textSize)

                        append(
                            if (i == numberOfLines) " " else " \n",
                            SkeletonTextSpan(drawable, widthInPixels, fontMetrics),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )

                        weightedValue -= 1.0
                    }
                }
            }
        }
    }

    private fun calculateWidth(value: Double, textSize: Float): Int {
        val widthInPixels = when (width) {
            TextWidth.CHARACTERS -> textSize * value
            TextWidth.LINES -> view.layout.width * value.coerceIn(0.0, 1.0)
        }

        return widthInPixels.toInt()
    }

    override fun show() {
        text = view.text

        if (::onPreDraw.isInitialized) {
            listener = view.doOnPreDraw(onPreDraw)
            view.invalidate()
        }
    }

    override fun hide() {
        listener?.removeListener()

        view.text = text
    }

    override fun animate(progress: SkeletonAnimation.Progress) {
        view.invalidate()
    }

    enum class TextWidth {
        LINES, CHARACTERS
    }

    private inner class SkeletonTextSpan(
        val drawable: Drawable,
        val width: Int,
        val fontMetrics: Paint.FontMetricsInt
    ) :
        LeadingMarginSpan {

        var startOffset = 0

        init {
            val inset = view.layout.width - width
            val gravity = Gravity.getAbsoluteGravity(view.gravity, view.layoutDirection)

            if (inset > 0) {
                if (gravity and Gravity.HORIZONTAL_GRAVITY_MASK == Gravity.RIGHT) {
                    startOffset = inset
                } else if (gravity and Gravity.HORIZONTAL_GRAVITY_MASK == Gravity.CENTER_HORIZONTAL) {
                    startOffset = inset / 2
                }
            }
        }

        override fun getLeadingMargin(first: Boolean): Int {
            return width
        }

        override fun drawLeadingMargin(
            c: Canvas, p: Paint, x: Int, dir: Int,
            top: Int, baseline: Int, bottom: Int,
            text: CharSequence, start: Int, end: Int,
            first: Boolean, layout: Layout
        ) {
            drawable.setBounds(
                x + startOffset,
                baseline + fontMetrics.ascent,
                x + width + startOffset,
                baseline
            )

            drawable.draw(c)
        }
    }
}
