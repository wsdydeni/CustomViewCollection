package wsdydeni.widget.custom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.lang.ref.WeakReference;
import wsdydeni.widget.progress.CircleProgressBar;
import wsdydeni.widget.progress.ProgressView;

public class ProgressActivity extends AppCompatActivity {

    private int progress = 0;
    private Handler mHandler;

    private String imageUrl = "https://p3.music.126.net/B79zcPS2VDkBYImlsgiasg==/109951163400425649.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        CircleProgressBar circleProgressBar = findViewById(R.id.circle_progress_bar);
        WeakReference<CircleProgressBar> circleProgressBarWeakReference = new WeakReference<>(circleProgressBar);
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == 0) {
                    progress += 1;
                    if(progress < 100) {
                        mHandler.sendEmptyMessageDelayed(0,1000L);
                    }
                    circleProgressBarWeakReference.get().setProgress(progress);
                }
            }
        };
        mHandler.sendEmptyMessageAtTime(0,1000L);

        ImageView imageView = findViewById(R.id.image_view);
        ProgressView progressView = findViewById(R.id.progress_view);
        progressView.setOnProgressListener(pro -> {
            if(pro == progressView.getMaxProgress()) {
                Glide.with(this).load(imageUrl).into(imageView);
                progressView.setVisibility(View.GONE);
            }
            return null;
        });
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,100).setDuration(10000L);
        valueAnimator.addUpdateListener(animation -> progressView.setProgress((Integer) animation.getAnimatedValue()));
        findViewById(R.id.restart).setOnClickListener(v -> {
            imageView.setImageDrawable(null);
            progressView.setVisibility(View.VISIBLE);
            if(valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.start();
        });
    }
}