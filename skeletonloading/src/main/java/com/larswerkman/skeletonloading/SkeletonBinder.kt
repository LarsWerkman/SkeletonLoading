package com.larswerkman.skeletonloading

import android.graphics.drawable.Drawable

class SkeletonBinder internal constructor(
    private val style: Drawable,
    private val views: MutableList<ISkeletonView>
) {

    var isShowing = false
        get

    var isUnbound = false
        get

    init {
        views.forEach {
            it.setup(style.constantState?.newDrawable() ?: style)
        }
    }

    internal fun update(progress: SkeletonAnimation.Progress) {
        views.forEach {
            it.animate(progress)
        }
    }

    fun show() {
        if (isShowing) {
            return
        }

        views.forEach {
            it.show()
        }

        isShowing = true
    }

    fun hide() {
        if (!isShowing) {
            return
        }

        views.forEach {
            it.hide()
        }

        isShowing = false
    }

    fun unbind() {
        hide()
        views.clear()

        isUnbound = true
    }

    data class Builder internal constructor(val views: ArrayList<ISkeletonView> = arrayListOf()) {
        fun bind(view: ISkeletonView?) = apply {
            view?.let {
                views += it
            }
        }
    }
}
