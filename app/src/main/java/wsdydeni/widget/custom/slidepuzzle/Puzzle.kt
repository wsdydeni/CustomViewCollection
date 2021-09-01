package wsdydeni.widget.custom.slidepuzzle

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.AttributeSet
import android.view.View
import wsdydeni.widget.custom.until.BitmapUtils
import wsdydeni.widget.custom.until.RandomUtils
import kotlin.properties.Delegates
import kotlin.random.Random

/*
 * Date: 2020/5/5
 * Author: wsdydeni
 * Description: 验证拼图
 */
class Puzzle : View {

    private var mProgress = 0f //当前滑动进度

    private var randomX by Delegates.notNull<Float>() //滑块Path X轴比例
    private var randomY by Delegates.notNull<Float>() //滑块Path Y轴比例

    private lateinit var mShadowPaint: Paint
    private lateinit var mPuzzlePaint : Paint
    private lateinit var mWhitePaint: Paint

    private lateinit var mBitmap: Bitmap
    private var mPuzzleBitmap : Bitmap? = null
    private var mBackgroundBitmap : Bitmap? = null
    private var mWidth by Delegates.notNull<Int>()
    private var mHeight by Delegates.notNull<Int>()

    //滑块Path
    private var path by Delegates.notNull<Path>()

    //成功动画的构造
    private var isShowAnim = false
    private var mTranslateX = 0f
    private var mTranslateY = 0f
    private lateinit var rect: Rect
    private lateinit var mSuccessPaint: Paint
    private lateinit var linearGradient: LinearGradient
    private lateinit var mGradientMatrix : Matrix
    private lateinit var valueAnimator: ValueAnimator

    fun getCurRandomX(): Float = randomX

    fun setProgress(progress : Float) {
        mProgress = progress
        invalidate()
    }

    fun showSuccessAnim() {
        isShowAnim = true
        valueAnimator.start()
    }

    private fun initAnimator() {
        valueAnimator = ValueAnimator.ofFloat(0f,1f).apply {
            duration = 1000
            addUpdateListener {
                val progress = it.animatedValue as Float
                mTranslateX = 4 * mWidth * progress - 2 * mWidth
                mTranslateY = mHeight * progress
                mGradientMatrix.setTranslate(mTranslateX,mTranslateY)
                linearGradient.setLocalMatrix(mGradientMatrix)
                invalidate()
            }
        }
    }

    fun setBitmap(bitmap: Bitmap) {
        mBitmap = bitmap
        invalidate()
    }

