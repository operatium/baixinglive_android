package com.baixingkuaizu.live.android.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.baixingkuaizu.live.android.R
import androidx.core.graphics.drawable.toDrawable

class Baixing_TeenModeDialog(context: Context) : Dialog(context) {

    private var mBaixing_onEnterTeenModeListener: (() -> Unit)? = null
    private var mBaixing_onDismissListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        setContentView(R.layout.baixing_dialog_teen_mode)

        window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
        }

        findViewById<TextView>(R.id.tv_enter_teen_mode)?.setOnClickListener {
            mBaixing_onEnterTeenModeListener?.invoke()
            dismiss()
        }

        findViewById<Button>(R.id.btn_dismiss)?.setOnClickListener {
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