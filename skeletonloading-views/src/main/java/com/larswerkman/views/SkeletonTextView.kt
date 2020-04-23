package com.larswerkman.views

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.style.ReplacementSpan
import android.view.View
import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.core.view.OneShotPreDrawListener
import androidx.core.view.doOnPreDraw
import com.larswerkman.skeletonloading.ISkeletonView
import com.larswerkman.skeletonloading.SkeletonAnimation
import kotlin.math.ceil

fun TextView.skeleton(width: SkeletonTextView.TextWidth, value: Double): ISkeletonView {
    return SkeletonTextView(this, width, value)
}

class SkeletonTextView(
    private val view: TextView,
    private val width: TextWidth,
    private val value: Double
) : ISkeletonView {

    private lateinit var onPreDraw: (view: View) -> Unit
    private lateinit var drawable: Drawable

    private var listener: OneShotPreDrawListener? = null
    private var text: CharSequence? = null

    override fun setup(drawable: Drawable) {
        this.drawable = drawable

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
                        SPACE,
                        SkeletonTextSpan(drawable, widthInPixels, fontMetrics),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                TextWidth.LINES -> {
                    var weightedValue = value

                    val numberOfLines = ceil(weightedValue).toInt()
                    for (i in 1..numberOfLines) {
                        val widthInPixels = calculateWidth(weightedValue, textSize)
                        val lastLine = i == numberOfLines

                        val text = if (lastLine) SPACE else LINE_BREAK

                        append(
                            text,
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
        return when (width) {
            TextWidth.CHARACTERS -> textSize * value
            TextWidth.LINES -> (view.width - view.totalPaddingStart - view.totalPaddingEnd) *
                    value.coerceIn(0.0, 1.0)
        }.toInt()
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
        progress.update(drawable)
        view.invalidate()
    }

    companion object {
        const val SPACE = " "
        const val LINE_BREAK = " \n"
    }

    enum class TextWidth {
        LINES, CHARACTERS
    }

    private inner class SkeletonTextSpan(
        val drawable: Drawable,
        val width: Int,
        val fontMetrics: Paint.FontMetricsInt
    ) : ReplacementSpan() {

        override fun getSize(
            paint: Paint,
            text: CharSequence?,
            start: Int,
            end: Int,
            fm: Paint.FontMetricsInt?
        ): Int {
            fm?.apply {
                top = fontMetrics.top
                bottom = fontMetrics.bottom
                ascent = fontMetrics.ascent
                descent = fontMetrics.descent
            }

            return width
        }

        override fun draw(
            canvas: Canvas,
            text: CharSequence?,
            start: Int,
            end: Int,
            x: Float,
            top: Int,
            y: Int,
            bottom: Int,
            paint: Paint
        ) {
            drawable.setBounds(
                x.toInt(),
                y + fontMetrics.ascent,
                x.toInt() + width,
                y
            )

            drawable.draw(canvas)
        }
    }
}
