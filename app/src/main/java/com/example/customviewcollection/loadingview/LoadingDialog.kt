package com.example.customviewcollection.loadingview

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.customviewcollection.R

/*
 * Create by wsdydeni on 2020/10/4 15:24
 */
class LoadingDialog(context: Context) {

    private var mContext: Context? = null

    private var mDialog: Dialog? = null

    private var mLoadingCircleView: LoadingCircleView

    private var mLoadingText: TextView

    private var isCanBack = false

    init {
        mContext = context
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_loading_dialog_view,null)
        mLoadingCircleView = view.findViewById(R.id.dialog_loading_view)
        mLoadingText = view.findViewById(R.id.dialog_loading_text)
        initView(view)
    }

    private fun initView(view: View) {
        mDialog = object : Dialog(mContext!!,R.style.Dialog_Fullscreen) {
            override fun onBackPressed() {
                if(!isCanBack) return
                this@LoadingDialog.close()
            }
        }
        mDialog?.setCancelable(isCanBack)
        mDialog?.setContentView(view.findViewById(R.id.dialog_loading_layout) as ConstraintLayout,
            ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.MATCH_PARENT))
        mDialog?.setOnDismissListener {
            mContext = null
        }
    }

    fun show(text: String) {
        mLoadingCircleView.loadLoading()
        mLoadingText.text = text
        mDialog?.show()
    }

    fun success(text: String) {
        mLoadingCircleView.loadSuccess()
        mLoadingText.text = text
    }

    fun failure(text: String) {
        mLoadingCircleView.loadFailure()
        mLoadingText.text = text
    }

    fun close() {
        mDialog?.let {
            mLoadingCircleView.stopAllAnimator()
            it.dismiss()
        }
    }
}