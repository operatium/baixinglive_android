package com.baixingkuaizu.live.android.dialog

import android.app.Activity
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
import com.baixingkuaizu.live.android.os.Baixing_AdapterHelper.setClick
import com.baixingkuaizu.live.android.busiess.router.Baixing_GoRouter
import com.baixingkuaizu.live.android.databinding.BaixingPrivacyDialogBinding
import com.baixingkuaizu.live.android.widget.toast.CenterToast

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 隐私政策对话框，用于展示隐私政策内容并获取用户同意或拒绝的结果。显示应用的隐私政策和用户协议，并提供同意和拒绝按钮。使用ViewBinding进行视图绑定，支持点击协议文本打开详细内容，通过回调机制向调用方传递用户的选择结果。设置为不可取消，确保用户必须做出选择。
 */
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

        mBaixing_binding.baixingPrivacyContent.movementMethod = LinkMovementMethod.getInstance()

        val content = mBaixing_binding.baixingPrivacyContent.text.toString()

        val spannableString = SpannableString(content)

        val userAgreementStart = content.indexOf("《用户协议》")
        val userAgreementEnd = userAgreementStart + "《用户协议》".length
        if (userAgreementStart >= 0) {
            spannableString.setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        CenterToast.show(context as? Activity, "《用户协议》")
                        Baixing_GoRouter.baixing_jumpWebActivity(taskName = "用户协议", taskType = "Baixing_PrivacyAgreementTaskManager")
                    }
                },
                userAgreementStart,
                userAgreementEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor("#87CEEB")),
                userAgreementStart,
                userAgreementEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        val privacyPolicyStart = content.indexOf("《隐私政策》")
        val privacyPolicyEnd = privacyPolicyStart + "《隐私政策》".length
        if (privacyPolicyStart >= 0) {
            spannableString.setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        CenterToast.show(context as? Activity, "《隐私政策》")
                        Baixing_GoRouter.baixing_jumpWebActivity(taskName = "隐私政策", taskType = "Baixing_PrivacyAgreementTaskManager")
                    }
                },
                privacyPolicyStart,
                privacyPolicyEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor("#87CEEB")),
                privacyPolicyStart,
                privacyPolicyEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        mBaixing_binding.baixingPrivacyContent.text = spannableString

        mBaixing_binding.baixingBtnAgree.setClick {
            mBaixing_onAgreeListener?.invoke()
            dismiss()
        }
        
        mBaixing_binding.baixingBtnDisagree.setClick {
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