package com.baixingkuaizu.live.android.os

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

    fun init() {
        contentLayout.isVisible = false
        emptyLayout.isVisible = false
        errorLayout.isVisible = false
    }

    fun addListener(fragment: Fragment):Baixing_NetViewState {
        init()
        Baixing_ViewVisibilityListener(fragment, contentLayout) { isVisible ->
            baixing_updateLoadingVisibility(contentVisible = isVisible)
        }

        Baixing_ViewVisibilityListener(fragment, emptyLayout) { isVisible ->
            baixing_updateLoadingVisibility(emptyVisible = isVisible)
        }

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
        if (contentVisible) {
            emptyLayout.isVisible = false
            errorLayout.isVisible = false
        } else if (emptyVisible) {
            contentLayout.isVisible = false
            errorLayout.isVisible = false
        } else if (errorVisible) {
            contentLayout.isVisible = false
            emptyLayout.isVisible = false
        }
        listener?.invoke()
    }
}