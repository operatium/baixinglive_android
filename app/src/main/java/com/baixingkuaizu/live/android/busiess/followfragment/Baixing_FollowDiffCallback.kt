package com.baixingkuaizu.live.android.busiess.followfragment

import androidx.recyclerview.widget.DiffUtil

/**
 * @author yuyuexing
 * @date: 2025/4/13
 * @description: 关注列表的DiffCallback，用于RecyclerView的高效更新
 */
object Baixing_FollowDiffCallback : DiffUtil.ItemCallback<Baixing_FollowGirlEntity>() {
    override fun areItemsTheSame(oldItem: Baixing_FollowGirlEntity, newItem: Baixing_FollowGirlEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Baixing_FollowGirlEntity, newItem: Baixing_FollowGirlEntity): Boolean {
        return oldItem == newItem
    }
}