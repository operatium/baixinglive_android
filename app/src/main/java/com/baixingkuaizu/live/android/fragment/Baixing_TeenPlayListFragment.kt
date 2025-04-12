package com.baixingkuaizu.live.android.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baixingkuaizu.live.android.R
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.busiess.teenmode.Baixing_TeenPlayListAdapter
import java.util.concurrent.TimeUnit

/**
 * @author yuyuexing
 * @date: 2025/4/14
 * @description: 青少年模式播放列表页面
 */
class Baixing_TeenPlayListFragment : Baixing_BaseFragment() {
    
    private lateinit var mBaixing_recyclerView: RecyclerView
    private lateinit var mBaixing_adapter: Baixing_TeenPlayListAdapter
    private lateinit var mBaixing_backButton: View
    private lateinit var mBaixing_exitButton: TextView
    private lateinit var mBaixing_usageTimeText: TextView
    private lateinit var mBaixing_localDataManager: Baixing_LocalDataManager
    
    // 标签按钮列表
    private lateinit var mBaixing_tagButtons: List<TextView>
    
    // 使用时间相关
    private var mBaixing_usedTime: Long = 0
    private var mBaixing_maxTime: Long = TimeUnit.MINUTES.toMillis(40) // 40分钟
    private val mBaixing_updateInterval: Long = 60000 // 1分钟更新一次
    
