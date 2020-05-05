package com.example.customviewcollection.progressview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.customviewcollection.R
import com.example.customviewcollection.until.PixelUtils
import kotlin.math.min

/*
 * Date: 2020/4/29 13:24
 * Author: wsdydeni
 * Description: 圆形加载进度条
 */
class ProgressView : View {

    private var mWidth = 0  //view宽度
    private var mHeight = 0 //view高度
    private var mPaddingTop = 0 //上填充
    private var mPaddingBottom = 0 //下填充
    private var mPaddingLeft = 0 //左填充
    private var mPaddingRight = 0 //右填充
    private lateinit var mRect : RectF //绘制区域

    private val mPiePaint by lazy { Paint() } //内圆画笔
    private val mOuterCirclePaint by lazy { Paint() } //外圆画笔

    private var mMax = 0 //最大进度
    private var mProgress = 0 //当前进度

    private var mPieColor = 0 //内圆颜色
    private var mOuterCircleColor = 0 //外圆颜色
    private var mRadius : Float = 0f //外圆半径
    private var mOuterCircleBorderWidth : Float = 0f //外圆宽度

    private val defaultPieColor = Color.parseColor("#FFFFFF") //默认内圆颜色 白色
    private val defaultOutCircleColor = Color.parseColor("#FFFFFF") //默认外圆颜色 白色

    private val defaultMax = 100 //默认最大进度
    private val defaultProgress = 0  //当前默认进度
    private var defaultOutBounderSize : Float = 3F //默认外圆宽度 1dp

    private fun initView(attributeSet: AttributeSet?) {
        defaultOutBounderSize = PixelUtils.dp2px(context,1f).toFloat()
        if(attributeSet != null){
            val array = context.obtainStyledAttributes(attributeSet,R.styleable.ProgressView)
            mMax = array.getInt(R.styleable.ProgressView_max,defaultMax)
            mProgress = array.getInt(R.styleable.ProgressView_progress,defaultProgress)
            val resultOuterCircleColor = array.getColor(R.styleable.ProgressView_outer_circle_color,defaultOutCircleColor)
            mOuterCircleColor = if(resultOuterCircleColor != defaultOutCircleColor) {
                resultOuterCircleColor
            }else {
                val outerCircleResId = array.getResourceId(R.styleable.ProgressView_outer_circle_color,android.R.color.white)
                ContextCompat.getColor(context,outerCircleResId)
            }
            val resultPieCircleColor = array.getColor(R.styleable.ProgressView_pie_color,defaultPieColor)
            mPieColor = if(resultPieCircleColor != defaultPieColor) {
                resultPieCircleColor
            }else {
                val pieColorResId = array.getResourceId(R.styleable.ProgressView_outer_circle_color,android.R.color.white)
                ContextCompat.getColor(context,pieColorResId)
            }
            mOuterCircleBorderWidth = array.getDimension(R.styleable.ProgressView_outer_circle_border_width,defaultOutBounderSize)
            array.recycle()
        }else {
            mMax = defaultMax
            mProgress = defaultProgress
            mPieColor = defaultPieColor
            mOuterCircleColor = defaultOutCircleColor
            mOuterCircleBorderWidth = defaultOutBounderSize
        }
        mOuterCirclePaint.color = mOuterCircleColor
        mOuterCirclePaint.style = Paint.Style.STROKE
        mOuterCirclePaint.strokeWidth = mOuterCircleBorderWidth
        mOuterCirclePaint.isAntiAlias = true
        mPiePaint.color = mPieColor
        mPiePaint.style = Paint.Style.FILL
        mPiePaint.isAntiAlias = true
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setBackgroundColor(ContextCompat.getColor(context,android.R.color.transparent))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        mPaddingTop = paddingTop
        mPaddingBottom = paddingBottom
        mPaddingLeft = paddingLeft
        mPaddingRight = paddingRight
        val diameter = min(mWidth,mHeight) - mPaddingLeft - mPaddingRight
        mRadius = ((diameter / 2) * 0.98).toFloat()
        mRect = RectF(mPaddingLeft.toFloat(), mPaddingTop.toFloat(),
            (mWidth - mPaddingRight).toFloat(), (mHeight - mPaddingBottom).toFloat())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if(widthSize != heightSize) {
            val finalSize = min(widthSize,heightSize)
            widthSize = finalSize
            heightSize = finalSize
        }
        val defaultWidth = PixelUtils.dp2px(context,55f)
        val defaultHeight = PixelUtils.dp2px(context,55f)
        if(defaultWidth != MeasureSpec.EXACTLY && defaultHeight != MeasureSpec.EXACTLY) {
            if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(defaultWidth,defaultHeight)
            }else if(widthMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(defaultWidth,heightSize)
            }else if(heightMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(widthSize,defaultHeight)
            }
        }else {
            setMeasuredDimension(widthSize,heightSize)
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        var currentProgress = getProgress()
        if(currentProgress < 0) currentProgress = 0
        if(currentProgress > mMax) currentProgress = mMax
        canvas.drawCircle((mWidth/2).toFloat(), (mHeight/2).toFloat(),mRadius,mOuterCirclePaint)
        canvas.save()
        canvas.scale(0.90f,0.90f, (mWidth / 2).toFloat(), (mHeight / 2).toFloat())
        val angle = 360 * (currentProgress * 1.0f / getMaxProgress())
        canvas.drawArc(mRect, (-90).toFloat(),angle,true,mPiePaint)
        canvas.restore()
        _onProgress?.invoke(currentProgress)
    }

    private var _onProgress : ((Int) -> Unit)? = null

    private fun getProgress(): Int = mProgress

    fun getMaxProgress() : Int = mMax

    fun setOnProgressListener(listener : (Int) -> Unit) {
        _onProgress = listener
    }

    @Synchronized
    fun setProgress(progress: Int) {
        var mProgress1 = progress
        if (mProgress1 < 0) {
            mProgress1 = 0
        }
        mProgress = mProgress1
        postInvalidate()
    }

//    @Synchronized
//    fun setMax(max: Int) {
//        var max1 = max
//        if (max1 < 0) {
//            max1 = 0
//        }
//        mMax = max1
//        postInvalidate()
//    }

    constructor(context: Context) : super(context) { initView(null)}

    constructor(context: Context, attributeSet: AttributeSet) : super(context,attributeSet) { initView(attributeSet)}

    constructor(context: Context, attributeSet: AttributeSet, defStyle : Int) : super(context,attributeSet,defStyle) { initView(attributeSet)}
}