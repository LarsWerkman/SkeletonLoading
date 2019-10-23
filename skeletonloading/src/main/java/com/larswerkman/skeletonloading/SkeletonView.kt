package com.larswerkman.skeletonloading

import android.graphics.drawable.Drawable

interface SkeletonView {

    fun setup(drawable: Drawable)

    fun show()

    fun hide()

    fun animate(progress: SkeletonAnimation.Progress)
}