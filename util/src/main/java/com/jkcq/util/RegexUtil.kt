package com.jkcq.util

import com.blankj.utilcode.util.RegexUtils

/**
 * 正则判断工具
 *  Created by BeyondWorlds
 *  on 2020/7/30
 */
object RegexUtil {

    fun isPhone(phone: String): Boolean {
        return RegexUtils.isMobileSimple(phone)
    }

    fun isEmail(email: String): Boolean {
        return RegexUtils.isEmail(email)
    }
}