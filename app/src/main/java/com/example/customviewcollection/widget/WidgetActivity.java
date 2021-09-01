package com.example.customviewcollection.widget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.customviewcollection.R;

public class WidgetActivity extends AppCompatActivity {

    private Handler handler;
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == 0) {
                    progress += 1;
                    if(progress < 100) {
                        handler.sendEmptyMessageDelayed(0,1000L);
                    }
                    ((CircleProgressBar) findViewById(R.id.circle_progress_view)).setProgress(progress);
                }
            }
        };
        handler.sendEmptyMessageDelayed(0,1000L);
    }
}