package com.example.customviewcollection

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.customviewcollection.banner.BannerInfo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val list = arrayListOf(
            "https://c-ssl.duitang.com/uploads/item/201604/17/20160417130700_RsHWL.thumb.700_0.jpeg",
            "https://c-ssl.duitang.com/uploads/item/201708/11/20170811063029_82nif.thumb.700_0.jpeg",
            "https://c-ssl.duitang.com/uploads/item/201709/18/20170918230840_amiRJ.thumb.700_0.jpeg",
            "https://c-ssl.duitang.com/uploads/item/201607/28/20160728233536_kNhEj.thumb.700_0.jpeg",
            "https://c-ssl.duitang.com/uploads/item/201604/06/20160406124724_iBUne.thumb.700_0.jpeg")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val linkedHashMap = LinkedHashMap<String,String>()
//        linkedHashMap["https://c-ssl.duitang.com/uploads/item/201604/17/20160417130700_RsHWL.thumb.700_0.jpeg"] = "0"
//        linkedHashMap["https://c-ssl.duitang.com/uploads/item/201708/11/20170811063029_82nif.thumb.700_0.jpeg"] = "1"
//        linkedHashMap["https://c-ssl.duitang.com/uploads/item/201709/18/20170918230840_amiRJ.thumb.700_0.jpeg"] = "2"
//        linkedHashMap["https://c-ssl.duitang.com/uploads/item/201607/28/20160728233536_kNhEj.thumb.700_0.jpeg"] = "3"
//        linkedHashMap["https://c-ssl.duitang.com/uploads/item/201604/06/20160406124724_iBUne.thumb.700_0.jpeg"] = "4"
        banner.setDataList(list)
        banner.setOnItemClickListener { position, link ->
            Toast.makeText(this, "position:  $position link:  $link", Toast.LENGTH_SHORT).show()
        }
        banner.startLoop()
    }

}
