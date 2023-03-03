package com.jkcq.util

import com.blankj.utilcode.util.ConvertUtils

/**
 *  Created by BeyondWorlds
 *  on 2020/7/30
 */
object ConvertUtil {
    fun dp2px(dpValue: Float): Int {
        return ConvertUtils.dp2px(dpValue)
    }

    fun sp2px(spValue: Float): Int {
        return ConvertUtils.dp2px(spValue)
    }
}