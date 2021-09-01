package wsdydeni.widget.custom.loadingview

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import wsdydeni.widget.custom.R

/*
 * Date: 2020/10/4
 * Author: wsdydeni
 * Description: LoadingCircleView
 */
class LoadingCircleView : View {

    enum class StatusEnum {
        Loading, LoadSuccess, LoadFailure
    }

    private var status: StatusEnum = StatusEnum.Loading

    private var oval = RectF()

    private var sweepAngle = 120f
    private var loadingValue = 0f

    private val defaultLoadingColor = Color.BLUE

    private val defaultPaintWidth = 20f
    private val mPathMeasure: PathMeasure = PathMeasure()
    private val mPathCircle: Path = Path()
    private val mPathCircleDst: Path = Path()
    private var circleValue = 0f
    private val defaultBackgroundColor = Color.parseColor("#D6D7DA")

    private val successPath: Path = Path()
    private var successValue = 0f
    private val cornerPathEffect = CornerPathEffect(20f)
    private val defaultSuccessColor = Color.parseColor("#30E849")

    private val failurePathLeft: Path = Path()
    private val failurePathRight: Path = Path()
    private var failValueRight = 0f
    private var failValueLeft = 0f
    private val defaultFailureColor = Color.parseColor("#DB0A19")

    private var mBackgroundPaint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        color = defaultBackgroundColor
        strokeWidth = defaultPaintWidth
    }

    private var mLoadingPaint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        color = defaultLoadingColor
        strokeWidth = defaultPaintWidth
    }

    private var mSuccessPaint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        color = defaultSuccessColor
        strokeWidth = defaultPaintWidth
    }

    private var mFailurePaint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        color = defaultFailureColor
        strokeWidth = defaultPaintWidth
    }

    private var circleAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        addUpdateListener { animation: ValueAnimator ->
            circleValue = animation.animatedValue as Float
            invalidate()
        }
    }

    private var loadingAnimator =  ValueAnimator.ofFloat(0f,360f).apply {
        addUpdateListener { animation: ValueAnimator ->
            loadingValue = animation.animatedValue as Float
            invalidate()
        }
        duration = 1000L
        repeatCount = -1
    }

    private fun startLoadingAnim() {
        loadingAnimator.start()
    }

    private var successAnimator = ValueAnimator.ofFloat(0f, 1.0f).apply {
        addUpdateListener { animation: ValueAnimator ->
            successValue = animation.animatedValue as Float
            invalidate()
        }
    }

    private var successAnimatorSet = AnimatorSet().apply {
        play(successAnimator).after(circleAnimator)
        duration = 500L
    }

    private fun startSuccessAnim() {
        successAnimatorSet.start()
    }

    private var failureLeftAnimator = ValueAnimator.ofFloat(0f, 1.0f).apply {
        addUpdateListener { animation: ValueAnimator ->
            failValueRight = animation.animatedValue as Float
            invalidate()
        }
    }

    private var failureRightAnimator = ValueAnimator.ofFloat(0f, 1.0f).apply {
        addUpdateListener { animation: ValueAnimator ->
            failValueLeft = animation.animatedValue as Float
            invalidate()
        }
    }

    private var failureAnimatorSet = AnimatorSet().apply {
        play(failureLeftAnimator).after(circleAnimator).before(failureRightAnimator)
        duration = 500
    }

    private fun startFailAnim() {
        failureAnimatorSet.start()
    }

    private fun resetPath() {
        successValue = 0f
        circleValue = 0f
        mPathCircle.reset()
        mPathCircleDst.reset()
        successPath.reset()
    }

    fun loadFailure() {
        resetPath()
        status = StatusEnum.LoadFailure
        startFailAnim()
    }

    fun loadLoading() {
        status = StatusEnum.Loading
        startLoadingAnim()
        invalidate()
    }

    fun loadSuccess() {
        resetPath()
        status = StatusEnum.LoadSuccess
        startSuccessAnim()
    }

    fun stopAllAnimator() {
        circleAnimator.cancel()
        circleAnimator.end()
        loadingAnimator.cancel()
        loadingAnimator.end()
        successAnimatorSet.cancel()
        successAnimatorSet.end()
        failureAnimatorSet.cancel()
        failureAnimatorSet.end()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val mRadius = ((if (width > height) height else width).toFloat()) / 2f - 40f
        val mXCenter = width / 2f
        val mYCenter = height / 2f
        when(status) {
            StatusEnum.Loading -> {
                oval.left = mXCenter - mRadius
                oval.right = mXCenter + mRadius
                oval.top = mYCenter - mRadius
                oval.bottom = mYCenter + mRadius
                canvas.drawArc(oval, -90f, 360f, false, mBackgroundPaint)
                canvas.drawArc(oval,loadingValue ,sweepAngle, false, mLoadingPaint)
            }
            StatusEnum.LoadSuccess -> {
                mPathCircle.addCircle(mXCenter, mYCenter, mRadius, Path.Direction.CW)
                mPathMeasure.setPath(mPathCircle, false)
                mPathMeasure.getSegment(0f, circleValue * mPathMeasure.length, mPathCircleDst, true)
                canvas.drawPath(mPathCircleDst, mSuccessPaint)
                successPath.moveTo(mXCenter - mRadius * 0.38f, mYCenter + mRadius * 0.05f)
                successPath.lineTo(mXCenter, mYCenter + mRadius * 0.35f )
                successPath.lineTo(mXCenter + mRadius * 0.35f, mYCenter - mRadius * 0.25f)
                mSuccessPaint.pathEffect = cornerPathEffect
                mPathMeasure.nextContour()
                mPathMeasure.setPath(successPath, false)
                mPathMeasure.getSegment(0f, successValue * mPathMeasure.length, mPathCircleDst, true)
                canvas.drawPath(mPathCircleDst, mSuccessPaint)
            }
            StatusEnum.LoadFailure -> {
                mPathCircle.addCircle(mXCenter, mYCenter, mRadius, Path.Direction.CW)
                mPathMeasure.setPath(mPathCircle, false)
                mPathMeasure.getSegment(0f, circleValue * mPathMeasure.length, mPathCircleDst, true)
                canvas.drawPath(mPathCircleDst, mFailurePaint)
                if (circleValue == 1f) {
                    failurePathRight.moveTo(mXCenter + mRadius * 0.3f, mYCenter - mRadius * 0.3f)
                    failurePathRight.lineTo(mXCenter - mRadius * 0.3f, mYCenter + mRadius * 0.3f)
                    mPathMeasure.nextContour()
                    mPathMeasure.setPath(failurePathRight, false)
                    mPathMeasure.getSegment(0f, failValueRight * mPathMeasure.length, mPathCircleDst, true)
                    canvas.drawPath(mPathCircleDst, mFailurePaint)
                }
                if (failValueRight == 1f) {
                    failurePathLeft.moveTo(mXCenter + mRadius * 0.3f, mYCenter + mRadius * 0.3f)
                    failurePathLeft.lineTo(mXCenter - mRadius * 0.3f, mYCenter - mRadius * 0.3f)
                    mPathMeasure.nextContour()
                    mPathMeasure.setPath(failurePathLeft, false)
                    mPathMeasure.getSegment(0f, failValueLeft * mPathMeasure.length, mPathCircleDst, true)
                    canvas.drawPath(mPathCircleDst, mFailurePaint)
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val minimumWidth = suggestedMinimumWidth
        val minimumHeight = suggestedMinimumHeight
        val width = measureWidth(minimumWidth, widthMeasureSpec)
        val height = measureHeight(minimumHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    private fun measureWidth(defaultWidth: Int, measureSpec: Int): Int {
        var defaultWidth1 = defaultWidth
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        when (specMode) {
            MeasureSpec.AT_MOST -> defaultWidth1 = paddingLeft + paddingRight
            MeasureSpec.EXACTLY -> defaultWidth1 = specSize
            MeasureSpec.UNSPECIFIED -> {
                TODO()
            }
        }
        return defaultWidth1
    }

    private fun measureHeight(defaultHeight: Int, measureSpec: Int): Int {
        var defaultHeight1 = defaultHeight
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        when (specMode) {
            MeasureSpec.AT_MOST -> defaultHeight1 = paddingTop + paddingBottom
            MeasureSpec.EXACTLY -> defaultHeight1 = specSize
            MeasureSpec.UNSPECIFIED -> {
                TODO()
            }
        }
        return defaultHeight1
    }

    private fun initView(attrs: AttributeSet?) {
        attrs?.let {
            val array = context.obtainStyledAttributes(it, R.styleable.LoadingCircleView)
            /*
                背景颜色
             */
            val resultBackgroundColor = array.getColor(R.styleable.LoadingCircleView_backColor,defaultBackgroundColor)
            if(resultBackgroundColor != defaultBackgroundColor) {
                mBackgroundPaint.color = resultBackgroundColor
            }
            /*
                加载颜色
             */
            val resultLoadingColor = array.getColor(R.styleable.LoadingCircleView_loadingColor,defaultLoadingColor)
            if(resultLoadingColor != defaultLoadingColor) {
                mLoadingPaint.color = resultLoadingColor
            }
            /*
                成功颜色
             */
            val resultSuccessColor = array.getColor(R.styleable.LoadingCircleView_successColor,defaultSuccessColor)
            if(resultSuccessColor != defaultSuccessColor) {
                mSuccessPaint.color = resultSuccessColor
            }
            /*
                失败颜色
             */
            val resultFailureColor = array.getColor(R.styleable.LoadingCircleView_failureColor,defaultFailureColor)
            if(resultFailureColor != defaultFailureColor) {
                mFailurePaint.color = resultFailureColor
            }
            /*
                画笔宽度
             */
            val resultPaintWidth = array.getFloat(R.styleable.LoadingCircleView_paintWidth,defaultPaintWidth)
            if(resultPaintWidth != defaultPaintWidth) {
                mBackgroundPaint.strokeWidth = resultPaintWidth
                mLoadingPaint.strokeWidth = resultPaintWidth
                mSuccessPaint.strokeWidth = resultPaintWidth
                mFailurePaint.strokeWidth = resultPaintWidth
            }
            array.recycle()
        }
    }

    constructor(context: Context?) : super(context) { initView(null) }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { initView(attrs) }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initView(attrs) }

}