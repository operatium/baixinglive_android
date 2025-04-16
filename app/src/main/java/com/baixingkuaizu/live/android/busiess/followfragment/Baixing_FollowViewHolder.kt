package com.baixingkuaizu.live.android.busiess.followfragment

import androidx.recyclerview.widget.RecyclerView
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.databinding.BaixingFollowItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager

/**
 * @author yuyuexing
 * @date: 2025/4/13
 * @description: 关注列表的ViewHolder，负责绑定数据到视图
 */
class Baixing_FollowViewHolder(private val binding: BaixingFollowItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private var glideRequestManager: RequestManager? = null
    
    fun bind(item: Baixing_FollowGirlEntity) {
        binding.apply {
            glideRequestManager = Glide.with(baixingIvAvatar)
            glideRequestManager
                ?.load(item.image)
                ?.placeholder(R.drawable.baixing_def_cover)
                ?.into(baixingIvAvatar)

            baixingTvName.text = item.name
            baixingTvAttention.text = item.attention.toString()
        }
    }
    
    fun unbind() {
        try {
            glideRequestManager?.clear(binding.baixingIvAvatar)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        glideRequestManager = null
    }
}