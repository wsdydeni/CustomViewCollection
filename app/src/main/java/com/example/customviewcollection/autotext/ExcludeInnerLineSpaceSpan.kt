package com.example.customviewcollection.autotext

import android.graphics.Paint
import android.text.style.LineHeightSpan
import kotlin.math.roundToInt

class ExcludeInnerLineSpaceSpan(private val mHeight : Int) : LineHeightSpan {

    override fun chooseHeight(text: CharSequence?, start: Int, end: Int, spanstartv: Int, lineHeight: Int, fm: Paint.FontMetricsInt?) {
        if (fm == null) return
        val originHeight = fm.descent - fm.ascent
        if(originHeight < 0) return
        val ratio : Float = mHeight * 1.0f / originHeight
        fm.descent = (fm.descent * ratio).roundToInt()
        fm.ascent = fm.descent - mHeight
    }

}