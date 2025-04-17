package com.baixingkuaizu.live.android.os

import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

class Baixing_NetViewState(
    val contentLayout: View,
    val emptyLayout: View,
    val errorLayout: View,
    val listener: (()-> Unit)? = null
) {
    private val TAG= "yyx类NetViewState${this}"
    /**
     * 统一管理视图状态和加载指示器的显示
     */
    fun addListener(fragment: Fragment):Baixing_NetViewState {
        // 内容视图可见性变化监听
        Baixing_ViewVisibilityListener(fragment, contentLayout) { isVisible ->
            baixing_updateLoadingVisibility(contentVisible = isVisible)
        }

        // 空视图可见性变化监听
        Baixing_ViewVisibilityListener(fragment, emptyLayout) { isVisible ->
            baixing_updateLoadingVisibility(emptyVisible = isVisible)
        }

        // 错误视图可见性变化监听
        Baixing_ViewVisibilityListener(fragment, errorLayout) { isVisible ->
            baixing_updateLoadingVisibility(errorVisible = isVisible)
        }
        return this
    }

    /**
     * 根据各视图的可见状态更新加载指示器的显示
     */
    fun baixing_updateLoadingVisibility(
        contentVisible: Boolean = contentLayout.isVisible,
        emptyVisible: Boolean = emptyLayout.isVisible,
        errorVisible: Boolean = errorLayout.isVisible
    ) {
        // 当任何一个内容视图显示时，隐藏其他视图
        if (contentVisible) {
            Log.d(TAG, "baixing_updateLoadingVisibility: contentVisible")
            emptyLayout.visibility = View.GONE
            errorLayout.visibility = View.GONE
        } else if (emptyVisible) {
            Log.d(TAG, "baixing_updateLoadingVisibility: emptyVisible")
            contentLayout.visibility = View.GONE
            errorLayout.visibility = View.GONE
        } else if (errorVisible) {
            Log.d(TAG, "baixing_updateLoadingVisibility: errorVisible")
            contentLayout.visibility = View.GONE
            emptyLayout.visibility = View.GONE
        }
        listener?.invoke()
    }
}