package com.beyondworlds.wanandroid.net.responseHandle

/**
 * 时间　: 2019/12/17
 * 描述　:自定义错误信息异常
 */
class AppException : Exception {

    var errorMsg: String //错误消息
    var errCode: Int = 0 //错误码
    var errorLog: String? //错误日志

    constructor(errCode: Int, error: String?, errorLog: String = "Request Failed") : super(error) {
        this.errorMsg = error ?: "error"
        this.errCode = errCode
        this.errorLog = errorLog
    }

    constructor(error: Error, e: Throwable?) {
        errCode = error.getKey()
        errorMsg = error.getValue()
        errorLog = e?.message
    }
}