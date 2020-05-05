package com.example.customviewcollection.slidepuzzle

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.customviewcollection.R
import kotlinx.android.synthetic.main.activity_slide_puzzle.*

class SlidePuzzleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_puzzle)
        slide_puzzle.setBitmap(BitmapFactory.decodeResource(resources,R.drawable.ic_slide_puzzle_test))
        slide_puzzle.setOnVerifyListener {
            Toast.makeText(this,if(it) "验证成功" else "验证失败", Toast.LENGTH_SHORT).show()
        }
    }

}
