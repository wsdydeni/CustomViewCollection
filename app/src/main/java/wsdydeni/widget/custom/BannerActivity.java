package wsdydeni.widget.custom;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import java.util.ArrayList;
import wsdydeni.widget.banner.horizontal.HorizontalBanner;
import wsdydeni.widget.banner.viewpager.MZBanner;
import wsdydeni.widget.banner.viewpager.MZBannerAdapter;
import wsdydeni.widget.banner.viewpager2.Banner;
import wsdydeni.widget.banner.viewpager2.BannerAdapter;
import wsdydeni.widget.banner.viewpager2.BannerInfo;


public class BannerActivity extends AppCompatActivity {

    Banner banner;
    ArrayList<BannerInfo> dataList;

    MZBanner mzBanner;
    HorizontalBanner horizontalBanner;
    ArrayList<String> dataList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        initView();
        initData();

//        BannerAdapter bannerAdapter = new BannerAdapter();
//        bannerAdapter.setOnClickListener(integer -> {
//            Toast.makeText(BannerActivity.this, "点击了" + integer, Toast.LENGTH_SHORT).show();
//            return null;
//        });
//        banner.setAdapter(bannerAdapter);
//        banner.setData(dataList);

        mzBanner.setDataList(dataList2);
        mzBanner.setOnItemClickListener((position, link) -> Toast.makeText(this, "点击了" + position, Toast.LENGTH_SHORT).show());
        horizontalBanner.setDataList(dataList2);
        horizontalBanner.setItemClickListener((url, position) -> Toast.makeText(BannerActivity.this, "点击了" + position, Toast.LENGTH_SHORT).show());
    }

    private void initData() {
        dataList = new ArrayList<>();
        dataList.add(new BannerInfo("https://c-ssl.duitang.com/uploads/item/201604/17/20160417130700_RsHWL.thumb.700_0.jpeg"));
        dataList.add(new BannerInfo("https://c-ssl.duitang.com/uploads/item/201708/11/20170811063029_82nif.thumb.700_0.jpeg"));
        dataList.add(new BannerInfo("https://c-ssl.duitang.com/uploads/item/201709/18/20170918230840_amiRJ.thumb.700_0.jpeg"));
        dataList.add(new BannerInfo("https://c-ssl.duitang.com/uploads/item/201607/28/20160728233536_kNhEj.thumb.700_0.jpeg"));
        dataList.add(new BannerInfo("https://c-ssl.duitang.com/uploads/item/201604/06/20160406124724_iBUne.thumb.700_0.jpeg"));

        dataList2 = new ArrayList<>();
        dataList2.add("https://c-ssl.duitang.com/uploads/item/201604/17/20160417130700_RsHWL.thumb.700_0.jpeg");
        dataList2.add("https://c-ssl.duitang.com/uploads/item/201708/11/20170811063029_82nif.thumb.700_0.jpeg");
        dataList2.add("https://c-ssl.duitang.com/uploads/item/201709/18/20170918230840_amiRJ.thumb.700_0.jpeg");
        dataList2.add("https://c-ssl.duitang.com/uploads/item/201607/28/20160728233536_kNhEj.thumb.700_0.jpeg");
        dataList2.add("https://c-ssl.duitang.com/uploads/item/201604/06/20160406124724_iBUne.thumb.700_0.jpeg");
    }

    private void initView() {
//        banner = findViewById(R.id.viewpager2_banner);
        mzBanner = findViewById(R.id.viewpager_banner);
        horizontalBanner = findViewById(R.id.horizontal_banner);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mzBanner.startLoop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mzBanner.stopLoop();
    }
}