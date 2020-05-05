package com.example.customviewcollection.progressview

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.customviewcollection.R
import kotlinx.android.synthetic.main.activity_progress_view.*

class ProgressViewActivity : AppCompatActivity() {

    private val imageUrl = "https://p3.music.126.net/B79zcPS2VDkBYImlsgiasg==/109951163400425649.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_view)
        progress_view.setOnProgressListener { progress ->
            if(progress == progress_view.getMaxProgress()) {
                Glide.with(this).load(imageUrl).into(image_view)
            }
        }
        val animator =  ValueAnimator.ofInt(0,1000).setDuration(6000)
        animator.addUpdateListener { animation ->
            val currentValue = animation.animatedValue as Int
            progress_view.setProgress(currentValue)
        }
        animator.start()
    }
}
