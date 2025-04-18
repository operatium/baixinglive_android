package com.baixingkuaizu.live.android.busiess.accountfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.databinding.BaixingAccountMenuFragmentBinding
import com.baixingkuaizu.live.android.widget.toast.CenterToast

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 账户菜单页面，展示固定的菜单项，使用可滚动控件适配小尺寸手机
 */
class Baixing_AccountMenuFragment : Baixing_BaseFragment() {

    private var mBaixing_binding: BaixingAccountMenuFragmentBinding? = null
    private lateinit var mBaixing_viewModel: Baixing_AccountMenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBaixing_binding = BaixingAccountMenuFragmentBinding.inflate(inflater, container, false)
        return mBaixing_binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        mBaixing_viewModel = ViewModelProvider(this)[Baixing_AccountMenuViewModel::class.java]
        
        baixing_setupMenuItems()
        
        baixing_observeMenuItems()
    }
    
    private fun baixing_setupMenuItems() {
        mBaixing_binding?.apply {
            baixingMenuItemSetting.setClick {
                baixing_onMenuItemClick("设置")
            }
            
            baixingMenuItemWallet.setClick {
                baixing_onMenuItemClick("钱包")
            }
            
            baixingMenuItemHistory.setClick {
                baixing_onMenuItemClick("观看历史")
            }
            
            baixingMenuItemFavorite.setClick {
                baixing_onMenuItemClick("我的收藏")
            }
            
            baixingMenuItemHelp.setClick {
                baixing_onMenuItemClick("帮助中心")
            }
            
            baixingMenuItemFeedback.setClick {
                baixing_onMenuItemClick("意见反馈")
            }
        }
    }
    
    private fun baixing_observeMenuItems() {
        mBaixing_viewModel.mBaixing_menuItems.observe(viewLifecycleOwner) { menuItems ->
        }
    }
    
    private fun baixing_onMenuItemClick(menuName: String) {
        CenterToast.show(activity, "点击了$menuName")
        when (menuName) {
            "设置" -> mBaixing_viewModel.baixing_onSettingClick()
            "钱包" -> mBaixing_viewModel.baixing_onWalletClick()
            "观看历史" -> mBaixing_viewModel.baixing_onHistoryClick()
            "我的收藏" -> mBaixing_viewModel.baixing_onFavoriteClick()
            "帮助中心" -> mBaixing_viewModel.baixing_onHelpClick()
            "意见反馈" -> mBaixing_viewModel.baixing_onFeedbackClick()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        mBaixing_binding = null
    }
}