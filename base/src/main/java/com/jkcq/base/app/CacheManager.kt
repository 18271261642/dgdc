package com.jkcq.base.app

import android.graphics.Bitmap
import android.text.TextUtils
import com.jkcq.base.net.bean.UserInfo
import java.util.*
import kotlin.collections.HashMap

/**
 *  Created by BeyondWorlds
 *  on 2020/8/1
 */
object CacheManager {
    var mUserInfo: UserInfo? = null
    var userBitmap = HashMap<String, Bitmap>()
    fun getAge(birthday: String): Int {
        var age = 18
        try {
            if (!TextUtils.isEmpty(birthday)) {
                val birthdays = birthday.split("-".toRegex()).toTypedArray()
                val calendar = Calendar.getInstance()
                val ageYear = birthdays[0].toInt()
                val currentYear = calendar[Calendar.YEAR]
                val ageMonth = birthdays[1].toInt()
                val ageDay = birthdays[2].toInt()
                val currentMonth = calendar[Calendar.MONTH] + 1
                val currentDay = calendar[Calendar.DAY_OF_MONTH]
                age = currentYear - ageYear
                if (currentMonth < ageMonth) {
                    age--
                } else if (currentMonth == ageMonth && currentDay < ageDay) {
                    age--
                }
            } else {
                age = 18
            }
        } catch (e: Exception) {
            age = 18
        } finally {
            return age
        }
    }
}