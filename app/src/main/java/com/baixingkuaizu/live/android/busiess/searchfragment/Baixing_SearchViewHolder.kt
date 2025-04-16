package com.baixingkuaizu.live.android.busiess.searchfragment

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.databinding.BaixingSearchResultItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 搜索结果视图持有者，负责绑定搜索结果数据到视图
 */
class Baixing_SearchViewHolder(private val binding: BaixingSearchResultItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private var glideRequestManager: RequestManager? = null
    
    fun bind(item: Baixing_SearchAnchorEntity) {
        binding.baixingTvAnchorName.text = item.anchorName
        binding.baixingTvCategory.text = item.category
        binding.baixingTvViewerCount.text = "${item.viewerCount}人观看"
        binding.baixingTvLiveStatus.isVisible = item.isLive
        glideRequestManager = Glide.with(binding.baixingIvCover)
        glideRequestManager
            ?.load(item.coverUrl)
            ?.apply(RequestOptions().centerCrop())
            ?.placeholder(R.drawable.baixing_default_cover)
            ?.error(R.drawable.baixing_default_cover)
            ?.into(binding.baixingIvCover)
    }
    
    fun unbind() {
        try {
            glideRequestManager?.clear(binding.baixingIvCover)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        glideRequestManager = null
    }
}