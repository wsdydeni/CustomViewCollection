package com.example.customviewcollection.seekbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.customviewcollection.R
import kotlinx.android.synthetic.main.activity_seek_bar.*

class SeekBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seek_bar)
        verify_seekbar.setOnFinishListener {
            Toast.makeText(this,"OK", Toast.LENGTH_SHORT).show()
        }
    }
}
