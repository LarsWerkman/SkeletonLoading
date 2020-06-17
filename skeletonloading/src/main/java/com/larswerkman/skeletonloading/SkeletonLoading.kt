package com.larswerkman.skeletonloading

import android.graphics.drawable.Drawable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Skeleton loading for Android views. Use this class to easily
 * switch views between loading and displaying data without changes to your layout.
 *
 * The loading state can be personalized by using a custom [Drawable]
 * with a optional [SkeletonAnimation].
 *
 * A [SkeletonLoading] instance must be attached to the lifecycle of an [android.app.Activity] or [android.app.Fragment],
 * it can be registered using the [register] function.
 *
 * @param drawable base drawable used to draw the skeleton state from.
 * @param animation optional animation to animate the drawable while loading.
 */
class SkeletonLoading(
    private val drawable: Drawable,
    private val animation: SkeletonAnimation? = null
) : LifecycleObserver {

    /**
     * Thread-safe [ArrayList] used to reference all the binders.
     */
    private val binders = CopyOnWriteArrayList<SkeletonBinder>()

    /**
     * Binds and sets-up a [SkeletonBinder] instance for loading.
     *
     * @param builder of views used to create a [SkeletonBinder] instance
     * @return [SkeletonBinder] instance which can be used to show and hide
     * the bound views with their loading state
     */
    fun add(builder: SkeletonBinder.Builder): SkeletonBinder {
        return SkeletonBinder({
            drawable.constantState?.newDrawable()?.also {
                animation?.progress?.update(it)
            } ?: drawable
        }, builder.views).also { binders += it }
    }

    /**
     * A convenient method to directly create and [add] a [SkeletonBinder.Builder] instance.
     */
    fun create(block: SkeletonBinder.Builder.() -> Unit): SkeletonBinder {
        return SkeletonBinder.Builder().let {
            block(it)
            add(it)
        }
    }

    /**
     * Register this instance to a [Lifecycle]
     *
     * @param owner to get the [Lifecycle] instance from
     */
    fun register(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    /**
     * Will start the [SkeletonAnimation] if it exists.
     *
     * This must be called directly if this instance is not registered
     * to any [Lifecycle] using [register]
     */
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

    /**
     * Will start the [SkeletonAnimation] and [SkeletonBinder.unbind] all attached binders
     * and destroy their reference to prevent memory leaks.
     *
     * This must be called directly if this instance is not registered
     * to any [Lifecycle] using [register]
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        animation?.stop()

        binders.forEach {
            it.unbind()
        }

        binders.clear()
    }
}