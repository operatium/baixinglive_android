package com.baixingkuaizu.live.android.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.baixingkuaizu.live.android.busiess.router.Baixing_GoRouter
import com.baixingkuaizu.live.android.databinding.BaixingFragmentLoginBinding

class Baixing_LoginFragment : Fragment() {
    
    private lateinit var mBaixing_binding: BaixingFragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBaixing_binding = BaixingFragmentLoginBinding.inflate(inflater, container, false)
        return mBaixing_binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baixing_initView()
    }
    
    private fun baixing_initView() {
        mBaixing_binding.apply {
            baixingBtnSendCode.setOnClickListener {
                baixing_sendVerificationCode()
            }
            
            baixingBtnLogin.setOnClickListener {
                baixing_login()
            }
        }
        // 设置文本可点击
        mBaixing_binding.baixingCheckboxAgreement.movementMethod = LinkMovementMethod.getInstance()

        // 获取原始文本
        val content = mBaixing_binding.baixingCheckboxAgreement.text.toString()

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
                        Baixing_GoRouter.baixing_jumpWebActivity(taskName = "用户协议", taskType = "Baixing_PrivacyAgreementTaskManager")
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
                        Baixing_GoRouter.baixing_jumpWebActivity(taskName = "隐私政策", taskType = "Baixing_PrivacyAgreementTaskManager")
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
        mBaixing_binding.baixingCheckboxAgreement.text = spannableString
    }
    
    private fun baixing_sendVerificationCode() {
    }
    
    private fun baixing_login() {
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
    }
}