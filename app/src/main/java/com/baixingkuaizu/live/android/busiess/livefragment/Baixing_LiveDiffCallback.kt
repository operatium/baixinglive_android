package com.baixingkuaizu.live.android.busiess.livefragment

import androidx.recyclerview.widget.DiffUtil

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 直播列表的DiffCallback，用于RecyclerView的高效更新
 */
object Baixing_LiveDiffCallback : DiffUtil.ItemCallback<Baixing_LiveDataEntity>() {
    override fun areItemsTheSame(oldItem: Baixing_LiveDataEntity, newItem: Baixing_LiveDataEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Baixing_LiveDataEntity, newItem: Baixing_LiveDataEntity): Boolean {
        return oldItem == newItem
    }
}