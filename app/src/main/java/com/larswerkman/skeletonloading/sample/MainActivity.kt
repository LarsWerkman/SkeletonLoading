package com.larswerkman.skeletonloading.sample

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.larswerkman.skeletonloading.SkeletonLoading
import com.larswerkman.skeletonloading.animations.AlphaAnimation
import com.larswerkman.views.SkeletonTextView
import com.larswerkman.views.skeleton

class MainActivity : AppCompatActivity() {

    val loading = SkeletonLoading(this, R.drawable.skeleton, AlphaAnimation(.2f, .8f))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loading.onCreate()

        val textView: TextView? = null

        val binder = loading.create {
            bind(textView?.skeleton(SkeletonTextView.TextWidth.WEIGHT, 3.0))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loading.onDestroy()
    }
}
