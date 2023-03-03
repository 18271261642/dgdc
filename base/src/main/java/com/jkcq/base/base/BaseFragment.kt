package com.jkcq.base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jkcq.util.StatusBarUtil
import kotlinx.coroutines.cancel

/**
 *  Created by BeyondWorlds
 *  on 2020/7/23
 */
abstract class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setStatusBar()
        initView()
        initData()
    }

    abstract fun getLayoutResId(): Int

    abstract fun initView()

    abstract fun initData()

    override fun onDestroy() {
        super.onDestroy()
    }

    open fun setStatusBar() {
        //无标题栏的沉浸式
        StatusBarUtil.setTransparentForImageView(activity, null)
        StatusBarUtil.setLightMode(activity)
    }
}