    // 密码验证对话框
    private var mBaixing_passwordVerificationDialog: AlertDialog? = null
    
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
    ): View? {
        return inflater.inflate(R.layout.baixing_play_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        mBaixing_localDataManager = Baixing_LocalDataManager.baixing_getInstance(requireContext())
        
        // 初始化视图
        baixing_initViews(view)
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
            baixing_showPasswordVerificationForTimeLimit(false)
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
    
    private fun baixing_initViews(view: View) {
        mBaixing_recyclerView = view.findViewById(R.id.baixing_recycler_view)
        mBaixing_backButton = view.findViewById(R.id.baixing_back)
        mBaixing_exitButton = view.findViewById(R.id.baixing_exit_teen_mode)
        mBaixing_usageTimeText = view.findViewById(R.id.baixing_usage_time)
        
        // 标签按钮
        mBaixing_tagButtons = listOf(
            view.findViewById(R.id.baixing_tag_all),
            view.findViewById(R.id.baixing_tag_education),
            view.findViewById(R.id.baixing_tag_science),
            view.findViewById(R.id.baixing_tag_nature),
            view.findViewById(R.id.baixing_tag_history),
            view.findViewById(R.id.baixing_tag_cartoon)
        )
    }
    
    private fun baixing_setupListeners() {
        // 返回按钮点击事件
        mBaixing_backButton.setClick {
            requireActivity().finish()
        }
        
        // 退出青少年模式按钮点击事件
        mBaixing_exitButton.setClick {
            baixing_showPasswordVerificationForExit()
        }
        
        // 标签按钮点击事件
        mBaixing_tagButtons.forEachIndexed { index, textView ->
            textView.setClick {
                baixing_selectTag(index)
            }
        }
    }
    
    private fun baixing_initData() {
        // 初始化RecyclerView和适配器
        mBaixing_adapter = Baixing_TeenPlayListAdapter(emptyList()) { video ->
            // 点击视频项的处理
            Toast.makeText(requireContext(), "播放: ${video.mBaixing_title}", Toast.LENGTH_SHORT).show()
        }
        
        mBaixing_recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        mBaixing_recyclerView.adapter = mBaixing_adapter
        
        // 加载初始数据
        baixing_loadInitialData()
    }
    
    private fun baixing_loadInitialData() {
        // 使用适配器的静态列表数据
        mBaixing_adapter.baixing_filterByTag("全部")
    }
    
    /**
     * 选择标签
     */
    private fun baixing_selectTag(selectedIndex: Int) {
        // 更新所有标签的背景和文字颜色
        mBaixing_tagButtons.forEachIndexed { index, textView ->
            if (index == selectedIndex) {
                textView.setBackgroundResource(R.drawable.baixing_tag_selected_bg)
                textView.setTextColor(resources.getColor(android.R.color.white))
            } else {
                textView.setBackgroundResource(R.drawable.baixing_tag_normal_bg)
                textView.setTextColor(resources.getColor(android.R.color.darker_gray))
            }
        }
        
        // 根据选中的标签筛选数据
        val tagText = mBaixing_tagButtons[selectedIndex].text.toString()
        mBaixing_adapter.baixing_filterByTag(tagText)
    }
    
    /**
     * 显示退出青少年模式的密码验证对话框
     */
    private fun baixing_showPasswordVerificationForExit() {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.baixing_password_verification_dialog, null)
        val passwordInput = dialogView.findViewById<EditText>(R.id.baixing_password_input)
        
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("确定", null) // 后面手动设置监听器以防止对话框自动关闭
            .setNegativeButton("取消", null)
            .create()
        
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val password = passwordInput.text.toString()
                
                if (password.isEmpty()) {
                    Toast.makeText(requireContext(), "密码不能为空", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                
                if (baixing_verifyPassword(password)) {
                    dialog.dismiss()
                    baixing_exitTeenMode()
                } else {
                    Toast.makeText(requireContext(), "密码错误", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        dialog.show()
    }
    
    /**
     * 显示因使用时间限制的密码验证对话框
     * @param allowDismiss 是否允许用户关闭对话框
     */
    private fun baixing_showPasswordVerificationForTimeLimit(allowDismiss: Boolean) {
        // 停止计时器
        mBaixing_handler.removeCallbacks(mBaixing_updateTimeRunnable)
        
        // 如果已经有对话框在显示，不再创建新的
        if (mBaixing_passwordVerificationDialog != null && mBaixing_passwordVerificationDialog!!.isShowing) {
            return
        }
        
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.baixing_password_verification_dialog, null)
        val passwordInput = dialogView.findViewById<EditText>(R.id.baixing_password_input)
        
        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("使用时间已达上限")
            .setMessage("您今日的使用时间已达到上限（40分钟），请输入监护密码继续使用。")
            .setPositiveButton("确定", null) // 后面手动设置监听器以防止对话框自动关闭
        
        // 如果不允许关闭，不提供取消按钮
        if (allowDismiss) {
            builder.setNegativeButton("取消", null)
        }
        
        val dialog = builder.create()
        
        // 设置是否可以通过返回键或点击外部关闭
        dialog.setCancelable(allowDismiss)
        
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val password = passwordInput.text.toString()
                
                if (password.isEmpty()) {
                    Toast.makeText(requireContext(), "密码不能为空", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                
                if (baixing_verifyPassword(password)) {
                    // 密码验证成功，重置计时，记录验证时间
                    mBaixing_usedTime = 0
                    mBaixing_localDataManager.baixing_setTodayUsedDuration(0)
                    mBaixing_localDataManager.baixing_setLastVerifiedTime(System.currentTimeMillis())
                    baixing_updateUsageTimeText()
                    
                    // 关闭对话框，重新开始计时
                    dialog.dismiss()
                    mBaixing_handler.postDelayed(mBaixing_updateTimeRunnable, mBaixing_updateInterval)
                    
                    Toast.makeText(requireContext(), "验证成功，已重置使用时间", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "密码错误", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        mBaixing_passwordVerificationDialog = dialog
        dialog.show()
    }
    
    /**
     * 验证密码
     */
    private fun baixing_verifyPassword(inputPassword: String): Boolean {
        val savedPassword = mBaixing_localDataManager.baixing_getParentPassword()
        return inputPassword == savedPassword
    }
    
    /**
     * 退出青少年模式
     */
    private fun baixing_exitTeenMode() {
        // 关闭青少年模式，清空密码
        mBaixing_localDataManager.baixing_setTeenModeEnabled(false)
        mBaixing_localDataManager.baixing_setParentPassword("")
        
        Toast.makeText(requireContext(), "已退出青少年模式", Toast.LENGTH_SHORT).show()
        
        // 关闭当前页面
        requireActivity().finish()
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
            baixing_showPasswordVerificationForTimeLimit(false)
        }
    }
    
    /**
     * 更新使用时间文本
     */
    private fun baixing_updateUsageTimeText() {
        val usedMinutes = TimeUnit.MILLISECONDS.toMinutes(mBaixing_usedTime)
        val remainMinutes = TimeUnit.MILLISECONDS.toMinutes(mBaixing_maxTime - mBaixing_usedTime).coerceAtLeast(0)
        
        mBaixing_usageTimeText.text = "今日已使用 $usedMinutes 分钟，剩余可用时长 $remainMinutes 分钟"
    }
    
    companion object {
        fun newInstance(): Baixing_TeenPlayListFragment {
            return Baixing_TeenPlayListFragment()
        }
    }
} 