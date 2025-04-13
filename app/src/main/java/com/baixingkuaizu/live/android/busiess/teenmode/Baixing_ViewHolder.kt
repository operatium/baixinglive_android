package com.baixingkuaizu.live.android.busiess.teenmode

import androidx.recyclerview.widget.RecyclerView
import com.baixingkuaizu.live.android.adatperandroid.Baixing_AdapterHelper.setClick
import com.baixingkuaizu.live.android.busiess.router.Baixing_GoRouter
import com.baixingkuaizu.live.android.databinding.BaixingPlayListItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.Target

/**
 * @author yuyuexing
 * @date: 2025/4/11
 * @description: 播放列表的ViewHolder

 */
class Baixing_ViewHolder(binding: BaixingPlayListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private var mBaixing_imageLoadRequest: Target<*>? = null
    private lateinit var mBaixing_requestManager: RequestManager
    val mBaixing_cover = binding.baixingVideoCover
    val mBaixing_title = binding.baixingVideoTitle
    val mBaixing_author = binding.baixingVideoAuthor
    val mBaixing_duration = binding.baixingVideoDuration
    val mBaixing_tag = binding.baixingVideoTag
    
    fun baixing_bind(video: Baixing_VideoData) {
        mBaixing_title.text = video.mBaixing_title
        mBaixing_author.text = video.mBaixing_author
        mBaixing_duration.text = video.mBaixing_duration
        mBaixing_tag.text = video.mBaixing_tag
        
        baixing_unbind()
        
        mBaixing_requestManager = Glide.with(itemView.context)
        
        mBaixing_imageLoadRequest = if (video.mBaixing_coverUrl.isNotEmpty()) {
            mBaixing_requestManager.load(video.mBaixing_coverUrl).into(mBaixing_cover)
        } else {
            mBaixing_requestManager.load(video.mBaixing_videoUrl).into(mBaixing_cover)
        }
        
        itemView.setClick {
            Baixing_GoRouter.baixing_jumpVideoPlayerActivity(video)
        }
    }
    
    fun baixing_unbind() {
        if (mBaixing_imageLoadRequest != null) {
            try {
                if (::mBaixing_requestManager.isInitialized) {
                    mBaixing_requestManager.clear(mBaixing_imageLoadRequest)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            mBaixing_imageLoadRequest = null
        }
    }
}