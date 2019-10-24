package com.larswerkman.views

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.text.Layout
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.DrawableMarginSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
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

    var testDrawable: ShapeDrawable? = null

    var text: CharSequence? = null

    override fun setup(drawable: Drawable) {
        onPreDraw = { draw(drawable) }
    }

    private fun draw(drawable: Drawable) {
        view.layout ?: view.onPreDraw()

        if (view.layout == null || view.paint == null) return

        val fontMetrics = view.paint.fontMetricsInt
        val textSize = view.paint.textSize

        testDrawable = ShapeDrawable(RectShape())
        testDrawable?.paint?.color = Color.RED

        view.text = buildSpannedString {
            when (width) {
                TextWidth.CHARACTERS -> {
                    val widthInPixels = calculateWidth(value, textSize)

                    val textViewDrawable = createDrawable(
                        drawable, widthInPixels, fontMetrics, textSize.toInt()
                    )

                    drawable(textViewDrawable) {
                        append(" ")
                    }
                }

                TextWidth.LINES -> {
                    var weightedValue = value

                    val numberOfLines = ceil(weightedValue).toInt()
                    for (i in 1..numberOfLines) {
                        val widthInPixels = calculateWidth(weightedValue, textSize)


                        val textViewDrawable = createDrawable(
                            testDrawable!!, widthInPixels, fontMetrics, textSize.toInt()
                        )

                        append(
                            if (i == numberOfLines) " " else "\n",
                            DrawableMarginSpan(textViewDrawable), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
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

    private fun createDrawable(
        drawable: Drawable,
        widthInPixels: Int,
        fontMetrics: Paint.FontMetricsInt,
        textSize: Int
    ): Drawable {
        val height = textSize - fontMetrics.descent

        var insetLeft = 0
        var insetRight = 0

        val gravity = Gravity.getAbsoluteGravity(view.gravity, view.layoutDirection)
        val inset = view.layout.width - widthInPixels

        if (inset > 0) {
            if (gravity and Gravity.HORIZONTAL_GRAVITY_MASK == Gravity.RIGHT) {
                insetLeft = inset
            } else if (gravity and Gravity.HORIZONTAL_GRAVITY_MASK == Gravity.CENTER_HORIZONTAL) {
                insetLeft = inset / 2
                insetRight = inset / 2
            }
        }

        return TextViewDrawable(
            drawable = drawable,
            width = widthInPixels,
            height = height,
            left = insetLeft,
            top = 0,
            right = insetRight,
            bottom = 0
        )
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
        if(testDrawable != null) {
            progress.update(testDrawable!!)
        }
        view.invalidate()
    }

    enum class TextWidth {
        LINES, CHARACTERS
    }

    private inline fun SpannableStringBuilder.drawable(
        drawable: Drawable,
        builderAction: SpannableStringBuilder.() -> Unit
    ) = inSpans(TextViewSpan(drawable), builderAction)

    private class TextViewDrawable(
        drawable: Drawable,
        val width: Int,
        val height: Int,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) : InsetDrawable(drawable, left, top, right, bottom) {

        override fun getIntrinsicWidth(): Int {
            return width
        }

        override fun getIntrinsicHeight(): Int {
            return height
        }
    }

    private class TextViewSpan(drawable: Drawable) : DrawableMarginSpan(drawable) {

        override fun drawLeadingMargin(
            c: Canvas,
            p: Paint,
            x: Int,
            dir: Int,
            top: Int,
            baseline: Int,
            bottom: Int,
            text: CharSequence,
            start: Int,
            end: Int,
            first: Boolean,
            layout: Layout
        ) {
            val bitmap = Bitmap.createBitmap(c.width, c.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            super.drawLeadingMargin(
                canvas,
                p,
                x,
                dir,
                top,
                baseline,
                bottom,
                text,
                start,
                end,
                first,
                layout
            )

            Log.i("Test", "test")
        }
    }
}
