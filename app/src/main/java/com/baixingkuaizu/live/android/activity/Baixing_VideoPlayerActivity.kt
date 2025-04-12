package com.baixingkuaizu.live.android.activity

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import com.baixingkuaizu.live.android.adatperandroid.Baixing_AdapterHelper.setClick
import com.baixingkuaizu.live.android.base.Baixing_BaseActivity
import com.baixingkuaizu.live.android.databinding.BaixingVideoPlayerActivityBinding
import com.baixingkuaizu.live.android.widget.toast.CenterToast
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util

/**
 * @author yuyuexing
 * @date: 2025/4/16
 * @description: 视频播放器活动页面，负责播放青少年模式中点击的视频。使用ExoPlayer实现视频播放功能，支持显示标题、加载进度以及错误处理机制，通过Intent获取视频URL和标题等信息。特别优化了M3U8等流媒体格式的播放。
 */
class Baixing_VideoPlayerActivity : Baixing_BaseActivity() {
    
    private lateinit var mBaixing_binding: BaixingVideoPlayerActivityBinding
    private var mBaixing_player: SimpleExoPlayer? = null
    private var mBaixing_playWhenReady = true
    private var mBaixing_currentWindow = 0
    private var mBaixing_playbackPosition = 0L
    
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
        
        if (mBaixing_videoUrl.isNotEmpty()) {
            baixing_initializePlayer(mBaixing_videoUrl)
        } else {
            baixing_initializePlayer("https://medias.kouyujie.cn/longtLshnk9NAaSbKBR-Fm6mycMQ")
        }
    }
    
    private fun baixing_initializePlayer(videoUrl: String) {
        if (videoUrl.isEmpty()) {
            CenterToast.show(this, "视频地址无效")
            mBaixing_binding.baixingLoadingProgress.visibility = View.GONE
            return
        }
        
        try {
            // 创建播放器实例
            mBaixing_player = SimpleExoPlayer.Builder(this)
                .build()
                .also { exoPlayer ->
                    // 绑定播放器到视图
                    mBaixing_binding.baixingPlayerView.player = exoPlayer
                    
                    // 创建适合的媒体源
                    val mediaSource = baixing_buildMediaSource(videoUrl)
                    
                    // 准备播放器
                    exoPlayer.setMediaSource(mediaSource)
                    exoPlayer.playWhenReady = mBaixing_playWhenReady
                    exoPlayer.seekTo(mBaixing_currentWindow, mBaixing_playbackPosition)
                    
                    // 添加监听器
                    exoPlayer.addListener(baixing_createPlayerListener())
                    
                    exoPlayer.prepare()
                }
        } catch (e: Exception) {
            CenterToast.show(this, "视频初始化失败: ${e.message}")
            mBaixing_binding.baixingLoadingProgress.visibility = View.GONE
        }
    }
    
    private fun baixing_buildMediaSource(videoUrl: String): MediaSource {
        // 创建HTTP数据源工厂
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)
            .setConnectTimeoutMs(15000)
            .setReadTimeoutMs(15000)
        
        // 创建默认数据源工厂
        val dataSourceFactory = DefaultDataSourceFactory(this, httpDataSourceFactory)
        
        // 根据URL类型创建不同的媒体源
        return if (videoUrl.endsWith(".m3u8", ignoreCase = true)) {
            // HLS格式（M3U8）媒体源
            HlsMediaSource.Factory(dataSourceFactory)
                .setAllowChunklessPreparation(true)
                .createMediaSource(MediaItem.fromUri(videoUrl))
        } else {
            // 常规格式媒体源
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(videoUrl))
        }
    }
    
    private fun baixing_createPlayerListener(): Player.Listener {
        return object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_BUFFERING -> {
                        mBaixing_binding.baixingLoadingProgress.visibility = View.VISIBLE
                    }
                    Player.STATE_READY -> {
                        mBaixing_binding.baixingLoadingProgress.visibility = View.GONE
                    }
                    Player.STATE_ENDED -> {
                        // 自动循环播放
                        mBaixing_player?.seekTo(0)
                        mBaixing_player?.play()
                    }
                    Player.STATE_IDLE -> {
                        // 空闲状态，可能是播放出错
                    }
                }
            }
            
            override fun onPlayerError(error: ExoPlaybackException) {
                val errorMessage = when (error.type) {
                    ExoPlaybackException.TYPE_SOURCE -> "视频源错误: ${error.sourceException.message}"
                    ExoPlaybackException.TYPE_RENDERER -> "渲染错误: ${error.rendererException.message}"
                    ExoPlaybackException.TYPE_UNEXPECTED -> "意外错误: ${error.unexpectedException.message}"
                    ExoPlaybackException.TYPE_REMOTE -> "远程错误"
                    else -> "未知错误: ${error.message}"
                }
                
                CenterToast.show(this@Baixing_VideoPlayerActivity, errorMessage)
                mBaixing_binding.baixingLoadingProgress.visibility = View.GONE
            }
        }
    }
    
    private fun baixing_releasePlayer() {
        mBaixing_player?.let { player ->
            mBaixing_playbackPosition = player.currentPosition
            mBaixing_currentWindow = player.currentWindowIndex
            mBaixing_playWhenReady = player.playWhenReady
            player.release()
        }
        mBaixing_player = null
    }
    
    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            baixing_initializePlayer(intent.getStringExtra("video_url") ?: "https://medias.kouyujie.cn/longtLshnk9NAaSbKBR-Fm6mycMQ")
        }
    }
    
    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || mBaixing_player == null) {
            baixing_initializePlayer(intent.getStringExtra("video_url") ?: "https://medias.kouyujie.cn/longtLshnk9NAaSbKBR-Fm6mycMQ")
        }
    }
    
    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            baixing_releasePlayer()
        }
    }
    
    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            baixing_releasePlayer()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        baixing_releasePlayer()
    }
} 