package com.example.customviewcollection.mzbanner;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.customviewcollection.R;

import java.util.ArrayList;

public class MZBannerAdapter extends PagerAdapter {

    private ArrayList<String> mDataList = new ArrayList<>();

    public void setDataList(ArrayList<String> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    public int getDataListSize() {
        return mDataList.size();
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void click(int position,String link);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getCount() {
        return mDataList.size() > 1 ? Integer.MAX_VALUE : mDataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public float getPageWidth(int position) {
        return (float) 0.8;
    }

    @NonNull
    @Override
    @SuppressLint("ClickableViewAccessibility")
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView;
        if(mViewCaches.isEmpty()) {
            imageView = new ImageView(container.getContext());
        }else {
            imageView = (ImageView) mViewCaches.remove(0);
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setClipToOutline(true);
        imageView.setBackground(container.getContext().getResources().getDrawable(R.drawable.round_image,null));
        imageView.setOnTouchListener((v, event) -> {
            if(event.getAction() ==  MotionEvent.ACTION_DOWN) {
                v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(150L).start();
            }else if(event.getAction() == MotionEvent.ACTION_UP) {
                v.animate().scaleX(1f).scaleY(1f).setDuration(150L).start();
                v.performClick();
            }else if(event.getAction() == MotionEvent.ACTION_CANCEL) {
                v.animate().scaleX(1f).scaleY(1f).setDuration(150L).start();
            }
            return true;
        });
        int realPosition;
        if(mDataList.size() == 0) {
            realPosition = 0;
        }else {
            realPosition = (position - 1 + mDataList.size()) % mDataList.size();
        }
        Glide.with(container.getContext()).load(mDataList.get(realPosition)).into(imageView);
        if(mOnItemClickListener != null) {
            imageView.setOnClickListener(v -> {
                mOnItemClickListener.click(realPosition,mDataList.get(realPosition));
            });
        }
        container.addView(imageView);
        return imageView;
    }

    private final ArrayList<View> mViewCaches = new ArrayList<>();

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ImageView imageView = (ImageView) object;
        container.removeView(imageView);
        mViewCaches.add(imageView);
    }
}
