package com.baixingkuaizu.live.android.busiess.livefragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.baixingkuaizu.live.android.databinding.BaixingLiveItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 直播列表适配器
 */
class Baixing_LiveListAdapter : ListAdapter<Baixing_LiveData, Baixing_LiveViewHolder>(
    object : DiffUtil.ItemCallback<Baixing_LiveData>() {
        override fun areItemsTheSame(oldItem: Baixing_LiveData, newItem: Baixing_LiveData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Baixing_LiveData, newItem: Baixing_LiveData): Boolean {
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
        // 清除图片加载请求，避免错位问题
        holder.baixing_clearImage()
    }
}