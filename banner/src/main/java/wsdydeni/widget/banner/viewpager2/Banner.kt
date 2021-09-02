package wsdydeni.widget.banner.viewpager2

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import wsdydeni.widget.banner.R

class Banner : RelativeLayout {

    private lateinit var mViewPager2: ViewPager2
    private lateinit var indicatorView: IndicatorView
    private var mAdapter : BannerAdapter? = null

    private var isLooping = false

    private val mRunnable = object : Runnable {
        override fun run() {
            if(mAdapter?.getListSize()!! > 1) {
                mViewPager2.currentItem = mViewPager2.currentItem + 1
                handler.postDelayed(this,5000)
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
        if (mAdapter?.getListSize()!! > 1) {
            mViewPager2.setCurrentItem(MAX_VALUE / 2 - MAX_VALUE / 2 % mAdapter!!.getListSize() + 1 + position, smoothScroll)
        } else {
            mViewPager2.setCurrentItem(position, smoothScroll)
        }
    }

    private fun initBannerData(data : List<BannerInfo>) {
        indicatorView.initView(
            data.size,0.6f,
            MAX_VALUE / 2 - ((MAX_VALUE / 2) % data.size) + 1,
            ResourcesCompat.getDrawable(resources, R.drawable.item_indicator_select,null),
            ResourcesCompat.getDrawable(resources, R.drawable.item_indicator_normal,null)
        )
        mViewPager2.adapter = mAdapter
        ScrollDurationManger.reflectLayoutManager(mViewPager2, 800)
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
        startLoop()
    }

    private fun startLoop() {
        if(!isLooping && mAdapter != null && mAdapter!!.getListSize() > 0) {
            handler.postDelayed(mRunnable,5000)
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
        inflate(context, R.layout.layout_banner,this)
        mViewPager2 = findViewById(R.id.viewpager2)
        indicatorView = findViewById(R.id.indicator_view)
    }

    init { initView() }

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context,attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyle : Int) : super(context,attributeSet,defStyle)

}