package com.baixingkuaizu.live.android.widget.loading

import android.app.Dialog
import android.content.Context
import android.view.WindowManager

class Baixing_FullScreenLoadingDialog(context: Context) : Dialog(context) {

    private val mBaixing_loadingView: Baixing_LoadingView

    init {
        window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            attributes?.dimAmount = 0.8f
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        }
        setCancelable(false)
        setCanceledOnTouchOutside(false)

        mBaixing_loadingView = Baixing_LoadingView(context)
        setContentView(mBaixing_loadingView)
    }
}