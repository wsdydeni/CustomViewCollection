package wsdydeni.widget.puzzle

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class SlideBar : View {

    private var mDistance = 0 //滑动距离

    private lateinit var mPaint: Paint
    private lateinit var mBitmap: Bitmap
    private lateinit var backgroundRect : RectF

    private var mBitmapWidth by Delegates.notNull<Int>()
    private var mBitmapHeight by Delegates.notNull<Int>()

    private lateinit var progressAnimator: ValueAnimator
    private lateinit var distanceAnimator: ValueAnimator

    private var userTime by Delegates.notNull<Float>() //拖动使用时间
    private var currentTemp by Delegates.notNull<Long>() //拖动开始时间

    private var _onDrag : ((Float, Float?, Boolean) -> Unit)? = null //滑动监听

    fun setOnDragListener(listener : (Float, Float?, Boolean) -> Unit) {
        _onDrag = listener
    }

    fun reset() {
        //重置拖动位置
        distanceAnimator = ValueAnimator.ofInt(mDistance,0).apply {
            duration = 1000
            addUpdateListener {
                mDistance = it.animatedValue as Int
                invalidate()
            }
            start()
        }
        //重置滑块位置
        progressAnimator = ValueAnimator.ofFloat(mDistance.toFloat() / (width - mBitmapWidth),0f).apply {
            duration = 1000
            addUpdateListener { _onDrag?.invoke(it.animatedValue as Float,null,false) }
            start()
        }
    }

    private fun dp2pxF(context: Context, dp: Float) : Float = dp * context.resources.displayMetrics.density + 0.5f

    private fun getBitmap(context: Context, vectorDrawableId : Int) : Bitmap {
        val vectorDrawable = ContextCompat.getDrawable(context,vectorDrawableId) ?: throw NullPointerException("can't found the resource")
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0,0,canvas.width,canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    private fun getNewBitmap(bitmap: Bitmap, newWidth : Int, newHeight : Int) : Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val scaleWidth = newWidth / width.toFloat()
        val scaleHeight = newHeight / height.toFloat()
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap,0,0,width, height,matrix,true)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mBitmapHeight = height / 5 * 3
        mBitmapWidth = width / 8
        mBitmap = getNewBitmap(mBitmap,mBitmapWidth,mBitmapHeight)
        backgroundRect.set(0f, height.toFloat() / 5 * 2, width.toFloat(), height.toFloat() / 5 * 3)
        //绘制拖动条背景
        canvas.drawRoundRect(backgroundRect,dp2pxF(context,5f), dp2pxF(context,5f),mPaint)
        if(mDistance >= width - mBitmapWidth ) mDistance = width - mBitmapWidth
        if(mDistance <= 0) mDistance = 0
        //绘制拖动图片
        canvas.drawBitmap(mBitmap, mDistance.toFloat(), ((height - mBitmapHeight) / 2).toFloat(),mPaint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentTemp = System.currentTimeMillis()
            }
            MotionEvent.ACTION_MOVE -> {
                mDistance = if(event.x.toInt() < 0) 0 else event.x.toInt()
                _onDrag?.invoke(mDistance.toFloat() / (width - mBitmapWidth),null,false)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                userTime = (System.currentTimeMillis() - currentTemp) / 1000f
                _onDrag?.invoke(mDistance.toFloat() / (width - mBitmapWidth),userTime,true)
            }
        }
        return true
    }

    private fun initView() {
        setLayerType(LAYER_TYPE_SOFTWARE,null)
        mPaint = Paint()
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.parseColor("#E7E7E7")
        backgroundRect = RectF()
        mBitmap = getBitmap(context, R.drawable.ic_slide)
    }

    constructor(context: Context) : super(context) { initView() }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) { initView() }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle) { initView() }
}