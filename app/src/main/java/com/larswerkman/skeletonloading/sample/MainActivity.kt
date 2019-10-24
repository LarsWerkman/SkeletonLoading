package com.larswerkman.skeletonloading.sample

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.larswerkman.skeletonloading.SkeletonLoading
import com.larswerkman.skeletonloading.animations.AlphaAnimation
import com.larswerkman.views.SkeletonTextView
import com.larswerkman.views.skeleton

class MainActivity : AppCompatActivity() {

    private lateinit var loading: SkeletonLoading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loading = SkeletonLoading(this, R.drawable.skeleton, AlphaAnimation(.1f, .9f))
        loading.onCreate()

        val test = findViewById<TextView>(R.id.tvTest)


        val binder = loading.create {
            bind(test?.skeleton(SkeletonTextView.TextWidth.LINES, 1.5))
        }

        test.setOnClickListener {
            if(binder.isShowing) binder.hide() else binder.show()
        }

        binder.show()
    }


    override fun onDestroy() {
        super.onDestroy()
        loading.onDestroy()
    }
}
