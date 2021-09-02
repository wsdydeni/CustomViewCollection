package wsdydeni.widget.custom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class WidgetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);
        findViewById(R.id.go_banner).setOnClickListener(v -> startActivity(new Intent(WidgetActivity.this,BannerActivity.class)));
        findViewById(R.id.go_progress).setOnClickListener(v -> startActivity(new Intent(WidgetActivity.this,ProgressActivity.class)));
        findViewById(R.id.go_puzzle).setOnClickListener(v -> startActivity(new Intent(WidgetActivity.this,PuzzleActivity.class)));
        findViewById(R.id.go_seekbar).setOnClickListener(v -> startActivity(new Intent(WidgetActivity.this,SeekBarActivity.class)));
    }
}