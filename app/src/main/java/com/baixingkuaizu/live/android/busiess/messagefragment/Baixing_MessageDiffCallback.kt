package com.baixingkuaizu.live.android.busiess.messagefragment

import androidx.recyclerview.widget.DiffUtil

class Baixing_MessageDiffCallback : DiffUtil.ItemCallback<Baixing_MessageItemEntity>() {
    override fun areItemsTheSame(oldItem: Baixing_MessageItemEntity, newItem: Baixing_MessageItemEntity): Boolean {
        return oldItem.baixing_id == newItem.baixing_id
    }

    override fun areContentsTheSame(oldItem: Baixing_MessageItemEntity, newItem: Baixing_MessageItemEntity): Boolean {
        return oldItem == newItem
    }
} 