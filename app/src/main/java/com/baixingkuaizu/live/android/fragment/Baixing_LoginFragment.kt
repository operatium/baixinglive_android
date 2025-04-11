package com.baixingkuaizu.live.android.fragment

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.router.Baixing_GoRouter

import com.baixingkuaizu.live.android.databinding.BaixingFragmentLoginBinding
import com.baixingkuaizu.live.android.viewmodel.Baixing_LoginViewModel
import com.baixingkuaizu.live.android.widget.toast.CenterToast
import androidx.core.graphics.toColorInt
import com.baixingkuaizu.live.android.widget.loading.Baixing_FullScreenLoadingDialog

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 登录界面Fragment，处理用户登录相关功能
 */
class Baixing_LoginFragment : Baixing_BaseFragment() {
    
    private lateinit var mBaixing_binding: BaixingFragmentLoginBinding

    private lateinit var mBaixing_viewModel: Baixing_LoginViewModel

    private var mBaixing_loading: Baixing_FullScreenLoadingDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBaixing_binding = BaixingFragmentLoginBinding.inflate(inflater, container, false)
        return mBaixing_binding.root
    }
    
    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBaixing_viewModel = ViewModelProvider(requireActivity()).get(Baixing_LoginViewModel::class.java)
        baixing_initView()
        mBaixing_viewModel.mBaixing_toast.observe(viewLifecycleOwner) {
            CenterToast.show(activity, it)
        }
        mBaixing_viewModel.mBaixing_codeTime.observe(viewLifecycleOwner) {
            if (it > 0) {
                mBaixing_binding.baixingBtnSendCode.text = "${it}s"
            } else {
                mBaixing_binding.baixingBtnSendCode.text = "发验证码"
            }
        }
        mBaixing_viewModel.mBaixing_loginLoading.observe(viewLifecycleOwner) {
            if (mBaixing_loading == null) {
                mBaixing_loading = Baixing_FullScreenLoadingDialog(requireActivity())
            }
            if (it) {
                mBaixing_loading?.show()
            } else {
                mBaixing_loading?.hide()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mBaixing_viewModel.baixing_checkCodeTime()
    }

    private fun baixing_initView() {
        mBaixing_binding.apply {
            baixingBtnSendCode.setClick {
                baixing_agress {
                    baixing_sendVerificationCode()
                }
            }
            
            baixingBtnLogin.setClick {
                baixing_agress {
                    baixing_login()
                }
            }
        }
        mBaixing_binding.baixingCheckboxAgreement.movementMethod = LinkMovementMethod.getInstance()
        val content = mBaixing_binding.baixingCheckboxAgreement.text.toString()
        val spannableString = SpannableString(content)
        val userAgreementStart = content.indexOf("《用户协议》")
        val userAgreementEnd = userAgreementStart + "《用户协议》".length
        if (userAgreementStart >= 0) {
            spannableString.setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        Baixing_GoRouter.baixing_jumpWebActivity(taskName = "用户协议", taskType = "Baixing_PrivacyAgreementTaskManager")
                        CenterToast.show(activity, "《用户协议》")
                    }
                },
                userAgreementStart,
                userAgreementEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan("#87CEEB".toColorInt()),
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
                        Baixing_GoRouter.baixing_jumpWebActivity(
                            taskName = "隐私政策",
                            taskType = "Baixing_PrivacyAgreementTaskManager"
                        )
                        CenterToast.show(activity, "《隐私政策》")
                    }
                },
                privacyPolicyStart,
                privacyPolicyEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan("#87CEEB".toColorInt()),
                privacyPolicyStart,
                privacyPolicyEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        mBaixing_binding.baixingCheckboxAgreement.text = spannableString
    }
    
    private fun baixing_sendVerificationCode() {
        mBaixing_viewModel.baixing_sendVerificationCode(mBaixing_binding.baixingEditPhone.text.toString())
    }
    
    private fun baixing_login() {
        activity?:return
        mBaixing_viewModel.baixing_login(
            requireActivity().applicationContext,
            mBaixing_binding.baixingEditPhone.text.toString(),
            mBaixing_binding.baixingEditCode.text.toString(),
            )
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        mBaixing_loading?.dismiss()
    }

    private fun baixing_agress(doafter: (() -> Unit)? = null) {
        if (!mBaixing_binding.baixingCheckboxAgreement.isChecked) {
            baixing_startShakeAnimation()
            return
        }
        doafter?.invoke()
    }

    private fun baixing_startShakeAnimation() {
        val shakeAnimatorX = ObjectAnimator.ofFloat(mBaixing_binding.baixingCheckboxAgreement,
            "translationX", 0f, 20f, -20f, 20f, -20f, 10f, -10f, 0f)
        shakeAnimatorX.duration = 500
        shakeAnimatorX.start()
        if (activity is Baixing_BaseActivity) {
            (activity as Baixing_BaseActivity).vibrateOnce()
        }
    }
}