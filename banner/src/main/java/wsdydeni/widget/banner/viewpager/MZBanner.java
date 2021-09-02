package wsdydeni.widget.banner.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;

import wsdydeni.widget.banner.ViscousFluidInterpolator;

public class MZBanner extends ViewPager implements ViewPager.OnPageChangeListener {

    private final MZBannerAdapter bannerAdapter = new MZBannerAdapter();

    public void setDataList(ArrayList<String> mDataList) {
        bannerAdapter.setDataList(mDataList);
        if(mDataList.size() > 1) {
            setCurrentItem(Integer.MAX_VALUE / 2 - ((Integer.MAX_VALUE / 2) % mDataList.size()) + 1,false);
        }
    }

    public void setOnItemClickListener(MZBannerAdapter.OnItemClickListener mOnItemClickListener) {
        bannerAdapter.setOnItemClickListener(mOnItemClickListener);
    }

    private void init() {
        setAdapter(bannerAdapter);
        setPageMargin(80);
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller2 fixedSpeedScroller = new FixedSpeedScroller2(getContext(),new ViscousFluidInterpolator());
            field.set(this,fixedSpeedScroller);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final long AUTO_PLAY_DURATION = 4000L;
    public final AutoPlayRunnable autoPlayRunnable = new AutoPlayRunnable(this);

    private static class AutoPlayRunnable implements Runnable {

        private final WeakReference<MZBanner> mzBannerWeakReference;

        public AutoPlayRunnable(MZBanner mzBanner) {
            mzBannerWeakReference = new WeakReference<>(mzBanner);
        }

        @Override
        public void run() {
            MZBanner mzBanner = mzBannerWeakReference.get();
            if(mzBanner != null && mzBanner.bannerAdapter.getCount() > 1) {
                mzBanner.setCurrentItem(mzBanner.getCurrentItem() + 1);
                mzBanner.postDelayed(mzBanner.autoPlayRunnable,MZBanner.AUTO_PLAY_DURATION);
            }
        }
    }

    private boolean isLooping = false;

    public void startLoop() {
        if(!isLooping  && bannerAdapter.getDataListSize() > 0) {
            postDelayed(autoPlayRunnable,AUTO_PLAY_DURATION);
            isLooping = true;
        }
    }

    public void stopLoop() {
        if(isLooping) {
            removeCallbacks(autoPlayRunnable);
            isLooping = false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
            startLoop();
        } else if (action == MotionEvent.ACTION_DOWN) {
            stopLoop();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        int listSize = bannerAdapter.getDataListSize();
        int realPosition;
        if(listSize == 0) {
            realPosition =  0;
        } else {
            realPosition = (position - 1 + listSize) % listSize;
        }
        if(listSize > 0) {
            super.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        int listSize = bannerAdapter.getCount();
        int realPosition;
        if(listSize == 0) {
            realPosition =  0;
        } else {
            realPosition = (position - 1 + listSize) % listSize;
        }
        if(listSize > 0 && position == 0 || position == Integer.MAX_VALUE - 1) {
            if(bannerAdapter.getDataListSize() > 1) {
                setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % bannerAdapter.getDataListSize() + 1 + realPosition, true);
            }else {
                setCurrentItem(realPosition,true);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public MZBanner(@NonNull Context context) {
        super(context);
        init();
    }

    public MZBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
}
