package com.baixingkuaizu.live.android.activity

import android.os.Bundle
import android.view.View
import com.baixingkuaizu.live.android.adatperandroid.Baixing_AdapterHelper.setClick
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import com.baixingkuaizu.live.android.databinding.BaixingVideoPlayerActivityBinding
import com.baixingkuaizu.live.android.widget.toast.CenterToast
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils

/**
 * @author yuyuexing
 * @date: 2025/4/16
 * @description: 视频播放器活动页面，负责播放青少年模式中点击的视频。使用GSYVideoPlayer实现视频播放功能，支持显示标题、加载进度以及错误处理机制，通过Intent获取视频URL和标题等信息。特别优化了M3U8等流媒体格式的播放。
 */
class Baixing_VideoPlayerActivity : Baixing_BaseActivity() {
    
    private lateinit var mBaixing_binding: BaixingVideoPlayerActivityBinding
    private var mBaixing_orientationUtils: OrientationUtils? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBaixing_binding = BaixingVideoPlayerActivityBinding.inflate(layoutInflater)
        setContentView(mBaixing_binding.root)
        mBaixing_binding.root.setWindowListener()
        
        val mBaixing_videoTitle = intent.getStringExtra("video_title") ?: "未知视频"
        val mBaixing_videoUrl = intent.getStringExtra("video_url") ?: ""
        
        mBaixing_binding.baixingVideoTitle.text = mBaixing_videoTitle
        
        mBaixing_binding.baixingBack.setClick {
            onBackPressed()
        }
        
        baixing_initializePlayer(mBaixing_videoTitle, mBaixing_videoUrl)
    }
    
    private fun baixing_initializePlayer(videoTitle: String, videoUrl: String) {
        if (videoUrl.isEmpty()) {
            CenterToast.show(this, "视频地址无效")
            mBaixing_binding.baixingLoadingProgress.visibility = View.GONE
            return
        }
        
        try {
            // 设置播放器标题
            mBaixing_binding.baixingVideoPlayer.setUp(videoUrl, true, videoTitle)
            
            // 设置全屏按键功能
            mBaixing_orientationUtils = OrientationUtils(this, mBaixing_binding.baixingVideoPlayer)
            
            // 设置返回按键功能
            mBaixing_binding.baixingVideoPlayer.backButton.visibility = View.VISIBLE
            mBaixing_binding.baixingVideoPlayer.backButton.setOnClickListener {
                onBackPressed()
            }
            
            // 设置旋转
            mBaixing_binding.baixingVideoPlayer.fullscreenButton.setOnClickListener {
                mBaixing_orientationUtils?.resolveByClick()
                mBaixing_binding.baixingVideoPlayer.startWindowFullscreen(this, true, true)
            }
            
            // 设置播放状态回调
            mBaixing_binding.baixingVideoPlayer.setVideoAllCallBack(object : GSYSampleCallBack() {
                override fun onPrepared(url: String?, vararg objects: Any?) {
                    super.onPrepared(url, *objects)
                    mBaixing_binding.baixingLoadingProgress.visibility = View.GONE
                    mBaixing_orientationUtils?.isEnable = true
                }
                
                override fun onPlayError(url: String?, vararg objects: Any?) {
                    super.onPlayError(url, *objects)
                    CenterToast.show(this@Baixing_VideoPlayerActivity, "播放错误")
                    mBaixing_binding.baixingLoadingProgress.visibility = View.GONE
                }
                
                override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                    super.onQuitFullscreen(url, *objects)
                    mBaixing_orientationUtils?.backToProtVideo()
                }
            })
            
            // 设置自动播放
            mBaixing_binding.baixingVideoPlayer.startPlayLogic()
            
        } catch (e: Exception) {
            CenterToast.show(this, "视频初始化失败: ${e.message}")
            mBaixing_binding.baixingLoadingProgress.visibility = View.GONE
        }
    }
    
    override fun onBackPressed() {
        // 先返回正常状态
        if (mBaixing_orientationUtils?.screenType == 1) {
            mBaixing_binding.baixingVideoPlayer.onBackFullscreen()
            return
        }
        
        // 释放所有
        mBaixing_binding.baixingVideoPlayer.releaseVideos()
        GSYVideoManager.releaseAllVideos()
        super.onBackPressed()
    }
    
    override fun onPause() {
        mBaixing_binding.baixingVideoPlayer.currentPlayer.onVideoPause()
        super.onPause()
    }
    
    override fun onResume() {
        mBaixing_binding.baixingVideoPlayer.currentPlayer.onVideoResume()
        super.onResume()
    }
    
    override fun onDestroy() {
        GSYVideoManager.releaseAllVideos()
        mBaixing_orientationUtils?.releaseListener()
        super.onDestroy()
    }
} 