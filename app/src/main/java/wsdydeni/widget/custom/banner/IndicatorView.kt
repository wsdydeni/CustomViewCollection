package wsdydeni.widget.custom.banner

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat

/*
 * Date: 2020/4/29
 * Author: wsdydeni
 * Description: 自定义指示器
 */
class IndicatorView : LinearLayoutCompat {

    private var mSelectColor: Drawable? = null
    private var mNormalColor: Drawable? = null

    private var mAlpha = 0f

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun initView(indicatorCount: Int, alpha: Float ,selectPosition: Int, selectDrawable: Drawable?, normalDrawable: Drawable?): IndicatorView {
        mSelectColor = selectDrawable
        mNormalColor = normalDrawable
        this.mAlpha = alpha
        for (i in 0 until indicatorCount) {
            val ivIndicator = AppCompatImageView(context)
            val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            val scale = context.resources.displayMetrics.density
            lp.leftMargin = if (i == 0) 0 else (6 * scale + 0.5f).toInt()
            ivIndicator.layoutParams = lp
            ivIndicator.background = (if (i == selectPosition) selectDrawable else normalDrawable)
            ivIndicator.alpha = if (i == selectPosition) 1f else alpha
            addView(ivIndicator)
        }
        return this
    }

    fun changeIndicator(position: Int) {
        val count = childCount
        for (i in 0 until count) {
            val ivIndicator = getChildAt(i) as AppCompatImageView
            ivIndicator.background = mNormalColor
            ivIndicator.alpha = mAlpha
        }
        val ivIndicator = getChildAt(position) as AppCompatImageView
        ivIndicator.background = mSelectColor
        ivIndicator.alpha = 1.0f
    }
}