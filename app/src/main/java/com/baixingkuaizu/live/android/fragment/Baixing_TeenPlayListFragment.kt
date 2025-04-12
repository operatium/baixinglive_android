package com.baixingkuaizu.live.android.fragment

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.busiess.teenmode.Baixing_TeenPlayListAdapter
import com.baixingkuaizu.live.android.databinding.BaixingPlayListFragmentBinding
import com.baixingkuaizu.live.android.dialog.Baixing_ExitDialog
import com.baixingkuaizu.live.android.dialog.Baixing_TeenModeExtendTimeDialog
import com.baixingkuaizu.live.android.widget.toast.CenterToast
import java.util.concurrent.TimeUnit

/**
 * @author yuyuexing
 * @date: 2025/4/14
 * @description: 青少年模式播放列表页面
 */
class Baixing_TeenPlayListFragment : Baixing_BaseFragment() {
    
    private var _mBaixing_binding: BaixingPlayListFragmentBinding? = null
    private val mBaixing_binding get() = _mBaixing_binding!!
    
    private lateinit var mBaixing_adapter: Baixing_TeenPlayListAdapter
    private lateinit var mBaixing_localDataManager: Baixing_LocalDataManager
    
    // 标签按钮列表
    private lateinit var mBaixing_tagButtons: List<TextView>
    
    // 使用时间相关
    private var mBaixing_usedTime: Long = 0
    private var mBaixing_maxTime: Long = TimeUnit.MINUTES.toMillis(40) // 40分钟
    private val mBaixing_updateInterval: Long = 60000 // 1分钟更新一次
    
    // 密码验证对话框
    private var mBaixing_passwordVerificationDialog: Dialog? = null
    
    // 处理器用于定时任务
    private val mBaixing_handler = Handler(Looper.getMainLooper())
    
