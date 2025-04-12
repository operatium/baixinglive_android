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
        
        // 获取传递的数据
        val mBaixing_videoTitle = intent.getStringExtra("video_title") ?: "未知视频"
        val mBaixing_videoUrl = intent.getStringExtra("video_url") ?: ""
        
        // 设置标题
        mBaixing_binding.baixingVideoTitle.text = mBaixing_videoTitle
        
        // 设置返回按钮点击事件
        mBaixing_binding.baixingBack.setClick {
            finish()
        }
        
        // 播放视频
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
            // 隐藏加载进度条
            mBaixing_binding.baixingLoadingProgress.visibility = View.GONE
            
            // 设置循环播放
            mediaPlayer.isLooping = true
            
            // 开始播放
            mediaPlayer.start()
        }
        
        mBaixing_binding.baixingVideoView.setOnErrorListener { _, _, _ ->
            // 显示错误信息
            CenterToast.show(this, "视频播放失败")
            mBaixing_binding.baixingLoadingProgress.visibility = View.GONE
            true
        }
        
        // 设置视频路径
        mBaixing_binding.baixingVideoView.setVideoURI(videoUri)
        
        // 请求获取焦点
        mBaixing_binding.baixingVideoView.requestFocus()
    }
    
    override fun onPause() {
        super.onPause()
        // 暂停视频播放
        if (mBaixing_binding.baixingVideoView.isPlaying) {
            mBaixing_binding.baixingVideoView.pause()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // 释放资源
        mBaixing_binding.baixingVideoView.stopPlayback()
    }
} 