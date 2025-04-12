package com.baixingkuaizu.live.android.activity

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import com.baixingkuaizu.live.android.adatperandroid.Baixing_AdapterHelper.setClick
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import com.baixingkuaizu.live.android.databinding.BaixingVideoPlayerActivityBinding
import com.baixingkuaizu.live.android.widget.toast.CenterToast

/**
 * @author yuyuexing
 * @date: 2025/4/16
 * @description: 视频播放器活动页面，负责播放青少年模式中点击的视频。使用VideoView实现视频播放功能，支持显示标题、加载进度以及错误处理机制，通过Intent获取视频URL和标题等信息。支持MP4、M3U8等多种视频格式。
 */
class Baixing_VideoPlayerActivity : Baixing_BaseActivity() {
    
    private lateinit var mBaixing_binding: BaixingVideoPlayerActivityBinding
    private var mBaixing_isM3u8: Boolean = false
    
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
        
        mBaixing_isM3u8 = videoUrl.endsWith(".m3u8", ignoreCase = true)
        
        try {
            val videoUri = videoUrl.toUri()
            
            mBaixing_binding.baixingVideoView.setOnPreparedListener { mediaPlayer ->
                mBaixing_binding.baixingLoadingProgress.visibility = View.GONE
                baixing_configureMediaPlayer(mediaPlayer)
                mediaPlayer.start()
            }
            
            mBaixing_binding.baixingVideoView.setOnErrorListener { _, what, extra ->
                baixing_handlePlaybackError(what, extra)
                true
            }
            
            mBaixing_binding.baixingVideoView.setOnCompletionListener {
                if (mBaixing_isM3u8) {
                    mBaixing_binding.baixingVideoView.start()
                }
            }
            
            mBaixing_binding.baixingVideoView.setVideoURI(videoUri)
            mBaixing_binding.baixingVideoView.requestFocus()
        } catch (e: Exception) {
            CenterToast.show(this, "视频初始化失败: ${e.message}")
            mBaixing_binding.baixingLoadingProgress.visibility = View.GONE
        }
    }
    
    private fun baixing_configureMediaPlayer(mediaPlayer: MediaPlayer) {
        mediaPlayer.isLooping = !mBaixing_isM3u8
        
        if (mBaixing_isM3u8) {
            mediaPlayer.setOnInfoListener { _, what, _ ->
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    mBaixing_binding.baixingLoadingProgress.visibility = View.VISIBLE
                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    mBaixing_binding.baixingLoadingProgress.visibility = View.GONE
                }
                false
            }
        }
    }
    
    private fun baixing_handlePlaybackError(what: Int, extra: Int) {
        val errorMessage = when (what) {
            MediaPlayer.MEDIA_ERROR_SERVER_DIED -> "服务器连接中断"
            MediaPlayer.MEDIA_ERROR_IO -> "网络连接错误"
            MediaPlayer.MEDIA_ERROR_MALFORMED -> "媒体格式不支持"
            MediaPlayer.MEDIA_ERROR_UNSUPPORTED -> "不支持的媒体类型"
            MediaPlayer.MEDIA_ERROR_TIMED_OUT -> "连接超时"
            else -> "视频播放失败 (错误码: $what, $extra)"
        }
        
        CenterToast.show(this, errorMessage)
        mBaixing_binding.baixingLoadingProgress.visibility = View.GONE
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