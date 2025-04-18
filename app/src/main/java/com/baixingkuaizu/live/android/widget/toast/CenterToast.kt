package com.baixingkuaizu.live.android.widget.toast

import android.annotation.SuppressLint
import android.app.Activity
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.baixingkuaizu.live.android.R

object CenterToast {

    @SuppressLint("InflateParams")
    fun show(activity: Activity?, message:String) {
        val inflater = activity?.layoutInflater?:return
        val layout: View = inflater.inflate(R.layout.baixing_custom_toast_layout, null)
        val textView = layout.findViewById<TextView>(R.id.custom_toast_text)
        textView.text = message

        val toast = Toast(activity)
        toast.view = layout
        toast.duration = Toast.LENGTH_SHORT
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}