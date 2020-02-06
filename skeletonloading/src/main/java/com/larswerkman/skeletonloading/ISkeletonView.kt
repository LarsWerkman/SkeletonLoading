package com.larswerkman.skeletonloading

import android.graphics.drawable.Drawable

interface ISkeletonView {

    fun setup(drawable: Drawable)

    fun show()

    fun hide()

    fun animate(progress: SkeletonAnimation.Progress)
}