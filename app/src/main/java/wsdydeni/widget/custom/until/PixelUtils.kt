package wsdydeni.widget.custom.until

import android.content.Context
import android.util.TypedValue

class PixelUtils {

    companion object {

        fun dp2px(context: Context, dp : Float) : Int = (dp * context.resources.displayMetrics.density + 0.5f).toInt()

        fun dp2pxF(context: Context,dp: Float) : Float = dp * context.resources.displayMetrics.density + 0.5f

        fun px2dp(context: Context,px : Float) : Int = (px / context.resources.displayMetrics.density + 0.5f).toInt()

        fun sp2Px(context: Context, sp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,context.resources.displayMetrics).toInt()
    }
}