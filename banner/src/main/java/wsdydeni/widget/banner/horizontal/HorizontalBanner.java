package wsdydeni.widget.banner.horizontal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.core.content.res.ResourcesCompat;
import com.bumptech.glide.Glide;
import org.jetbrains.annotations.NotNull;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;

import wsdydeni.widget.banner.R;
import wsdydeni.widget.banner.ViscousFluidInterpolator;

public class HorizontalBanner extends HorizontalScrollView implements View.OnClickListener {

    private boolean firstFlag = false;

    private LinearLayout container = null;
    private ViewGroup.LayoutParams imageLayoutParams = null;

    private int totalSize;
    private int currentPage;
    private ArrayList<String> mDataList;

    public static final long AUTO_PLAY_DURATION = 4000L;

    public final AutoPlayRunnable autoPlayRunnable = new AutoPlayRunnable(this);

    private VelocityTracker velocityTracker;

    private void init() {
        container = new LinearLayout(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        container.setLayoutParams(layoutParams);
        container.setOrientation(LinearLayout.HORIZONTAL);
        imageLayoutParams = new LinearLayout.LayoutParams(getWidth(),getHeight());
        this.addView(container);
        this.setSmoothScrollingEnabled(false);
        this.setHorizontalScrollBarEnabled(false);
        try {
            Field field = HorizontalScrollView.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller fixedSpeedScroller = new FixedSpeedScroller(getContext(),new ViscousFluidInterpolator());
            field.set(this,fixedSpeedScroller);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.setClipChildren(true);
    }

    @Override
    public void onClick(View v) {
        if(this.itemClickListener != null) {
            this.itemClickListener.itemClick(this.mDataList.get(currentPage),currentPage);
            postDelayed(autoPlayRunnable,AUTO_PLAY_DURATION);
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void itemClick(String url,int position);
    }

    private ItemClickListener itemClickListener;

    private static class AutoPlayRunnable implements Runnable {

        private final WeakReference<HorizontalBanner> horizontalBannerWeakReference;

        public AutoPlayRunnable(HorizontalBanner horizontalBanner) {
            horizontalBannerWeakReference = new WeakReference<>(horizontalBanner);
        }

        @Override
        public void run() {
            HorizontalBanner horizontalBanner = horizontalBannerWeakReference.get();
            if(horizontalBanner != null) {
                horizontalBanner.loadNextPage();
                horizontalBanner.postDelayed(horizontalBanner.autoPlayRunnable, AUTO_PLAY_DURATION);
            }
        }
    }

    public void startLoop() {
        this.postDelayed(autoPlayRunnable,AUTO_PLAY_DURATION);
    }

    public void stopLoop() {
        this.removeCallbacks(autoPlayRunnable);
    }

    public boolean setDataList(ArrayList<String> dataList) {
        this.mDataList = dataList;
        totalSize = dataList.size();
        this.container.removeAllViews();
        if(totalSize >= 3) {
            this.container.addView(getPositionView(totalSize - 1));
            for (int i = 0;i < totalSize - 1;i ++) {
                this.container.addView(getPositionView(i));
            }
            firstFlag = true;
            return true;
        }else {
           return false;
        }
    }

    public void loadNextPage() {
        if (currentPage == totalSize - 1) {
            currentPage = 0;
            scrollTo(0,0);
        }else {
            smoothScrollTo((currentPage + 1) * getWidth(),0);
            currentPage += 1;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        imageLayoutParams.width = getMeasuredWidth();
        imageLayoutParams.height = getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(firstFlag) {
            scrollTo(getWidth(),0);
            firstFlag = false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            removeCallbacks(autoPlayRunnable);
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                int width = getWidth();
                velocityTracker.computeCurrentVelocity(1000,2000);
                float speedX = velocityTracker.getXVelocity();
                int page;
                if(Math.abs(speedX) > 1000) {
                    page = scrollX / width;
                    if(speedX > 0) {
                        page -= 1;
                    }
                }else {
                    page = (int) Math.round(scrollX * 1.0 / width) - 1;
                }
                this.smoothScrollTo((page + 1) * width, 0);
                if(page < 0) {
                    if(currentPage == 0) {
                        currentPage = totalSize - 1;
                    }else {
                        currentPage -= 1;
                    }
                }else if(page > 0) {
                    if(currentPage == totalSize - 1) {
                        currentPage = 0;
                    }else {
                        currentPage += 1;
                    }
                }
                postDelayed(autoPlayRunnable,AUTO_PLAY_DURATION);
                return true;
        }
        return super.onTouchEvent(ev);
    }

    private ImageView getPositionView(int position) {
        ImageView imageView;
        imageView = new ImageView(getContext());
        imageView.setClipToOutline(true);
        imageView.setOnClickListener(this);
        imageView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.round_image,null));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(40,0,40,0);
        imageView.setLayoutParams(imageLayoutParams);
        Glide.with(getContext())
                .load(mDataList.get(position))
                .into(imageView);
        return imageView;
    }

    public HorizontalBanner(Context context) {
        super(context);
        init();
    }

    public HorizontalBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

}
