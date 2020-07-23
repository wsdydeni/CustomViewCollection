package com.example.customviewcollection.draglayout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import java.util.*
import kotlin.math.abs

class TestViewGroup @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var mDragHelper: ViewDragHelper

    var list = ArrayList<Int>()
    var isMultiChoose = false

    private var startX = 0f

    //    private long downTime;
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var result = mDragHelper.shouldInterceptTouchEvent(ev)
        val curX = ev.x
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> startX = curX
            MotionEvent.ACTION_MOVE -> {
                Log.e(TAG, "onInterceptTouchEvent space: " + String.format(Locale.CHINA, "%.2f", curX - startX))
                if (abs(curX - startX) > mDragHelper.touchSlop) result = true
            }
        }
        Log.e(TAG, "onInterceptTouchEvent result: $result")
        return result
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        for (i in 0 until childCount) {
            getChildAt(i).tag = i.toString()
            getChildAt(i).setOnClickListener { v: View ->
                if (isMultiChoose) {
                    list.add(v.tag.toString().toInt())
                } else {
                    Toast.makeText(this.context, "旋转", Toast.LENGTH_SHORT).show()
                    v.rotation = Random().nextFloat()
                }
            }
        }
    }

    override fun performClick(): Boolean {
        Log.e(TAG,"performClick")
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        performClick()
        mDragHelper.processTouchEvent(event)
        return true
    }

    companion object {
        private const val TAG = "TestViewGroup"
    }

    init {
        mDragHelper = ViewDragHelper.create(this, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean { return true }

            override fun getViewHorizontalDragRange(child: View): Int {
                return measuredWidth - child.measuredWidth
            }

            override fun getViewVerticalDragRange(child: View): Int {
                return measuredHeight - child.measuredHeight
            }

            override fun onViewDragStateChanged(state: Int) {
                super.onViewDragStateChanged(state)
                Log.e(TAG, "onViewDragStateChanged STATE: $state")
                if (state == ViewDragHelper.STATE_SETTLING) {
                    mDragHelper.abort()
                }
            }

            override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
                super.onViewPositionChanged(changedView, left, top, dx, dy)
                Log.e(TAG, "onViewPositionChanged " + "left:" + left + "top:" + top + ",dx:" + dx + ",dy:" + dy)
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                var left1 = left
                if (left1 < 0) {
                    left1 = 0
                } else if (left1 > measuredWidth - child.measuredWidth) {
                    left1 = measuredWidth - child.measuredWidth
                }
                return left1
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                var top1 = top
                if (top1 < 0) {
                    top1 = 0
                } else if (top1 > measuredHeight - child.measuredHeight) {
                    top1 = measuredHeight - child.measuredHeight
                }
                return top1
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                val centerLeft = measuredWidth / 2 - releasedChild.measuredWidth / 2
                if (releasedChild.left < centerLeft) {
                    mDragHelper.smoothSlideViewTo(releasedChild, 0, releasedChild.top)
                } else {
                    mDragHelper.smoothSlideViewTo(releasedChild, measuredWidth - releasedChild.measuredWidth, releasedChild.top)
                }
                Log.e(TAG, "onViewReleased " + "left" + releasedChild.left + "top" + releasedChild.top + "right" + releasedChild.right + "bottom" + releasedChild.bottom)
                Log.e(TAG, "onViewReleased raction " + releasedChild.rotation)
                ViewCompat.postInvalidateOnAnimation(this@TestViewGroup)

            }
        })
    }
}
