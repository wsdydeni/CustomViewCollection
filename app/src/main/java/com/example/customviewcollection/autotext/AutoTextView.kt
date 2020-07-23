package com.example.customviewcollection.autotext

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class AutoTextView : AppCompatTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context,attributeSet: AttributeSet?) : super(context,attributeSet)

    constructor(context: Context,attributeSet: AttributeSet?,defStyleAttr : Int) : super(context,attributeSet,defStyleAttr)

    fun setCustomText(text : CharSequence?) {
        if(text == null) return
        val lineHeight = textSize.toInt()
        val ssb : SpannableStringBuilder = if(text is SpannableStringBuilder) {
            text.also { it.setSpan(ExcludeInnerLineSpaceSpan(lineHeight),0,text.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) }
        }else {
            SpannableStringBuilder(text).also {
                it.setSpan(ExcludeInnerLineSpaceSpan(lineHeight),0,text.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        setText(ssb)
    }

}