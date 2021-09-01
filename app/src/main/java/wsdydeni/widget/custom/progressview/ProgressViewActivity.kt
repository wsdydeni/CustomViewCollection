package wsdydeni.widget.custom.progressview

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.bumptech.glide.Glide
import com.example.customviewcollection.R
import kotlinx.android.synthetic.main.activity_progress_view.*

class ProgressViewActivity : AppCompatActivity() {

    private val imageUrl = "https://p3.music.126.net/B79zcPS2VDkBYImlsgiasg==/109951163400425649.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_view)
        progress_view.setMax(100)
        progress_view.setOnProgressListener { progress ->
            if(progress == progress_view.getMaxProgress()) {
                Glide.with(this).load(imageUrl).into(image_view)
                progress_view.visibility = View.GONE
            }
        }
        val animator =  ValueAnimator.ofInt(0,1000).setDuration(10000)
        animator.addUpdateListener { animation ->
            val currentValue = animation.animatedValue as Int
            progress_view.setProgress(currentValue)
        }
        animator.start()
        findViewById<Button>(R.id.restart).setOnClickListener {
            progress_view.visibility = View.VISIBLE
            image_view.setImageDrawable(null)
            progress_view.setProgress(0)
            if(animator.isRunning) {
                animator.cancel()
            }
            animator.start()
        }
    }
}
