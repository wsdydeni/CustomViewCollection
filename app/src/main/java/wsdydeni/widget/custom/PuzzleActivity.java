package wsdydeni.widget.custom;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import wsdydeni.widget.puzzle.SlidePuzzle;

public class PuzzleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        ((SlidePuzzle) findViewById(R.id.slide_puzzle)).setBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_slide_puzzle_test));
        ((SlidePuzzle) findViewById(R.id.slide_puzzle)).setOnVerifyListener(new Function1<Boolean, Unit>() {
            @Override
            public Unit invoke(Boolean aBoolean) {
                Toast.makeText(PuzzleActivity.this,aBoolean ? "验证成功" : "验证失败", Toast.LENGTH_SHORT).show();
                return null;
            }
        });
    }
}