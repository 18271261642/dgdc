package com.jkcq.base.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.jkcq.base.app.BaseApp
import com.jkcq.base.app.Preference

/**
 *  Created by BeyondWorlds
 *  on 2020/6/22
 */
abstract class BaseVMFragment<VM : BaseViewModel> : BaseFragment() {

    var mUserId: String by Preference(Preference.USER_ID, "")
    var mUserName: String by Preference(Preference.USER_NAME, "")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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