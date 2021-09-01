package wsdydeni.widget.custom.seekbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_seek_bar.*
import wsdydeni.widget.custom.R

class SeekBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seek_bar)
        verify_seekbar.setOnFinishListener {
            Toast.makeText(this,"OK", Toast.LENGTH_SHORT).show()
        }
    }
}
