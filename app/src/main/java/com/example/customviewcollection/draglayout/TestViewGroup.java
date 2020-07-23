package com.example.customviewcollection.draglayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

public class TestViewGroup extends FrameLayout {

    private static final String TAG = "TestViewGroup";

    private ViewDragHelper mDragHelper;

    private boolean isMultiChoose = false;

    private ArrayList<View> viewList = new ArrayList<>();

    public boolean isMultiChoose() {
        return isMultiChoose;
    }

    public void setMultiChoose(boolean multiChoose) {
        isMultiChoose = multiChoose;
    }

    public TestViewGroup(Context context) {
        this(context,null,0);
    }

    public TestViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TestViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NotNull View child, int pointerId) {
                return true;
            }

            @Override
            public int getViewHorizontalDragRange(@NotNull View child) {
//                if(mDragHelper.getCapturedView() == child) {
                   return getMeasuredWidth() - child.getMeasuredWidth();
//                }else {
//                    return 0;
//                }
            }

            public int getViewVerticalDragRange(@NotNull View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public void onViewDragStateChanged(int state) {
                super.onViewDragStateChanged(state);
                Log.e(TAG,"onViewDragStateChanged STATE: " + state);
                if(state == ViewDragHelper.STATE_SETTLING) {
                    mDragHelper.abort();
                }
            }

            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                Log.e(TAG, "onViewPositionChanged " + "left:" + left + ",top:" + top + ",dx:" + dx + ",dy:" + dy );
//                invalidate();

            }

            @Override
            public int clampViewPositionHorizontal(@NotNull View child, int left, int dx) {
                if (left < 0) {
                    left = 0;
                } else if (left > (getMeasuredWidth() - child.getMeasuredWidth())) {
                    left = getMeasuredWidth() - child.getMeasuredWidth();
                }
                return left;
            }

            public int clampViewPositionVertical(@NotNull View child, int top, int dy) {
                if (top < 0) {
                    top = 0;
                } else if (top > getMeasuredHeight() - child.getMeasuredHeight()) {
                    top = getMeasuredHeight() - child.getMeasuredHeight();
                }
                return top;
            }

            @Override
            public void onViewReleased(@NotNull View releasedChild, float xvel, float yvel) {
                int centerLeft = getMeasuredWidth() / 2 - releasedChild.getMeasuredWidth() / 2;
                if (releasedChild.getLeft() < centerLeft) {
//                    mDragHelper.settleCapturedViewAt(0, releasedChild.getTop());
//                    ViewCompat.postInvalidateOnAnimation(TestViewGroup.this);
                    mDragHelper.smoothSlideViewTo(releasedChild, 0, releasedChild.getTop());
                } else {
//                    mDragHelper.settleCapturedViewAt(getMeasuredWidth() - releasedChild.getMeasuredWidth(), releasedChild.getTop());
                    mDragHelper.smoothSlideViewTo(releasedChild, getMeasuredWidth() - releasedChild.getMeasuredWidth(), releasedChild.getTop());
//                    ViewCompat.postInvalidateOnAnimation(TestViewGroup.this);
                }
                ViewCompat.postInvalidateOnAnimation(TestViewGroup.this);
//                this.onViewDragStateChanged(ViewDragHelper.STATE_SETTLING);
//                mDragHelper.settleCapturedViewAt(mDragOriLeft, mDragOriTop);
//                invalidate();
            }


        });


    }

    private float startX;
//    private long downTime;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = mDragHelper.shouldInterceptTouchEvent(ev);
        float curX = ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = curX;
//                downTime = ev.getDownTime();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG,"onInterceptTouchEvent space: " + String.format(Locale.CHINA,"%.2f",curX - startX));
                if(Math.abs(curX - startX) > mDragHelper.getTouchSlop()) result = true;
                break;
//            case MotionEvent.ACTION_UP:
//                long time = ev.getEventTime();
//                if(time - downTime > 1000) {
//                    result = true;
//                }
//                break;
        }
        Log.e(TAG,"onInterceptTouchEvent result: " + result);
        return result;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    int mVdhXOffset;
    int mVdhYOffset;

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mDragHelper != null) {
            if(mDragHelper.continueSettling(true)) {
                ViewCompat.postInvalidateOnAnimation(TestViewGroup.this);
            }else {
                if(mDragHelper.getCapturedView() != null) {
                    mVdhXOffset = mDragHelper.getCapturedView().getLeft();
                    mVdhYOffset = mDragHelper.getCapturedView().getTop();
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(null != mDragHelper) {
            if(mDragHelper.getCapturedView() != null) {
                Log.e(TAG,"onLayout");
                mDragHelper.getCapturedView().offsetLeftAndRight(mVdhXOffset);
                mDragHelper.getCapturedView().offsetTopAndBottom(mVdhYOffset);
            }
        }
    }
}

