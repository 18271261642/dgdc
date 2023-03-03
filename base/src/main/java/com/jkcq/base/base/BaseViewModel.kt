package com.jkcq.base.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beyondworlds.wanandroid.net.responseHandle.AppException
import com.beyondworlds.wanandroid.net.responseHandle.ExceptionHandle
import com.jkcq.base.R
import com.jkcq.base.app.BaseApp
import com.jkcq.base.app.Preference
import com.jkcq.base.net.bean.BaseResponse
import com.jkcq.base.observable.LoginOutObservable
import com.jkcq.base.observable.NetDialogObservable
import com.jkcq.util.AppUtil
import com.jkcq.util.NetUtil
import com.jkcq.util.ktx.ToastUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *  Created by BeyondWorlds
 *  on 2020/6/17
 */
abstract class BaseViewModel : ViewModel() {

    var mUserId: String by Preference(Preference.USER_ID, "")
    var mMobile: String by Preference(Preference.MOBILE, "")
    var mUserName: String by Preference(Preference.USER_NAME, "")

    var mNetworkLiveData = MutableLiveData<Boolean>().apply { value = true }

    /**
     * 处理请求结果，过滤错误码
     */
    fun <T> executeRequest(
        block: suspend () -> BaseResponse<T>,
        onSuccess: (T) -> Unit,
        error: (AppException) -> Unit = {
            ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
        }
    ) {
        if (!NetUtil.isNetworkConnected(BaseApp.sApplicaton)) {
            ToastUtil.showTextToast(
                BaseApp.sApplicaton,
                BaseApp.sApplicaton.getString(R.string.net_error)
            )
            //如果需要处理，添加livedata观察
            mNetworkLiveData.value = mNetworkLiveData.value?.let { !it }
            return
        }

        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) { block() }
            }.onSuccess {
                kotlin.runCatching {
                    withContext(Dispatchers.Main) {
                        executeResponse(it) {
                            NetDialogObservable.getInstance().hide()
                            onSuccess(it)
                        }
                    }
                }.onFailure {
                    NetDialogObservable.getInstance().hide()
                    error(ExceptionHandle.handleException(it))
                }

            }.onFailure {
                NetDialogObservable.getInstance().hide()
                error(ExceptionHandle.handleException(it))
            }
        }
    }

    /**
     * 处理请求结果，不过滤
     */
    fun <T> executeRequestNoCheck(
        block: suspend () -> T,
        onSuccess: (T) -> Unit,
        error: (AppException) -> Unit = {}
    ) {
        if (!NetUtil.isNetworkConnected(BaseApp.sApplicaton)) {
            ToastUtil.showTextToast(BaseApp.sApplicaton, R.string.net_error)
            //如果需要处理，添加livedata观察
            mNetworkLiveData.value = mNetworkLiveData.value?.let { !it }
            return
        }
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) { block() }
            }.onSuccess { data ->
                withContext(Dispatchers.Main) {
                    onSuccess(data)
                }
            }.onFailure {
                error(ExceptionHandle.handleException(it))
            }
        }
    }

    /**
     *结果过滤
     */
    private fun <T> executeResponse(response: BaseResponse<T>, success: (T) -> Unit) {
        if (response.code == 2000) {
            if (response.obj != null) {
                success(response.obj)
            } else {
                if (AppUtil.isCN()) {
                    throw AppException(response.code, response.message)
                } else {
                    throw AppException(response.code, response.message_en)
                }
            }
        } else if (response.code == 1003) {
            LoginOutObservable.getInstance().show()
            if (AppUtil.isCN()) {
                throw AppException(response.code, response.message)
            } else {
                throw AppException(response.code, response.message_en)
            }
        } else {
            if (AppUtil.isCN()) {
                throw AppException(response.code, response.message)
            } else {
                throw AppException(response.code, response.message_en)
            }
        }
    }
}