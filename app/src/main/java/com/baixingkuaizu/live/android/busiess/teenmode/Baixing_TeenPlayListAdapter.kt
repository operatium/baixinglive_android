package com.baixingkuaizu.live.android.busiess.teenmode

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.baixingkuaizu.live.android.adatperandroid.Baixing_AdapterHelper.setClick
import com.baixingkuaizu.live.android.databinding.BaixingPlayListItemBinding
import com.bumptech.glide.Glide

/**
 * @author yuyuexing
 * @date: 2025/4/14
 * @description: 青少年模式播放列表适配器，使用DiffUtil优化更新性能
 */
class Baixing_TeenPlayListAdapter(
    private val mBaixing_onItemClick: (Baixing_VideoData) -> Unit
) : ListAdapter<Baixing_VideoData, Baixing_ViewHolder>(DIFF_CALLBACK) {

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
        
        holder.mBaixing_title.text = video.mBaixing_title
        holder.mBaixing_author.text = video.mBaixing_author
        holder.mBaixing_duration.text = video.mBaixing_duration
        holder.mBaixing_tag.text = video.mBaixing_tag
        
        if (video.mBaixing_coverUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context).load(video.mBaixing_coverUrl).into(holder.mBaixing_cover)
        } else {
            Glide.with(holder.itemView.context).load(com.baixingkuaizu.live.android.R.drawable.baixing_default_cover).into(holder.mBaixing_cover)
        }
        
        holder.itemView.setClick {
            mBaixing_onItemClick(video)
        }
    }

    /**
     * 根据标签过滤视频
     */
    fun baixing_filterByTag(tag: String) {
        val filteredList = Baixing_VideoDataCache.getInstance().baixing_filterByTag(tag)
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
        // 初始化时从缓存管理类获取所有视频数据
        submitList(Baixing_VideoDataCache.getInstance().baixing_getAllVideos())
    }
}