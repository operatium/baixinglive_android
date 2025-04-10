package com.baixingkuaizu.live.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.baixingkuaizu.live.android.databinding.BaixingFragmentLoginBinding

class Baixing_LoginFragment : Fragment() {
    
    private var mBaixing_binding: BaixingFragmentLoginBinding? = null
    private var mBaixing_phoneNumber: String = ""
    private var mBaixing_verificationCode: String = ""
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBaixing_binding = BaixingFragmentLoginBinding.inflate(inflater, container, false)
        
        return mBaixing_binding?.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baixing_initView()
    }
    
    private fun baixing_initView() {
        mBaixing_binding?.apply {
            baixingBtnSendCode.setOnClickListener {
                mBaixing_phoneNumber = baixingEditPhone.text.toString()
                baixing_sendVerificationCode()
            }
            
            baixingBtnLogin.setOnClickListener {
                mBaixing_verificationCode = baixingEditCode.text.toString()
                baixing_login()
            }
        }
    }
    
    private fun baixing_sendVerificationCode() {
    }
    
    private fun baixing_login() {
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        mBaixing_binding = null
    }
}