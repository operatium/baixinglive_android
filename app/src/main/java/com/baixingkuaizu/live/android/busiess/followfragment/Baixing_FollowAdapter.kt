package com.baixingkuaizu.live.android.busiess.followfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.baixingkuaizu.live.android.databinding.BaixingFollowItemBinding

/**
 * @author yuyuexing
 * @date: 2025/4/13
 * @description: 关注列表的适配器，使用提取出的DiffCallback和ViewHolder
 */
class Baixing_FollowAdapter : ListAdapter<Baixing_FollowGirlEntity, Baixing_FollowViewHolder>(Baixing_FollowDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Baixing_FollowViewHolder {
        val binding = BaixingFollowItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Baixing_FollowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Baixing_FollowViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    override fun onViewRecycled(holder: Baixing_FollowViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }
}