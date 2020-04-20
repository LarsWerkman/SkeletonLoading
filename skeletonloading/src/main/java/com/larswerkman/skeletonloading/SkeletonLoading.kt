package com.larswerkman.skeletonloading

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.util.concurrent.CopyOnWriteArrayList

fun SkeletonLoading(
    context: Context,
    @DrawableRes style: Int,
    animation: SkeletonAnimation? = null
): SkeletonLoading {
    return SkeletonLoading(context.getDrawable(style)!!, animation)
}

class SkeletonLoading(
    private val drawable: Drawable,
    private val animation: SkeletonAnimation? = null
) : LifecycleObserver {

    private val binders = CopyOnWriteArrayList<SkeletonBinder>()

    fun create(block: SkeletonBinder.Builder.() -> Unit): SkeletonBinder {
        val builder = SkeletonBinder.Builder()
        block(builder)

        val binder = SkeletonBinder(drawable, builder.views)
        binders += binder

        return binder
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        animation?.start { progress ->
            binders.forEach { binder ->
                if (binder.isUnbound) {
                    binders.remove(binder)
                } else if (binder.isShowing) {
                    binder.update(progress)
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        animation?.stop()

        binders.forEach {
            it.unbind()
        }

        binders.clear()
    }
}