package com.baixingkuaizu.live.android.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baixingkuaizu.live.android.base.Baixing_BaseFragment
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_GirlListAdapter
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LiveDataCache
import com.baixingkuaizu.live.android.busiess.livefragment.Baixing_LiveDataEntity
import com.baixingkuaizu.live.android.databinding.BaixingLiveCategoryFragmentBinding
import com.baixingkuaizu.live.android.os.Baixing_NetViewState
import com.baixingkuaizu.live.android.viewmodel.Baixing_LiveCategoryViewModel
import com.baixingkuaizu.live.android.widget.toast.CenterToast

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

    private val mBaixing_OnScroll = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount

            if (lastVisibleItemPosition == totalItemCount - 1) {
                if (mBaixing_isFoot) return
                mBaixing_isFoot = true
                mBaixing_viewModel.requestNextPage(mBaixing_categoryId)
            }
        }
    }
    
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
                hasNextData.observe(viewLifecycleOwner) {
                    mBaixing_binding.run {
                        val listview = baixingLiveRecyclerView
                        if (it) {
                            listview.addOnScrollListener(mBaixing_OnScroll)
                            listview.layoutManager = GridLayoutManager(requireContext(), 2)
                        } else {
                            listview.removeOnScrollListener(mBaixing_OnScroll)
                            baixingLiveFooterEnd.visibility = View.VISIBLE
                            mBaixing_liveAdapter?.let { adapter ->
                                Baixing_LiveDataCache.getListById(mBaixing_categoryId).let{ datas ->
                                    datas.add(Baixing_LiveDataEntity("已经到底了", "", "", 0))
                                    adapter.submitList(datas)
                                }
                            }
                            listview.layoutManager = GridLayoutManager(requireContext(), 2).apply {
                                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                                    override fun getSpanSize(position: Int): Int {
                                        return if (mBaixing_liveAdapter != null
                                            && mBaixing_liveAdapter!!.currentList.isNotEmpty()
                                            && position == mBaixing_liveAdapter!!.currentList.size - 1) {
                                            2
                                        } else {
                                            1
                                        }
                                    }
                                }
                            }
                        }
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
                listrefersh.observe(viewLifecycleOwner) {
                    mBaixing_binding.baixingLiveSwipeRefresh.isRefreshing = it
                }

                listfoot.observe(viewLifecycleOwner) {
                    mBaixing_binding.baixingLiveFooterLoading.visibility =
                        if (it) View.VISIBLE else View.GONE
                    if (!it) {
                        mBaixing_isFoot = false
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

                // 设置下拉刷新监听
                baixing_setupSwipeRefresh()
            }
        }
    }
    
    /**
     * 设置下拉刷新
     */
    private fun baixing_setupSwipeRefresh() {
        mBaixing_binding.baixingLiveSwipeRefresh.setOnRefreshListener {
            mBaixing_viewModel.requestRefersh(mBaixing_categoryId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 释放资源
        mBaixing_NetViewState = null
        Log.d(TAG, "onDestroyView: $mBaixing_categoryId")
    }
}