    //背景Bitmap
    private fun getBackgroundBitmap(bitmap: Bitmap) : Bitmap {
        val newBitmap = Bitmap.createBitmap(bitmap.width,bitmap.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.save()
        canvas.drawBitmap(bitmap,0f,0f,null)
        canvas.restore()
        canvas.save()
        canvas.drawPath(path,mShadowPaint)
        canvas.drawPath(path,mWhitePaint)
        canvas.restore()
        return newBitmap
    }

    //滑块Bitmap
    private fun getPuzzleBitmap(bitmap: Bitmap) : Bitmap {
        val newBitmap = Bitmap.createBitmap(bitmap.width,bitmap.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.save()
        canvas.drawPath(path,mPuzzlePaint)
        mPuzzlePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap,0f,0f,mPuzzlePaint)
        mPuzzlePaint.xfermode = null
        canvas.restore()
        canvas.drawPath(path,mWhitePaint)
        return newBitmap
    }

    //模糊Bitmap
    private fun getBlurBitmap(bitmap: Bitmap) : Bitmap {
        val targetBitmap = Bitmap.createBitmap(bitmap.width,bitmap.height,Bitmap.Config.ARGB_8888)
        val rs = RenderScript.create(context)
        val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        val allIn = Allocation.createFromBitmap(rs,bitmap)
        val allOut = Allocation.createFromBitmap(rs,targetBitmap)
        blurScript.setRadius(10f)
        blurScript.setInput(allIn)
        blurScript.forEach(allOut)
        allOut.copyTo(targetBitmap)
        rs.destroy()
        return targetBitmap
    }

    private fun randomDirection() : Float {
        return if(Random.nextInt(0,2) == 0) { -180f }else { 180f }
    }

    private fun randomGap() : Array<Int?> {
        val list = arrayOfNulls<Int>(Random.nextInt(2,5))
        for(i in list.indices) { list[i] = Random.nextInt(1,5) }
        return list
    }

    //随机产生滑块样式
    private fun createPath() {
        randomX = RandomUtils.randomFloat(0.5f,0.8f)
        randomY = RandomUtils.randomFloat(0.1f,0.3f)
        val gapArray = randomGap()
        val sideLength = if(0.1f * mWidth >= 0.1f * mHeight) { 0.1f * mWidth }else { 0.1f * mHeight }
        path.moveTo(randomX * mWidth, randomY * mHeight)
        path.lineTo(randomX * mWidth + 0.2f * sideLength, randomY * mHeight)
        if(1 in gapArray) {
            path.arcTo(RectF(randomX * mWidth + 0.2f * sideLength, randomY * mHeight - 0.2f * sideLength,
                randomX * mWidth + 0.6f * sideLength, randomY * mHeight + 0.2f * sideLength),
                180f,randomDirection())
        }
        path.lineTo(randomX * mWidth + sideLength, randomY * mHeight)
        path.lineTo(randomX * mWidth + sideLength, randomY * mHeight + 0.2f * sideLength)
        if(2 in gapArray) {
            path.arcTo(RectF(randomX * mWidth + 0.8f * sideLength, randomY * mHeight + 0.2f * sideLength,
                randomX * mWidth + 1.2f * sideLength, randomY * mHeight + 0.6f * sideLength),
                270f,randomDirection())
        }
        path.lineTo(randomX * mWidth + sideLength, randomY * mHeight + sideLength)
        path.lineTo(randomX * mWidth + 0.6f * sideLength, randomY * mHeight + sideLength)
        if(3 in gapArray){
            path.arcTo(RectF(randomX * mWidth + 0.2f * sideLength, randomY * mHeight + 0.8f * sideLength,
                randomX * mWidth + 0.6f * sideLength, randomY * mHeight + 1.2f * sideLength),
                0f,randomDirection())
        }
        path.lineTo(randomX * mWidth, randomY * mHeight + sideLength)
        path.lineTo(randomX * mWidth,randomY * mHeight + 0.8f * sideLength)
        if (4 in gapArray) {
            path.arcTo(RectF(randomX * mWidth - 0.2f * sideLength, randomY * mHeight + 0.4f * sideLength,
                randomX * mWidth + 0.2f * sideLength, randomY * mHeight + 0.8f * sideLength),
                90f,randomDirection())
        }
        path.close()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        createPath()
        rect.set(0,0,w,h)
        linearGradient = LinearGradient(mWidth.toFloat() / 2.toFloat(),0f,0f,mHeight.toFloat(),
            intArrayOf(Color.BLACK,Color.WHITE,Color.BLACK),null,Shader.TileMode.CLAMP)
        mSuccessPaint.shader = linearGradient
        mSuccessPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.LIGHTEN)
        mGradientMatrix.setTranslate(- 2f * mWidth.toFloat(),mHeight.toFloat())
        linearGradient.setLocalMatrix(mGradientMatrix)
        mBitmap = BitmapUtils.getNewBitmap(mBitmap,mWidth,mHeight)
        mBackgroundBitmap = getBackgroundBitmap(mBitmap)
        mPuzzleBitmap = getPuzzleBitmap(getBlurBitmap(mBitmap))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(isShowAnim) {
            canvas.save()
            //绘制完整背景Bitmap
            canvas.drawBitmap(mBitmap, 0f,0f,null)
            //绘制成功动画
            canvas.drawRect(rect,mSuccessPaint)
            canvas.restore()
        }else {
            canvas.save()
            //绘制背景Bitmap
            mBackgroundBitmap?.let { canvas.drawBitmap(it,0f,0f,null) }
            canvas.restore()
            canvas.save()
            //绘制滑块
            mPuzzleBitmap?.let { canvas.drawBitmap(it, -(randomX * mWidth) + mProgress * 0.9f * width, 0f, null) }
            canvas.restore()
        }
    }

    private fun initView() {
        path = Path()
        rect = Rect()
        mGradientMatrix = Matrix()
        setLayerType(LAYER_TYPE_SOFTWARE,null)
        mWhitePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }
        mShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            color = 0x77000000
            maskFilter = BlurMaskFilter(20f, BlurMaskFilter.Blur.SOLID)
        }
        mPuzzlePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            color = Color.WHITE
            maskFilter = BlurMaskFilter(5f, BlurMaskFilter.Blur.NORMAL)
        }
        mSuccessPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        initAnimator()
    }

    constructor(context: Context) : super(context) { initView() }

    constructor(context: Context, attributeSet: AttributeSet) : super(context,attributeSet) { initView() }

    constructor(context: Context, attributeSet: AttributeSet, defStyle : Int) : super(context,attributeSet,defStyle) { initView() }
}