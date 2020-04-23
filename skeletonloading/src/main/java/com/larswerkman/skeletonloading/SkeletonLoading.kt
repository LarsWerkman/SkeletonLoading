package com.larswerkman.skeletonloading

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
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

        return SkeletonBinder({
            drawable.constantState?.newDrawable()?.also {
                animation?.progress?.update(it)
            } ?: drawable
        }, builder.views).also { binders += it }
    }

    fun register(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        animation?.start { progress ->
            progress.update(drawable)

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