package com.baixingkuaizu.live.android.busiess.livefragment

import android.view.View
import com.baixingkuaizu.live.android.databinding.BaixingLiveItemBinding

class Baixing_GirlFootViewHolder(private val mBaixing_binding: BaixingLiveItemBinding)
    : Baixing_LiveViewHolder(mBaixing_binding) {

    override fun baixing_bind(liveData: Baixing_LiveDataEntity) {
        mBaixing_binding.baixingFoot.visibility = View.VISIBLE
        mBaixing_binding.baixingLiveLayout.visibility = View.GONE
    }

}