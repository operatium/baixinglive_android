package com.baixingkuaizu.live.android.busiess.teenmode

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.baixingkuaizu.live.android.databinding.BaixingPlayListItemBinding

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 播放列表的Adapter
 */
class Baixing_TeenPlayListAdapter: ListAdapter<Baixing_VideoData, Baixing_ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Baixing_ViewHolder {
        val binding = BaixingPlayListItemBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return Baixing_ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Baixing_ViewHolder, position: Int) {
        val video = getItem(position)
        holder.baixing_bind(video)
    }
    
    override fun onViewRecycled(holder: Baixing_ViewHolder) {
        super.onViewRecycled(holder)
        holder.baixing_unbind()
    }

    fun baixing_filterByTag(tag: String) {
        val filteredList = Baixing_VideoDataCache.baixing_filterByTag(tag)
        submitList(filteredList)
    }
    
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Baixing_VideoData>() {
            override fun areItemsTheSame(oldItem: Baixing_VideoData, newItem: Baixing_VideoData): Boolean {
                return oldItem.mBaixing_id == newItem.mBaixing_id
            }

            override fun areContentsTheSame(oldItem: Baixing_VideoData, newItem: Baixing_VideoData): Boolean {
                return oldItem.mBaixing_videoUrl == newItem.mBaixing_videoUrl
            }
        }
    }
    
    init {
        submitList(Baixing_VideoDataCache.baixing_getAllVideos())
    }
}