package com.baixingkuaizu.live.android.busiess.livefragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.baixingkuaizu.live.android.databinding.BaixingLiveItemBinding

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 直播列表适配器
 */
class Baixing_LiveListAdapter : ListAdapter<Baixing_LiveDataEntity, Baixing_LiveViewHolder>(
    object : DiffUtil.ItemCallback<Baixing_LiveDataEntity>() {
        override fun areItemsTheSame(oldItem: Baixing_LiveDataEntity, newItem: Baixing_LiveDataEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Baixing_LiveDataEntity, newItem: Baixing_LiveDataEntity): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Baixing_LiveViewHolder {
        val binding = BaixingLiveItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Baixing_LiveViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Baixing_LiveViewHolder, position: Int) {
        val item = getItem(position)
        holder.baixing_bind(item)
    }

    override fun onViewRecycled(holder: Baixing_LiveViewHolder) {
        super.onViewRecycled(holder)
        holder.baixing_clearImage()
    }
}