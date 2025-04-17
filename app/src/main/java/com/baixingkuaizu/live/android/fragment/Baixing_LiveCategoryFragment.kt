package com.baixingkuaizu.live.android.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_GirlListAdapter
import com.baixingkuaizu.live.android.databinding.BaixingLiveCategoryFragmentBinding
import com.baixingkuaizu.live.android.os.Baixing_NetViewState
import com.baixingkuaizu.live.android.viewmodel.Baixing_LiveCategoryViewModel
import com.baixingkuaizu.live.android.widget.toast.CenterToast
import com.scwang.smart.refresh.footer.*
import com.scwang.smart.refresh.header.*

/**
 * @author yuyuexing
 * @date: 2025/4/18
 * @description: 直播栏目子页面Fragment，支持上拉加载和下拉刷新
 */
class Baixing_LiveCategoryFragment : Baixing_BaseFragment() {
    
    private lateinit var mBaixing_binding: BaixingLiveCategoryFragmentBinding
    private var mBaixing_liveAdapter: Baixing_GirlListAdapter? = null
    private var mBaixing_categoryId: String = ""
    private var mBaixing_isFoot = false
    private lateinit var mBaixing_viewModel: Baixing_LiveCategoryViewModel
    private var mBaixing_NetViewState: Baixing_NetViewState? = null
    
    companion object {
        val TAG = "yyx类Baixing_LiveCategoryFragment"
        private const val ARG_CATEGORY_ID = "category_id"

        /**
         * 创建Fragment实例并传入栏目ID参数
         * @param categoryId 栏目ID
         * @return 新的Fragment实例
         */
        fun baixing_newInstance(categoryId: String): Baixing_LiveCategoryFragment {
            return Baixing_LiveCategoryFragment().apply {
                arguments = bundleOf(ARG_CATEGORY_ID to categoryId)
            }
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBaixing_categoryId = arguments?.getString(ARG_CATEGORY_ID) ?: ""
        mBaixing_binding = BaixingLiveCategoryFragmentBinding.inflate(inflater, container, false)
        return mBaixing_binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baixing_initViewModel()
        baixing_setupViewState()
        baixing_loadData()
    }
    
    /**
     * 初始化ViewModel并设置LiveData观察
     */
    private fun baixing_initViewModel() {
        mBaixing_viewModel =
            ViewModelProvider(this)[Baixing_LiveCategoryViewModel::class.java].apply {

                // 观察直播列表数据
                liveList.observe(viewLifecycleOwner) {
                    Log.d(TAG, "LiveContentLayout: ${it.count()}")
                    mBaixing_binding.baixingLiveSwipeRefresh.visibility = View.VISIBLE
                    baixing_initListView()
                    mBaixing_liveAdapter?.submitList(it)
                }

                // 观察列表底部状态
                hasNextData.observe(viewLifecycleOwner) { hasMore ->
                    mBaixing_binding.baixingLiveSwipeRefresh.setEnableLoadMore(hasMore)
                    if (!hasMore) {
                        // 没有更多数据时，显示底部提示
                        mBaixing_binding.baixingLiveSwipeRefresh.finishLoadMoreWithNoMoreData()
                    }
                }

                retry.observe(viewLifecycleOwner) {
                    Log.d(TAG, "retry: ${it}")
                    mBaixing_binding.baixingLiveErrorLayout.visibility = if (it) View.VISIBLE else View.GONE
                }

                // 观察网络错误
                netError.observe(viewLifecycleOwner) {
                    if (it.isNullOrBlank()) return@observe
                    CenterToast.show(activity, it)
                }

                // 观察加载状态
                listloading.observe(viewLifecycleOwner) {
                    mBaixing_binding.loading.visibility = if (it) View.VISIBLE else View.GONE
                }

                // 观察刷新状态
                listrefersh.observe(viewLifecycleOwner) { isRefreshing ->
                    if (!isRefreshing) {
                        mBaixing_binding.baixingLiveSwipeRefresh.finishRefresh()
                    }
                }

                listfoot.observe(viewLifecycleOwner) { isLoadingMore ->
                    if (!isLoadingMore) {
                        mBaixing_binding.baixingLiveSwipeRefresh.finishLoadMore()
                    }
                }

                // 观察空数据状态
                listEmpty.observe(viewLifecycleOwner) {
                    mBaixing_binding.baixingLiveEmptyLayout.visibility =
                        if (it) View.VISIBLE else View.GONE
                }
            }
    }
    
    /**
     * 设置视图状态管理
     */
    private fun baixing_setupViewState() {
        mBaixing_binding.apply {
            // 使用NetViewState统一管理视图状态
            mBaixing_NetViewState = Baixing_NetViewState(
                baixingLiveSwipeRefresh,
                baixingLiveEmptyLayout,
                baixingLiveErrorLayout,
            ).addListener(this@Baixing_LiveCategoryFragment)
            Log.d(TAG, "baixing_setupViewState: ${mBaixing_NetViewState}")
            // 设置错误重试按钮点击事件
            baixingLiveErrorRetryBtn.setOnClickListener {
                CenterToast.show(activity, "正在重新加载...")
                mBaixing_viewModel.requestFirstPage(mBaixing_categoryId)
            }
        }
    }
    
    /**
     * 加载数据
     */
    private fun baixing_loadData() {
        mBaixing_viewModel.requestFirstPage(mBaixing_categoryId)
    }
    
    /**
     * 初始化列表视图
     */
    private fun baixing_initListView() {
        if (mBaixing_liveAdapter == null) {
            mBaixing_liveAdapter = Baixing_GirlListAdapter()
            mBaixing_binding.apply {
                // 设置网格布局管理器
                baixingLiveRecyclerView.layoutManager = GridLayoutManager(context, 2)
                baixingLiveRecyclerView.adapter = mBaixing_liveAdapter

                // 设置刷新和加载监听
                baixing_setupRefreshLayout()
            }
        }
    }
    
    /**
     * 设置刷新布局，包括下拉刷新和上拉加载更多
     */
    private fun baixing_setupRefreshLayout() {
        mBaixing_binding.baixingLiveSwipeRefresh.apply {
            // 设置下拉刷新监听
            setOnRefreshListener { refreshLayout ->
                mBaixing_viewModel.requestRefersh(mBaixing_categoryId)
                // 不在这里调用finishRefresh，由ViewModel的listrefersh观察者处理
            }
            
            // 设置上拉加载更多监听
            setOnLoadMoreListener { refreshLayout ->
                mBaixing_viewModel.requestNextPage(mBaixing_categoryId)
                // 不在这里调用finishLoadMore，由ViewModel的listfoot观察者处理
            }
            setRefreshHeader(MaterialHeader(context))
            setRefreshFooter(ClassicsFooter(context))
            setEnableOverScrollDrag(true)
            setEnableLoadMore(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 释放资源
        mBaixing_NetViewState = null
        Log.d(TAG, "onDestroyView: $mBaixing_categoryId")
    }
}