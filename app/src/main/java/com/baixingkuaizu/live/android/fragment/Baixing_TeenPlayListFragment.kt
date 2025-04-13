package com.baixingkuaizu.live.android.fragment

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.baixingkuaizu.live.android.adatperandroid.Baixing_AdapterHelper.setClick
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.localdata.Baixing_LocalDataManager
import com.baixingkuaizu.live.android.busiess.router.Baixing_GoRouter
import com.baixingkuaizu.live.android.busiess.teenmode.Baixing_TeenPlayListAdapter
import com.baixingkuaizu.live.android.databinding.BaixingPlayListFragmentBinding
import com.baixingkuaizu.live.android.dialog.Baixing_ExitDialog
import com.baixingkuaizu.live.android.dialog.Baixing_TeenModeExtendTimeDialog
import com.baixingkuaizu.live.android.widget.toast.CenterToast
import java.util.concurrent.TimeUnit

/**
 * @author yuyuexing
 * @date: 2025/4/14
 * @description: 青少年模式播放列表页面，负责展示适合青少年的内容并管理使用时间限制。实现了标签筛选、内容展示和使用时间监控功能，当使用时间达到限制（40分钟）时，会显示验证对话框要求输入监护密码。使用ViewBinding进行视图绑定，Handler处理定时任务，与多个对话框类和LocalDataManager协同工作。
 */
class Baixing_TeenPlayListFragment : Baixing_BaseFragment() {
    
    private var _mBaixing_binding: BaixingPlayListFragmentBinding? = null
    private val mBaixing_binding get() = _mBaixing_binding!!
    
    private lateinit var mBaixing_adapter: Baixing_TeenPlayListAdapter
    private lateinit var mBaixing_localDataManager: Baixing_LocalDataManager
    
    private lateinit var mBaixing_tagButtons: List<TextView>
    
    private var mBaixing_usedTime: Long = 0
    private var mBaixing_maxTime: Long = TimeUnit.MINUTES.toMillis(40)
    private val mBaixing_updateInterval: Long = 60000
    
    private var mBaixing_passwordVerificationDialog: Dialog? = null
    
    private val mBaixing_handler = Handler(Looper.getMainLooper())
    
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
        
        baixing_initViews()
        baixing_setupListeners()
        baixing_initData()
    }
    
    override fun onResume() {
        super.onResume()
        mBaixing_usedTime = mBaixing_localDataManager.baixing_getTodayUsedDuration()
        baixing_updateUsageTimeText()
        
        val lastVerifiedTime = mBaixing_localDataManager.baixing_getLastVerifiedTime()
        val currentTime = System.currentTimeMillis()
        
        if (mBaixing_usedTime >= mBaixing_maxTime && 
            (currentTime - lastVerifiedTime) >= mBaixing_maxTime) {
            baixing_showPasswordVerificationForTimeLimit()
        } else {
            mBaixing_handler.postDelayed(mBaixing_updateTimeRunnable, mBaixing_updateInterval)
        }
    }
    
    override fun onPause() {
        super.onPause()
        mBaixing_handler.removeCallbacks(mBaixing_updateTimeRunnable)
        mBaixing_localDataManager.baixing_setTodayUsedDuration(mBaixing_usedTime)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _mBaixing_binding = null
    }
    
    private fun baixing_initViews() {
        mBaixing_tagButtons = listOf(
            mBaixing_binding.baixingTagAll,
            mBaixing_binding.baixingTagEducation,
            mBaixing_binding.baixingTagScience,
            mBaixing_binding.baixingTagNature,
            mBaixing_binding.baixingTagHistory,
            mBaixing_binding.baixingTagCartoon
        )
    }
    
    private fun baixing_setupListeners() {
        mBaixing_binding.baixingBack.setClick {
            baixing_showPasswordVerificationForExit()
        }
        
        mBaixing_binding.baixingExitTeenMode.setClick {
            baixing_showPasswordVerificationForExit()
        }
        
        mBaixing_tagButtons.forEachIndexed { index, textView ->
            textView.setClick {
                baixing_selectTag(index)
            }
        }
    }
    
    private fun baixing_initData() {
        mBaixing_adapter = Baixing_TeenPlayListAdapter { video ->
            Baixing_GoRouter.baixing_jumpVideoPlayerActivity(video)
        }
        
        mBaixing_binding.baixingRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        mBaixing_binding.baixingRecyclerView.adapter = mBaixing_adapter
        
        baixing_loadInitialData()
    }
    
    private fun baixing_loadInitialData() {
        mBaixing_adapter.baixing_filterByTag("全部")
    }
    
    private fun baixing_selectTag(selectedIndex: Int) {
        mBaixing_tagButtons.forEachIndexed { index, textView ->
            if (index == selectedIndex) {
                textView.setBackgroundResource(com.baixingkuaizu.live.android.R.drawable.baixing_tag_selected_bg)
                textView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            } else {
                textView.setBackgroundResource(com.baixingkuaizu.live.android.R.drawable.baixing_tag_normal_bg)
                textView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
            }
        }
        
        val tagText = mBaixing_tagButtons[selectedIndex].text.toString()
        mBaixing_adapter.baixing_filterByTag(tagText)
    }
    
    private fun baixing_showPasswordVerificationForExit() {
        Baixing_ExitDialog(requireContext(), mBaixing_localDataManager) {
            requireActivity().finish()
        }.show()
    }
    
    private fun baixing_showPasswordVerificationForTimeLimit() {
        mBaixing_handler.removeCallbacks(mBaixing_updateTimeRunnable)

        if (mBaixing_passwordVerificationDialog != null && mBaixing_passwordVerificationDialog!!.isShowing) {
            return
        }

        mBaixing_passwordVerificationDialog = Baixing_TeenModeExtendTimeDialog(requireContext()) {
            mBaixing_usedTime = 0
            mBaixing_localDataManager.baixing_setTodayUsedDuration(0)
            baixing_updateUsageTimeText()

            mBaixing_handler.postDelayed(mBaixing_updateTimeRunnable, mBaixing_updateInterval)
        }.apply {
            show()
        }
    }

    private fun baixing_updateUsageTime() {
        mBaixing_usedTime += TimeUnit.MINUTES.toMillis(1)
        
        mBaixing_localDataManager.baixing_setTodayUsedDuration(mBaixing_usedTime)
        
        baixing_updateUsageTimeText()
        
        if (mBaixing_usedTime >= mBaixing_maxTime) {
            baixing_showPasswordVerificationForTimeLimit()
        }
    }
    
    private fun baixing_updateUsageTimeText() {
        val usedMinutes = TimeUnit.MILLISECONDS.toMinutes(mBaixing_usedTime)
        val remainMinutes = TimeUnit.MILLISECONDS.toMinutes(mBaixing_maxTime - mBaixing_usedTime).coerceAtLeast(0)
        
        mBaixing_binding.baixingUsageTime.text = "今日已使用 $usedMinutes 分钟，剩余可用时长 $remainMinutes 分钟"
    }
    
    companion object {
        fun newInstance(): Baixing_TeenPlayListFragment {
            return Baixing_TeenPlayListFragment()
        }
    }
}