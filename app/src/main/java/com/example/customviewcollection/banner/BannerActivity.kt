package com.example.customviewcollection.banner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.customviewcollection.R
import kotlinx.android.synthetic.main.activity_banner.*

class BannerActivity : AppCompatActivity() {

    private val list = listOf(
        BannerInfo("https://c-ssl.duitang.com/uploads/item/201604/17/20160417130700_RsHWL.thumb.700_0.jpeg"),
        BannerInfo("https://c-ssl.duitang.com/uploads/item/201708/11/20170811063029_82nif.thumb.700_0.jpeg"),
        BannerInfo("https://c-ssl.duitang.com/uploads/item/201709/18/20170918230840_amiRJ.thumb.700_0.jpeg"),
        BannerInfo("https://c-ssl.duitang.com/uploads/item/201607/28/20160728233536_kNhEj.thumb.700_0.jpeg"),
        BannerInfo("https://c-ssl.duitang.com/uploads/item/201604/06/20160406124724_iBUne.thumb.700_0.jpeg"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)
        banner.setAdapter(BannerAdapter().apply {
            setOnClickListener { position ->
                Toast.makeText(this@BannerActivity,"点击位置: $position", Toast.LENGTH_SHORT).show()
            }
        })
        banner.setData(list)

    }
}
