package com.jkcq.homebike.login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.jkcq.base.app.BaseApp
import com.jkcq.base.base.BaseViewModel
import com.jkcq.base.net.bean.UserInfo
import com.jkcq.base.observable.NetDialogObservable
import com.jkcq.homebike.Constant
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.platform.umeng.PlatformLoginManager
import com.jkcq.util.AppUtil
import com.jkcq.util.RegexUtil
import com.jkcq.util.ktx.ToastUtil

/**
 *  Created by BeyondWorlds
 *  on 2020/7/29
 */
class LoginViewModel : BaseViewModel() {

    var mUserInfoLiveData = MutableLiveData<UserInfo>()
    val mLoginRepository: LoginRepository by lazy { LoginRepository() }


    fun loginByMobile(account: String, pwd: String) {
        if (account.isBlank()) {
            ToastUtil.showTextToast(BaseApp.sApplicaton, R.string.phone_cannot_blank)
        } else if (pwd.isBlank()) {
            ToastUtil.showTextToast(BaseApp.sApplicaton, R.string.code_cannot_blank)
        } else {
            if (!RegexUtil.isPhone(account)) {
                ToastUtil.showTextToast(BaseApp.sApplicaton, R.string.enter_correct_tel_notice)
            } else {
                login(account, pwd, Constant.LOGIN_BY_PHONE)
            }
        }
    }

    fun loginByEmail(account: String, pwd: String) {
        if (account.isBlank()) {
            ToastUtil.showTextToast(BaseApp.sApplicaton, R.string.email_cannot_blank)
        } else if (pwd.isBlank()) {
            ToastUtil.showTextToast(BaseApp.sApplicaton, R.string.code_cannot_blank)
        } else {
            if (!RegexUtil.isEmail(account)) {
                ToastUtil.showTextToast(
                    BaseApp.sApplicaton,
                    BaseApp.sApplicaton.getString(R.string.enter_correct_email_notice)
                )
            } else {
                login(account, pwd, Constant.LOGIN_BY_EMAIL)
            }
        }

    }

    fun login(account: String, pwd: String, type: Int) {
        val map = HashMap<String, String>()
        map["mobile"] = account
        map["verify"] = pwd
        map["email"] = account
        map["interfaceId"] = "0"
        map["type"] = "$type"
        map["userId"] = ""

        executeRequest({ mLoginRepository.loginByMobile(map) }, {
            mUserInfoLiveData.value = it
            mUserName = it.nickName
            BikeConfig.userCurrentUtil = it.measurement
            mUserId = it.userId
        }, {
            Log.e("login", it.errorMsg)
            ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
        })
    }

    fun getVerifyCodeByMobile(phone: String) {
        if (phone.isBlank() || !RegexUtil.isPhone(phone)) {
            ToastUtil.showTextToast(
                BaseApp.sApplicaton,
                BaseApp.sApplicaton.getString(R.string.enter_correct_tel_notice)
            )
        } else {
            NetDialogObservable.getInstance().show()
            executeRequest({
                mLoginRepository.getVerifyCodeByMobile(
                    phone,
                    Constant.LOGIN_BY_PHONE
                )
            }, {})
        }
    }

    fun getVerifyCodeByEmail(email: String) {

        var language = "ch"
        if (!AppUtil.isCN()) {
            language = "en"
        }
//                || !RegexUtil.isEmail(email)
        if (email.isBlank()) {
            ToastUtil.showTextToast(
                BaseApp.sApplicaton,
                BaseApp.sApplicaton.getString(R.string.enter_correct_email_notice)
            )
        } else {
            NetDialogObservable.getInstance().show()
            executeRequest({
                mLoginRepository.getVerifyCodeByEmail(
                    email,
                    Constant.LOGIN_BY_EMAIL,
                    language
                )
            }, {})
        }
    }

    fun loginByWechat(activity: Activity) {
        PlatformLoginManager.loginByWeChat(activity, mThirdLoginListener)
    }

    fun LoginByQQ(activity: Activity) {
        PlatformLoginManager.loginByQQ(activity, mThirdLoginListener)
    }

    fun loginByFaceBook(activity: Activity) {
        PlatformLoginManager.loginByFaceBook(activity, mThirdLoginListener)

    }

    fun loginByGoogle(activity: Activity) {
        PlatformLoginManager.loginByGoogle(activity, mThirdLoginListener)

    }

    var mThirdLoginListener = object : PlatformLoginManager.LoginListener {
        override fun onSuccess(loginMap: Map<String, String>) {
            val map = HashMap<String, String>()
            map["authId"] = loginMap.get("openid")!!
            map["interfaceId"] = "0"
            map["mobile"] = mMobile
            map["nickName"] = loginMap.get("name")!!
            map["platformType"] = loginMap.get("platformType")!!
            map["url"] = loginMap.get("iconurl")!!
            map["userId"] = mUserId
            NetDialogObservable.getInstance().show()
            executeRequest({ mLoginRepository.loginByThirdPlatform(map) }, {
                mUserId = "" + it.userId
                mMobile = it.mobile
                mUserInfoLiveData.value = it
            }, {
                ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
            })
        }

        override fun onFailed(msg: String) {
            ToastUtil.showTextToast(BaseApp.sApplicaton, msg)
        }

    }
}