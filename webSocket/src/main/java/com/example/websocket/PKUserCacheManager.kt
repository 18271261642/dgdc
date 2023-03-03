package com.example.websocket

import com.example.websocket.bean.JoinUser
import com.example.websocket.bean.PkUsers

/**
 *  Created by BeyondWorlds
 *  on 2020/8/1
 */
object PKUserCacheManager {


    var mPkUsers = mutableListOf<PkUsers>()
    var pkuserName: HashMap<String, String> = HashMap<String, String>()
    var pkuserHeadUrl: HashMap<String, String> = HashMap<String, String>()
    fun addValue(userId: String, nikeName: String, headUrl: String) {
        pkuserName.put(userId, nikeName)
        pkuserHeadUrl.put(userId, headUrl)
    }


    fun clearMap() {
        pkuserName.clear()
        pkuserHeadUrl.clear()
    }

    fun removeUers(userId: String) {
        var i = 0
        while (i < mPkUsers.size) {
            if (mPkUsers.get(i).userId.equals(userId)) {
                mPkUsers.removeAt(i)
                break
            }
            i++
        }
    }

    fun addPKUsers(bean: JoinUser) {
        var tempBean = mPkUsers.findLast { bean.userId.equals(it.userId) }
        if (tempBean == null) {
            //String userId, String nickName, String avatar, String pkRecordId, long createTimestamp, boolean creatorFlag
            mPkUsers.add(
                PkUsers(
                    bean.userId,
                    bean.nickName,
                    bean.avatar,
                    bean.pkId,
                    bean.createTimestamp,
                    false
                )
            )
        } else {
            tempBean.avatar = bean.avatar;
            tempBean.nickName = bean.nickName;
            tempBean.pkRecordId = bean.pkId;
            tempBean.createTimestamp = bean.createTimestamp;
        }
    }

    fun addPkUsers(list: MutableList<PkUsers>) {
        mPkUsers.clear()
        mPkUsers.addAll(list)
    }

}