package com.jkcq.util

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.os.Build
import android.os.LocaleList
import java.util.*

/**
 *  Created by BeyondWorlds
 *  on 2020/7/29
 */
object AppUtil {
    lateinit var mApp: Application

    /**
     * application中初始化，util中需要用到context的地方都可以从这里拿
     */
    fun init(app: Application) {
        mApp = app
    }

    fun getApp(): Application {
        if (mApp == null) {
            throw NullPointerException("u should init first")
        }
        return mApp
    }

    fun isUSA(): Boolean {
        return !isCN()
    }

    fun isES(): Boolean {
        return getLocale().language == "es"
    }

    fun isCN(): Boolean {
        return getLocale().language == "zh"
    }

    private fun getLocale(): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.getDefault()[0]
        } else {
            Locale.getDefault()
        }
    }

    var bleAdapter: BluetoothAdapter? = null

    fun isOpenBle(): Boolean {
        if (bleAdapter == null) {
            bleAdapter = BluetoothAdapter.getDefaultAdapter()
        }
        return if (bleAdapter == null || !bleAdapter!!.isEnabled) {
            false
        } else {
            true
        }
    }

    fun isZh(context: Context): Boolean {
        val locale = context.resources.configuration.locale
        val language = locale.language
        return if (language.contains("zh")) true else false
    }


    fun getModel(): String? {
        var model = Build.MODEL
        model = model?.trim { it <= ' ' }?.replace("\\s*".toRegex(), "") ?: ""
        return model
    }

}