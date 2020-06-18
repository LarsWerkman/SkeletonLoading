package com.larswerkman.skeletonloading.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.larswerkman.skeletonloading.SkeletonLoading
import com.larswerkman.skeletonloading.animations.AlphaAnimation
import com.larswerkman.views.SkeletonTextView
import com.larswerkman.views.skeleton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loading = SkeletonLoading(
            getDrawable(R.drawable.skeleton)!!,
            AlphaAnimation(.1f, .6f)
        )

        loading.register(this)

        val binder = loading.create {
            sample_text_view.skeleton(SkeletonTextView.TextWidth.LINES, 1.2).bind()
        }

        sample_text_view.setOnClickListener {
            if (binder.isShowing) binder.hide() else binder.show()
        }

        binder.show()
    }
}
