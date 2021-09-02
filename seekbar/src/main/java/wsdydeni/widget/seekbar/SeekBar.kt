package wsdydeni.widget.seekbar

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class SeekBar : View {

    private var mDistance = 0
    private lateinit var mPaint: Paint
    private lateinit var backgroundRect : RectF
    private lateinit var dragRect : RectF

    private lateinit var startBitmap: Bitmap
    private lateinit var endBitmap: Bitmap
    private var mBitmapWidth by Delegates.notNull<Int>()
    private var mBitmapHeight by Delegates.notNull<Int>()

    private var mDragColor by Delegates.notNull<Int>()
    private var mBackgroundColor by Delegates.notNull<Int>()
    private val defaultBackgroundColor = Color.parseColor("#C7E1F3")
    private val defaultDragColor = Color.parseColor("#FF2912")

    private var mText = ""
    private val defaultText = "向右滑动验证"
    private var mTextColor by Delegates.notNull<Int>()
    private val defaultTextColor = Color.parseColor("#CBB5BE")

    private var isFirst = true
    private var _onFinish : (() -> Unit)? = null

    fun setOnFinishListener(listener : (() -> Unit)) {
        _onFinish = listener
    }

    fun getBitmap(context: Context,vectorDrawableId : Int) : Bitmap {
        val vectorDrawable = ContextCompat.getDrawable(context,vectorDrawableId) ?: throw NullPointerException("can't found the resource")
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0,0,canvas.width,canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    fun getNewBitmap(bitmap: Bitmap,newWidth : Int,newHeight : Int) : Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val scaleWidth = newWidth / width.toFloat()
        val scaleHeight = newHeight / height.toFloat()
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap,0,0,width, height,matrix,true)
    }

    fun dp2px(context: Context, dp : Float) : Int = (dp * context.resources.displayMetrics.density + 0.5f).toInt()

    fun dp2pxF(context: Context,dp: Float) : Float = dp * context.resources.displayMetrics.density + 0.5f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDistance = mBitmapWidth + 2 * dp2px(context,3f)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                mDistance = if(event.x.toInt() < mBitmapWidth + 2 *  dp2px(context,3f)) mBitmapWidth + 2 * dp2px(context,3f) else event.x.toInt()
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                if(mDistance < width) {
                    mDistance = 0
                    invalidate()
                }
                if(mDistance >= width) {
                    mDistance = width
                    invalidate()
                }
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(isFirst) {
            mBitmapHeight = if(startBitmap.height > height / 2) { height / 2 }else { startBitmap.height }
            mBitmapWidth = if(startBitmap.width > width / 10) { width / 10 }else { startBitmap.width }
            //问题 onDraw中多次修改Bitmap宽高引发异常
            startBitmap = getNewBitmap(startBitmap,mBitmapWidth,mBitmapHeight)
            endBitmap = getNewBitmap(endBitmap,mBitmapWidth,mBitmapHeight)
            isFirst = false
        }
        //背景颜色
        mPaint.color = mBackgroundColor
        //背景大小
        backgroundRect.set(0f,0f, width.toFloat(), height.toFloat())
        //绘制背景
        canvas.drawRoundRect(backgroundRect, dp2px(context,5f).toFloat(), dp2px(context,5f).toFloat(),mPaint)
        //最大滑动距离为宽度
        if(mDistance >= width ) mDistance = width
        if(mDistance <= 0) mDistance = 0
        //拖动背景颜色
        mPaint.color = mTextColor
        canvas.drawText(mText, (width / 2).toFloat(), height * 0.5f,mPaint)
        mPaint.color = mDragColor
        //拖动背景大小
        dragRect.set(0f,0f, mDistance.toFloat(), height.toFloat())
        //绘制拖动背景
        canvas.drawRoundRect(dragRect, dp2px(context,5f).toFloat(), dp2px(context,5f).toFloat(),mPaint)
        //根据拖动距离显示图片
        if(mDistance >= width) {
            parent.requestDisallowInterceptTouchEvent(false)
            val bitmapLeft = (mDistance - mBitmapWidth - dp2px(context,3f)).toFloat()
            canvas.drawBitmap(endBitmap, bitmapLeft, ((height - mBitmapHeight) / 2).toFloat(),mPaint)
            _onFinish?.invoke()
        }else {
            val bitmapLeft = if(mDistance - (mBitmapWidth + 2 * dp2px(context,3f))  <= 0) {
                dp2pxF(context,3f)
            } else {
                (mDistance - mBitmapWidth - dp2px(context,3f)).toFloat()
            }
            mDistance - mBitmapWidth.toFloat()
            canvas.drawBitmap(startBitmap, bitmapLeft,((height - mBitmapHeight) / 2).toFloat(),mPaint)
        }
    }

    private fun initView(attributeSet: AttributeSet?) {
        if(attributeSet != null) {
            val array = context.obtainStyledAttributes(attributeSet, R.styleable.SeekBar)
            val resultBackgroundColor = array.getColor(R.styleable.SeekBar_backgroundColor,defaultBackgroundColor)
            mBackgroundColor = if(resultBackgroundColor != defaultBackgroundColor) {
                resultBackgroundColor
            }else {
                val backgroundColorId = array.getResourceId(R.styleable.SeekBar_backgroundColor,R.color.paleBlue)
                ContextCompat.getColor(context,backgroundColorId)
            }
            val resultDragColor = array.getColor(R.styleable.SeekBar_dragColor,defaultDragColor)
            mDragColor = if(resultDragColor != defaultDragColor) {
                resultDragColor
            }else {
                val dragColorId = array.getResourceId(R.styleable.SeekBar_dragColor,R.color.orange)
                ContextCompat.getColor(context,dragColorId)
            }
            startBitmap = getBitmap(context,array.getResourceId(R.styleable.SeekBar_startBitmap,R.drawable.ic_right_more))
            endBitmap = getBitmap(context,array.getResourceId(R.styleable.SeekBar_endBitmap,R.drawable.ic_ok))
            mText = array.getString(R.styleable.SeekBar_text) ?: defaultText
            val resultTextColor = array.getColor(R.styleable.SeekBar_textColor,defaultTextColor)
            mTextColor = if(resultTextColor != defaultTextColor) {
                resultTextColor
            }else {
                val textColorId = array.getResourceId(R.styleable.SeekBar_textColor,R.color.paleGrey)
                ContextCompat.getColor(context,textColorId)
            }
            array.recycle()
        }else {
            mText = defaultText
            mTextColor = defaultTextColor
            mBackgroundColor = defaultBackgroundColor
            mDragColor = defaultDragColor
            //小问题 5.0以上系统无法将vector转变成bitmap
            startBitmap = getBitmap(context,R.drawable.ic_right_more)
            endBitmap = getBitmap(context,R.drawable.ic_ok)
        }
        dragRect = RectF()
        backgroundRect = RectF()
        mPaint = Paint().apply {
            style = Paint.Style.FILL
            textSize = 40f
            textAlign = Paint.Align.CENTER
        }
    }

    constructor(context: Context) : super(context) { initView(null)}

    constructor(context: Context, attributeSet: AttributeSet) : super(context,attributeSet) { initView(attributeSet)}

    constructor(context: Context, attributeSet: AttributeSet, defStyle : Int) : super(context,attributeSet,defStyle) { initView(attributeSet)}
}