package com.jkcq.base.base

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.jkcq.base.app.BaseApp

/**
 *  Created by BeyondWorlds
 *  on 2020/7/23
 */
abstract class BaseVMActivity<VM : BaseViewModel> : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startObserver()
    }

    abstract fun startObserver()

    /**
     * 创建viewModel
     */
    protected fun <T : BaseViewModel> createViewModel(vmClass: Class<T>): T {
        return ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(BaseApp.sApplicaton)
        ).get(vmClass)
    }
}