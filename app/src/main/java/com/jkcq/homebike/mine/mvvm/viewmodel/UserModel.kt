package com.jkcq.homebike.mine.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.jkcq.base.app.CacheManager
import com.jkcq.base.app.Preference
import com.jkcq.base.base.BaseViewModel
import com.jkcq.base.net.bean.UserInfo
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.login.LoginRepository
import com.jkcq.homebike.mine.mvvm.repository.UserRepository

/**
 *  Created by BeyondWorlds
 *  on 2020/7/31
 */
class UserModel : BaseViewModel() {
    val mUserRepository: UserRepository by lazy { UserRepository() }
    var mEditState = MutableLiveData<Boolean>()
    var mFeedBackUrl = MutableLiveData<String>()
    var mQuestionUrl = MutableLiveData<String>()
    var mUserInfo = MutableLiveData<UserInfo>()

    fun getUserInfo() {
        /**
         * {
        "interfaceId": 0,
        "mobile": "string",
        "userId": 4

         */
        val map = HashMap<String, String>()
        map.put("interfaceId", "0")
        map.put("mobile", "0")
        map.put("userId", mUserId)
        executeRequest({
            mUserRepository.getBasicInfo(map)
        }, {
            mUserInfo.value = it
            BikeConfig.userCurrentUtil = it.measurement
            BikeConfig.currentUser = it
            CacheManager.mUserInfo = it
            mUserId = it.userId
            mUserName = it.nickName
        }, {

        })
    }

    fun editUserInfo(
        gender: String,
        height: String,
        weight: String,
        birthday: String,
        name: String,
        measurement: String,
        myProfile: String
    ) {

        var genderEn = ""
        if (gender.equals("男")) {
            genderEn = "Male"
        } else if (gender.equals("女")) {
            genderEn = "Female"
        } else {
            genderEn = gender
        }
        val map = HashMap<String, String>()
        map["birthday"] = birthday
        map["nickName"] = name
        map["gender"] = genderEn
        map["height"] = height
        map["interfaceId"] = "0"
        map["mobile"] = mMobile
        map["occupation"] = ""
        map["userId"] = mUserId
        map["weight"] = weight
        map["measurement"] = measurement
        map["myProfile"] = myProfile
        executeRequest({
            mUserRepository.editUserInfo(map)
        }, {
            mEditState.value = true
        }, {
            mEditState.value = false
        })

    }

    fun setmeasurement(measurement: String) {
        val map = HashMap<String, String>()
        map.put("userId", mUserId)
        map.put("measurement", measurement)

        executeRequest({
            mUserRepository.getmeasurement(map)
        }, {
            mEditState.value = true
        }, {
            mEditState.value = it.errCode == 2000
        })
    }

    fun getFeedUrl() {
        executeRequest({
            mUserRepository.getFeedBackUrl()
        }, {
            mFeedBackUrl.value = it
        }, {
        })
    }

    fun getQuestionUrl() {
        executeRequest({
            mUserRepository.getQuestionUrl()
        }, {
            mQuestionUrl.value = it
        }, {
            mEditState.value = it.errCode == 2000
        })
    }

    fun editBgUrl(url: String) {
        executeRequest({
            mUserRepository.editBgUrl(url)
        }, {
            mQuestionUrl.value = it
        }, {
            mEditState.value = it.errCode == 2000
        })
    }
}