package com.baixingkuaizu.live.android.os

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.core.view.isVisible

class Baixing_ViewVisibilityListener(
    private val fragment: Fragment,
    private val targetView: View,
    private val onVisibilityChanged: (Boolean) -> Unit
) : DefaultLifecycleObserver {

    private val viewTreeObserverListener = View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        val isVisible = targetView.isVisible
        onVisibilityChanged(isVisible)
    }

    init {
        fragment.lifecycle.addObserver(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        targetView.addOnLayoutChangeListener(viewTreeObserverListener)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        targetView.removeOnLayoutChangeListener(viewTreeObserverListener)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        fragment.lifecycle.removeObserver(this)
    }
}