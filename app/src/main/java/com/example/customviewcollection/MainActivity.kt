package com.example.customviewcollection

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.customviewcollection.draglayout.TestViewGroup

class MainActivity : AppCompatActivity() {

    private var chooseButton: Button? = null
    private var chooseDirectionButton: Button? = null
    private var alertDialog: AlertDialog? = null
    private var testViewGroup: TestViewGroup? = null
    private var isChoose = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createDialog(this)
        chooseButton = findViewById(R.id.choose_btn)
        testViewGroup = findViewById(R.id.layout_drag_content)
        chooseDirectionButton = findViewById(R.id.choose_direction_btn)
        chooseDirectionButton?.setOnClickListener { alertDialog?.show() }
        chooseButton?.setOnClickListener {
            isChoose = !isChoose
            testViewGroup?.isMultiChoose = isChoose
            chooseButton?.text = if (isChoose) "多选" else "选择"
            chooseDirectionButton?.visibility = if (isChoose) View.VISIBLE else View.GONE
        }
    }

    private fun createDialog(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.item_direction_layout, null)
        alertDialog = AlertDialog.Builder(context).setView(view).create()
        val textView1 = view.findViewById<TextView>(R.id.choose_30_btn)
        val textView2 = view.findViewById<TextView>(R.id.choose_60_btn)
        val textView3 = view.findViewById<TextView>(R.id.choose_180_btn)
        val textView4 = view.findViewById<TextView>(R.id.choose_270_btn)
        textView1.setOnClickListener {
            if (testViewGroup?.list!!.size > 0) {
                for (i in testViewGroup?.list!!.indices) {
                    testViewGroup?.getChildAt(testViewGroup?.list!![i])?.rotation = 30f
                }
            }
            Toast.makeText(context, "30", Toast.LENGTH_SHORT).show()
            alertDialog?.dismiss()
        }
        textView2.setOnClickListener {
            if (testViewGroup?.list!!.size > 0) {
                for (i in testViewGroup?.list!!.indices) {
                    testViewGroup?.getChildAt(testViewGroup?.list!![i])?.rotation = 60f
                }
            }
            Toast.makeText(context, "60", Toast.LENGTH_SHORT).show()
            alertDialog?.dismiss()
        }
        textView3.setOnClickListener {
            if (testViewGroup?.list!!.size > 0) {
                for (i in testViewGroup?.list!!.indices) {
                    testViewGroup?.getChildAt(testViewGroup?.list!![i])?.rotation = 180f
                }
            }
            Toast.makeText(context, "180", Toast.LENGTH_SHORT).show()
            alertDialog?.dismiss()
        }
        textView4.setOnClickListener {
            if (testViewGroup?.list!!.size > 0) {
                for (i in testViewGroup?.list!!.indices) {
                    testViewGroup?.getChildAt(testViewGroup?.list!![i])?.rotation = 270f
                }
            }
            Toast.makeText(context, "270", Toast.LENGTH_SHORT).show()
            alertDialog?.dismiss()
        }
    }

}