    // 用于定时更新使用时间
    private val mBaixing_updateTimeRunnable = object : Runnable {
        override fun run() {
            baixing_updateUsageTime()
            mBaixing_handler.postDelayed(this, mBaixing_updateInterval)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBaixing_binding = BaixingPlayListFragmentBinding.inflate(inflater, container, false)
        return mBaixing_binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        mBaixing_localDataManager = Baixing_LocalDataManager.getInstance()
        
        // 初始化视图
        baixing_initViews()
        // 设置监听器
        baixing_setupListeners()
        // 初始化数据
        baixing_initData()
    }
    
    override fun onResume() {
        super.onResume()
        // 加载已使用时间
        mBaixing_usedTime = mBaixing_localDataManager.baixing_getTodayUsedDuration()
        baixing_updateUsageTimeText()
        
        // 检查是否需要显示验证对话框
        val lastVerifiedTime = mBaixing_localDataManager.baixing_getLastVerifiedTime()
        val currentTime = System.currentTimeMillis()
        
        if (mBaixing_usedTime >= mBaixing_maxTime && 
            (currentTime - lastVerifiedTime) >= mBaixing_maxTime) {
            // 如果使用时间超过40分钟且自上次验证已经过了40分钟，则显示验证对话框
            baixing_showPasswordVerificationForTimeLimit()
        } else {
            // 否则，开始计时
            mBaixing_handler.postDelayed(mBaixing_updateTimeRunnable, mBaixing_updateInterval)
        }
    }
    
    override fun onPause() {
        super.onPause()
        // 停止记录使用时间
        mBaixing_handler.removeCallbacks(mBaixing_updateTimeRunnable)
        // 保存已使用时间
        mBaixing_localDataManager.baixing_setTodayUsedDuration(mBaixing_usedTime)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _mBaixing_binding = null
    }
    
    /**
     * 初始化视图
     */
    private fun baixing_initViews() {
        // 标签按钮
        mBaixing_tagButtons = listOf(
            mBaixing_binding.baixingTagAll,
            mBaixing_binding.baixingTagEducation,
            mBaixing_binding.baixingTagScience,
            mBaixing_binding.baixingTagNature,
            mBaixing_binding.baixingTagHistory,
            mBaixing_binding.baixingTagCartoon
        )
    }
    
    /**
     * 设置监听器
     */
    private fun baixing_setupListeners() {
        // 返回按钮点击事件
        mBaixing_binding.baixingBack.setOnClickListener {
            baixing_showPasswordVerificationForExit()
        }
        
        // 退出青少年模式按钮点击事件
        mBaixing_binding.baixingExitTeenMode.setOnClickListener {
            baixing_showPasswordVerificationForExit()
        }
        
        // 标签按钮点击事件
        mBaixing_tagButtons.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                baixing_selectTag(index)
            }
        }
    }
    
    /**
     * 初始化数据
     */
    private fun baixing_initData() {
        // 初始化RecyclerView和适配器
        mBaixing_adapter = Baixing_TeenPlayListAdapter(emptyList()) { video ->
            // 点击视频项的处理
            CenterToast.show(requireActivity(), "播放: ${video.mBaixing_title}")
        }
        
        mBaixing_binding.baixingRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        mBaixing_binding.baixingRecyclerView.adapter = mBaixing_adapter
        
        // 加载初始数据
        baixing_loadInitialData()
    }
    
    /**
     * 加载初始数据
     */
    private fun baixing_loadInitialData() {
        // 使用适配器的静态列表数据
        mBaixing_adapter.baixing_filterByTag("全部")
    }
    
    /**
     * 选择标签
     * @param selectedIndex 所选标签的索引
     */
    private fun baixing_selectTag(selectedIndex: Int) {
        mBaixing_tagButtons.forEachIndexed { index, textView ->
            if (index == selectedIndex) {
                textView.setBackgroundResource(com.baixingkuaizu.live.android.R.drawable.baixing_tag_selected_bg)
                textView.setTextColor(resources.getColor(android.R.color.white))
            } else {
                textView.setBackgroundResource(com.baixingkuaizu.live.android.R.drawable.baixing_tag_normal_bg)
                textView.setTextColor(resources.getColor(android.R.color.darker_gray))
            }
        }
        
        val tagText = mBaixing_tagButtons[selectedIndex].text.toString()
        mBaixing_adapter.baixing_filterByTag(tagText)
    }
    
    /**
     * 显示退出青少年模式的密码验证对话框
     */
    private fun baixing_showPasswordVerificationForExit() {
        Baixing_ExitDialog(requireContext(), mBaixing_localDataManager) {
            requireActivity().finish()
        }.show()
    }
    
    /**
     * 显示因使用时间限制的密码验证对话框
     */
    private fun baixing_showPasswordVerificationForTimeLimit() {
        // 停止计时器
        mBaixing_handler.removeCallbacks(mBaixing_updateTimeRunnable)

        // 如果已经有对话框在显示，不再创建新的
        if (mBaixing_passwordVerificationDialog != null && mBaixing_passwordVerificationDialog!!.isShowing) {
            return
        }

        mBaixing_passwordVerificationDialog = Baixing_TeenModeExtendTimeDialog(requireContext()) {
            // 密码验证成功的回调
            // 重置计时，记录验证时间
            mBaixing_usedTime = 0
            mBaixing_localDataManager.baixing_setTodayUsedDuration(0)
            baixing_updateUsageTimeText()

            // 重新开始计时
            mBaixing_handler.postDelayed(mBaixing_updateTimeRunnable, mBaixing_updateInterval)
        }.apply {
            show()
        }
    }

    /**
     * 更新使用时间
     */
    private fun baixing_updateUsageTime() {
        // 更新已使用时间（这里简单增加1分钟）
        mBaixing_usedTime += TimeUnit.MINUTES.toMillis(1)
        
        // 保存到本地
        mBaixing_localDataManager.baixing_setTodayUsedDuration(mBaixing_usedTime)
        
        // 更新UI
        baixing_updateUsageTimeText()
        
        // 检查是否超过最大使用时间
        if (mBaixing_usedTime >= mBaixing_maxTime) {
            baixing_showPasswordVerificationForTimeLimit()
        }
    }
    
    /**
     * 更新使用时间文本
     */
    private fun baixing_updateUsageTimeText() {
        val usedMinutes = TimeUnit.MILLISECONDS.toMinutes(mBaixing_usedTime)
        val remainMinutes = TimeUnit.MILLISECONDS.toMinutes(mBaixing_maxTime - mBaixing_usedTime).coerceAtLeast(0)
        
        mBaixing_binding.baixingUsageTime.text = "今日已使用 $usedMinutes 分钟，剩余可用时长 $remainMinutes 分钟"
    }
    
    companion object {
        /**
         * 创建TeenPlayListFragment实例
         * @return TeenPlayListFragment实例
         */
        fun newInstance(): Baixing_TeenPlayListFragment {
            return Baixing_TeenPlayListFragment()
        }
    }
}