package com.baixingkuaizu.live.android.busiess.livefragment

import androidx.recyclerview.widget.DiffUtil

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 直播列表的DiffCallback，用于RecyclerView的高效更新
 */
object Baixing_GirlDiffCallback : DiffUtil.ItemCallback<Baixing_GirlDataEntity>() {
    override fun areItemsTheSame(oldItem: Baixing_GirlDataEntity, newItem: Baixing_GirlDataEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Baixing_GirlDataEntity, newItem: Baixing_GirlDataEntity): Boolean {
        return oldItem == newItem
    }
}