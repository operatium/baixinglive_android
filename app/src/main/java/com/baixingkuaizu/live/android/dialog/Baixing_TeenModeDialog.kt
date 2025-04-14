package com.baixingkuaizu.live.android.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.baixingkuaizu.live.android.os.Baixing_AdapterHelper.setClick
import com.baixingkuaizu.live.android.databinding.BaixingTeenModeDialogBinding

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 青少年模式对话框

 */
class Baixing_TeenModeDialog(context: Context) : Dialog(context) {

    private var mBaixing_onEnterTeenModeListener: (() -> Unit)? = null
    private var mBaixing_onDismissListener: (() -> Unit)? = null

    private lateinit var mBaixing_binding: BaixingTeenModeDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        setCancelable(false)

        mBaixing_binding = BaixingTeenModeDialogBinding.inflate(layoutInflater)
        setContentView(mBaixing_binding.root)

        mBaixing_binding.tvEnterTeenMode.setClick {
            mBaixing_onEnterTeenModeListener?.invoke()
            dismiss()
        }
        mBaixing_binding.btnDismiss.setClick {
            mBaixing_onDismissListener?.invoke()
            dismiss()
        }
    }

    fun baixing_setOnEnterTeenModeListener(listener: () -> Unit) {
        mBaixing_onEnterTeenModeListener = listener
    }

    fun baixing_setOnDismissListener(listener: () -> Unit) {
        mBaixing_onDismissListener = listener
    }
}