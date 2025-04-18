package com.baixingkuaizu.live.android.fragment

import android.os.Bundle
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
    private lateinit var mBaixing_viewModel: Baixing_LiveCategoryViewModel
    private lateinit var mBaixing_NetViewState: Baixing_NetViewState
    
    companion object {
        val TAG = "yyx类Baixing_LiveCategoryFragment"
        private const val ARG_CATEGORY_ID = "category_id"

        fun baixing_newInstance(categoryId: String): Baixing_LiveCategoryFragment {
            return Baixing_LiveCategoryFragment().apply {
                arguments = bundleOf(ARG_CATEGORY_ID to categoryId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        baixing_initLoadData()
    }
    
    private fun baixing_initViewModel() {
        mBaixing_viewModel =
            ViewModelProvider(this)[Baixing_LiveCategoryViewModel::class.java].apply {
                liveList.observe(viewLifecycleOwner) {
                    mBaixing_binding.baixingLiveSwipeRefresh.visibility = View.VISIBLE
                    baixing_initListView()
                    mBaixing_liveAdapter?.submitList(it)
                }
                hasNextData.observe(viewLifecycleOwner) { hasMore ->
                    mBaixing_binding.baixingLiveSwipeRefresh.setEnableLoadMore(hasMore)
                    if (!hasMore) {
                        mBaixing_binding.baixingLiveSwipeRefresh.finishLoadMoreWithNoMoreData()
                    }
                }
                retry.observe(viewLifecycleOwner) {
                    mBaixing_binding.baixingLiveErrorLayout.visibility = if (it) View.VISIBLE else View.GONE
                }
                netError.observe(viewLifecycleOwner) {
                    if (it.isNullOrBlank()) return@observe
                    CenterToast.show(activity, it)
                }
                listloading.observe(viewLifecycleOwner) {
                    mBaixing_binding.loading.visibility = if (it) View.VISIBLE else View.GONE
                }
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
                listEmpty.observe(viewLifecycleOwner) {
                    mBaixing_binding.baixingLiveEmptyLayout.visibility =
                        if (it) View.VISIBLE else View.GONE
                }
            }
    }
    
    private fun baixing_setupViewState() {
        mBaixing_binding.apply {
            mBaixing_NetViewState = Baixing_NetViewState(
                baixingLiveSwipeRefresh,
                baixingLiveEmptyLayout,
                baixingLiveErrorLayout,
            ).addListener(this@Baixing_LiveCategoryFragment)
            baixingLiveErrorRetryBtn.setOnClickListener {
                CenterToast.show(activity, "正在重新加载...")
                mBaixing_viewModel.requestFirstPage(mBaixing_categoryId)
            }
        }
    }
    
    private fun baixing_initLoadData() {
        if (mBaixing_liveAdapter != null && mBaixing_liveAdapter!!.itemCount > 0) {
            return
        }
        mBaixing_viewModel.requestFirstPage(mBaixing_categoryId)
    }
    
    private fun baixing_initListView() {
        if (mBaixing_liveAdapter == null) {
            mBaixing_liveAdapter = Baixing_GirlListAdapter()
            mBaixing_binding.apply {
                baixingLiveRecyclerView.layoutManager = GridLayoutManager(context, 2)
                baixingLiveRecyclerView.adapter = mBaixing_liveAdapter
                baixing_setupRefreshLayout()
            }
        }
    }
    
    private fun baixing_setupRefreshLayout() {
        mBaixing_binding.baixingLiveSwipeRefresh.apply {
            setOnRefreshListener { refreshLayout ->
                mBaixing_viewModel.requestRefersh(mBaixing_categoryId)
            }
            setOnLoadMoreListener { refreshLayout ->
                mBaixing_viewModel.requestNextPage(mBaixing_categoryId)
            }
            setRefreshHeader(MaterialHeader(context))
            setRefreshFooter(ClassicsFooter(context).setFinishDuration(0))
            setEnableLoadMoreWhenContentNotFull(false)
            setEnableAutoLoadMore(false)
            setEnableOverScrollDrag(true)
            setEnableLoadMore(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}