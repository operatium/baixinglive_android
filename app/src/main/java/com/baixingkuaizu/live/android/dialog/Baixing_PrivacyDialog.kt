package com.baixingkuaizu.live.android.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.Window
import android.widget.Toast
import com.baixingkuaizu.live.android.busiess.router.Baixing_GoRouter
import com.baixingkuaizu.live.android.databinding.BaixingPrivacyDialogBinding

class Baixing_PrivacyDialog(context: Context) : Dialog(context) {

    private var mBaixing_onAgreeListener: (() -> Unit)? = null
    private var mBaixing_onDisagreeListener: (() -> Unit)? = null

    private lateinit var mBaixing_binding: BaixingPrivacyDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        
        mBaixing_binding = BaixingPrivacyDialogBinding.inflate(layoutInflater)
        setContentView(mBaixing_binding.root)

        // 设置文本可点击
        mBaixing_binding.baixingPrivacyContent.movementMethod = LinkMovementMethod.getInstance()

        // 获取原始文本
        val content = mBaixing_binding.baixingPrivacyContent.text.toString()

        // 创建SpannableString
        val spannableString = SpannableString(content)

        // 处理《用户协议》
        val userAgreementStart = content.indexOf("《用户协议》")
        val userAgreementEnd = userAgreementStart + "《用户协议》".length
        if (userAgreementStart >= 0) {
            // 设置点击事件
            spannableString.setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        Toast.makeText(context, "《用户协议》", Toast.LENGTH_SHORT).show()
                        Baixing_GoRouter.baixing_jumpWebActivity("用户协议", "")
                    }
                },
                userAgreementStart,
                userAgreementEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // 设置颜色
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor("#87CEEB")),
                userAgreementStart,
                userAgreementEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // 处理《隐私政策》
        val privacyPolicyStart = content.indexOf("《隐私政策》")
        val privacyPolicyEnd = privacyPolicyStart + "《隐私政策》".length
        if (privacyPolicyStart >= 0) {
            // 设置点击事件
            spannableString.setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        Toast.makeText(context, "《隐私政策》", Toast.LENGTH_SHORT).show()
                        Baixing_GoRouter.baixing_jumpWebActivity("隐私政策", "")
                    }
                },
                privacyPolicyStart,
                privacyPolicyEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // 设置颜色
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor("#87CEEB")),
                privacyPolicyStart,
                privacyPolicyEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // 设置处理后的文本
        mBaixing_binding.baixingPrivacyContent.text = spannableString

        mBaixing_binding.baixingBtnAgree.setOnClickListener {
            mBaixing_onAgreeListener?.invoke()
            dismiss()
        }
        
        mBaixing_binding.baixingBtnDisagree.setOnClickListener {
            mBaixing_onDisagreeListener?.invoke()
            dismiss()
        }
    }
    
    fun baixing_setOnAgreeListener(listener: () -> Unit) {
        this.mBaixing_onAgreeListener = listener
    }
    
    fun baixing_setOnDisagreeListener(listener: () -> Unit) {
        this.mBaixing_onDisagreeListener = listener
    }
}