package com.baixingkuaizu.live.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.router.Baixing_GoRouter
import com.baixingkuaizu.live.android.databinding.BaixingSelectLoginFragmentBinding

class Baixing_SelectLoginFragment : Baixing_BaseFragment() {
    private lateinit var mBaixing_Binding: BaixingSelectLoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBaixing_Binding = BaixingSelectLoginFragmentBinding.inflate(inflater)
        mBaixing_Binding.baixingPhone.setOnClickListener {
            goLogin()
        }
        mBaixing_Binding.baixingMore.setOnClickListener {
            goLogin()
        }
        return mBaixing_Binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun goLogin() {
        Baixing_GoRouter.baixing_jumpLoginActivity()
        requireActivity().finish()
    }
}
