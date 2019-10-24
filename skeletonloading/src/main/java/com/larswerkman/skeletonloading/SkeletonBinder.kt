package com.larswerkman.skeletonloading

import android.graphics.drawable.Drawable

class SkeletonBinder internal constructor(
    private val style: Drawable,
    private val views: MutableList<SkeletonView>
) {

    var isShowing = false
        get

    init {
        views.forEach {
            it.setup(style)
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
    }

    data class Builder internal constructor(val views: ArrayList<SkeletonView> = arrayListOf()) {
        fun bind(view: SkeletonView?) = apply {
            view?.let {
                views += it
            }
        }
    }
}
