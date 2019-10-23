package com.larswerkman.skeletonloading

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes

fun SkeletonLoading(
    context: Context,
    @DrawableRes style: Int,
    animation: SkeletonAnimation?
): SkeletonLoading {
    return SkeletonLoading(context, context.getDrawable(style)!!, animation)
}

class SkeletonLoading(
    private val context: Context,
    private val style: Drawable,
    private val animation: SkeletonAnimation?
) {

    private val binders = arrayListOf<SkeletonBinder>()

    fun create(block: SkeletonBinder.Builder.() -> Unit): SkeletonBinder {
        val builder = SkeletonBinder.Builder()
        block(builder)

        val binder = SkeletonBinder(style, builder.views)
        binders += binder

        return binder
    }

    fun onCreate() {
        animation?.start { progress ->
            progress.update(style)

            binders.forEach { binder ->
                binder.update(progress)
            }
        }
    }

    fun onDestroy() {
        animation?.stop()
        binders.clear()
    }
}