package com.example.customviewcollection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.customviewcollection.loadingview.LoadingDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button1.setOnClickListener {
            val dialog = LoadingDialog(this)
            dialog.show("加载中")
        }
    }

}
