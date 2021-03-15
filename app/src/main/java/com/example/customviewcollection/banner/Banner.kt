package com.example.customviewcollection.banner

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.customviewcollection.R
import kotlinx.android.synthetic.main.layout_banner.view.*
import kotlin.math.abs
import kotlin.math.max

/*
 * Date: 2020/4/29 12:26
 * Author: wsdydeni
 * Description: Banner
 */
class Banner : RelativeLayout {

    private lateinit var mViewPager2: ViewPager2
    private lateinit var indicatorView: IndicatorView
    private var mAdapter : BannerAdapter? = null

    private var isLooping = false

    private val mHandler = Handler()

    private val mRunnable = object : Runnable {
        override fun run() {
            if(mAdapter?.getListSize()!! > 1) {
                mViewPager2.currentItem = mViewPager2.currentItem + 1
                mHandler.postDelayed(this,5000)
            }
        }
    }

    private val onPageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    private val mOnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            onPageChangeCallback?.onPageScrollStateChanged(state)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            val listSize = mAdapter!!.getListSize()
            val realPos = if(listSize == 0 ) { 0 } else { (position - 1 + listSize) % listSize }
            if(listSize > 0) {
                onPageChangeCallback?.onPageScrolled(realPos,positionOffset,positionOffsetPixels)
                indicatorView.changeIndicator(realPos)
            }
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val listSize = mAdapter!!.getListSize()
            val realPos = if(listSize == 0 ) { 0 } else { (position - 1 + listSize) % listSize }
            if(listSize > 0 && position == 0 || position == MAX_VALUE - 1) {
                setCurrentItem(realPos,false)
                indicatorView.changeIndicator(realPos)
            }
            onPageChangeCallback?.onPageSelected(realPos)
        }
    }

    fun setAdapter(adapter : BannerAdapter) : Banner { //设置适配器
        mAdapter = adapter
        return this
    }

    fun setData(data : List<BannerInfo>) { //设置数据
        if(mAdapter == null) {
            throw NullPointerException("adapter for banner can't be empty")
        }
        mAdapter?.setData(data)
        initBannerData(data)
    }

    fun setCurrentItem(position : Int,smoothScroll : Boolean) {
        if(mAdapter?.getListSize()!! > 1) {
            mViewPager2.setCurrentItem(MAX_VALUE / 2 - MAX_VALUE / 2 % mAdapter!!.getListSize() + 1 + position, smoothScroll)
        }else {
            mViewPager2.setCurrentItem(position,smoothScroll)
        }
    }

    private fun initBannerData(data : List<BannerInfo>) {
        indicatorView.initView(data.size,0.6f,
            MAX_VALUE / 2 - ((MAX_VALUE / 2) % data.size) + 1,
            resources.getDrawable(R.drawable.item_indicator_select,null),
            resources.getDrawable(R.drawable.item_indicator_normal,null))
        mViewPager2.adapter = mAdapter
        ScrollDurationManger.reflectLayoutManager(mViewPager2,800)
        if(data.size > 1) {
            mViewPager2.setCurrentItem(MAX_VALUE / 2 - ((MAX_VALUE / 2) % data.size) + 1,false)
        }
        mViewPager2.clipToPadding = false
        mViewPager2.clipChildren = false
        mViewPager2.offscreenPageLimit = 1
        mViewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        mViewPager2.unregisterOnPageChangeCallback(mOnPageChangeCallback)
        mViewPager2.registerOnPageChangeCallback(mOnPageChangeCallback)
        mViewPager2.apply {
            val recyclerView= getChildAt(0) as RecyclerView
            recyclerView.apply {
                // setting padding on inner RecyclerView puts overscroll effect in the right place
                setPadding(0, 0, 120, 0)
                clipChildren = false
            }
        }
        val pageTransformer = CompositePageTransformer()
        pageTransformer.addTransformer(MarginPageTransformer(20))
//        pageTransformer.addTransformer { page, position ->
//            when {
//                position < -1 -> {
//                    page.scaleY = 0.9f
//                }
//                position <= 1 -> {
//                    val scale = max(0.9f,1 - abs(position))
//                    page.scaleY = scale
//                }
//                else -> {
//                    page.scaleY = 0.9f
//                }
//            }
//        }
        mViewPager2.setPageTransformer(pageTransformer)
        startLoop()
    }

    private fun startLoop() {
        if(!isLooping && mAdapter != null && mAdapter!!.getListSize() > 0) {
            mHandler.postDelayed(mRunnable,5000)
            isLooping = true
        }
    }

//    private fun stopLoop() {
//        if(isLooping) {
//            mHandler.removeCallbacks(mRunnable)
//            isLooping = false
//        }
//    }

    private fun initView() {
        inflate(context,R.layout.layout_banner,this)
        mViewPager2 = viewpager2
        indicatorView = indicator_view
    }

    init { initView() }

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context,attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyle : Int) : super(context,attributeSet,defStyle)

}