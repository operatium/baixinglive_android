package com.baixingkuaizu.live.android.activity

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.baixingkuaizu.live.android.adatperandroid.Baixing_AdapterHelper.setClick
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import com.baixingkuaizu.live.android.databinding.BaixingVideoPlayerActivityBinding
import com.baixingkuaizu.live.android.widget.toast.CenterToast

/**
 * @author yuyuexing
 * @date: 2025/4/16
 * @description: 视频播放器活动页面，负责播放青少年模式中点击的视频。使用VideoView实现视频播放功能，支持显示标题、加载进度以及错误处理机制，通过Intent获取视频URL和标题等信息。
 */
class Baixing_VideoPlayerActivity : Baixing_BaseActivity() {
    
    private lateinit var mBaixing_binding: BaixingVideoPlayerActivityBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBaixing_binding = BaixingVideoPlayerActivityBinding.inflate(layoutInflater)
        setContentView(mBaixing_binding.root)
        mBaixing_binding.root.setWindowListener()
        
        val mBaixing_videoTitle = intent.getStringExtra("video_title") ?: "未知视频"
        val mBaixing_videoUrl = intent.getStringExtra("video_url") ?: ""
        
        mBaixing_binding.baixingVideoTitle.text = mBaixing_videoTitle
        
        mBaixing_binding.baixingBack.setClick {
            finish()
        }
        
        baixing_playVideo(mBaixing_videoUrl)
    }
    
    private fun baixing_playVideo(videoUrl: String) {
        if (videoUrl.isEmpty()) {
            CenterToast.show(this, "视频地址无效")
            mBaixing_binding.baixingLoadingProgress.visibility = View.GONE
            return
        }
        
        val videoUri = Uri.parse(videoUrl)
        
        mBaixing_binding.baixingVideoView.setOnPreparedListener { mediaPlayer ->
            mBaixing_binding.baixingLoadingProgress.visibility = View.GONE
            
            mediaPlayer.isLooping = true
            
            mediaPlayer.start()
        }
        
        mBaixing_binding.baixingVideoView.setOnErrorListener { _, _, _ ->
            CenterToast.show(this, "视频播放失败")
            mBaixing_binding.baixingLoadingProgress.visibility = View.GONE
            true
        }
        
        mBaixing_binding.baixingVideoView.setVideoURI(videoUri)
        
        mBaixing_binding.baixingVideoView.requestFocus()
    }
    
    override fun onPause() {
        super.onPause()
        if (mBaixing_binding.baixingVideoView.isPlaying) {
            mBaixing_binding.baixingVideoView.pause()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        mBaixing_binding.baixingVideoView.stopPlayback()
    }
} 