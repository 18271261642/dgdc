package com.jkcq.base.net.bean

/**
 *  created by BeyondWorlds
 *  on 2020/6/9
 */
class BaseResponse<T>(val code: Int, val message: String, val message_en: String, val obj: T)