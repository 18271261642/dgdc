package com.jkcq.util

import android.text.TextUtils
import com.blankj.utilcode.util.SizeUtils

/**
 *  Created by BeyondWorlds
 *  on 2020/7/28
 */
object SiseUtil {

    const val METRIC_UNITS = "0"
    const val ENGLISH_UNITS = "1"

    fun dip2px(dpValue: Float): Int {
        return SizeUtils.dp2px(dpValue)
    }

    //单位为秒转换成分钟
    fun timeCoversionMin(time: String): String {


        val hms: DateUtil.HMS = DateUtil.getHMSFromMillis(time.toLong() * 1000)

        if (hms.getHour() != 0) {


            if (AppUtil.isCN()) {
                return String.format(
                    "%2d时%2d'%2d\"",
                    hms.getHour(),
                    hms.getMinute(),
                    hms.getSecond()
                )
            } else {
                return String.format(
                    "%2dh%2d'%2d\"",
                    hms.getHour(),
                    hms.getMinute(),
                    hms.getSecond()
                )
            }

        } else {
            return String.format(
                "%2d'%2d\"",
                hms.getMinute(),
                hms.getSecond()
            )
        }

        /*  if (TextUtils.isEmpty(time)) {
              return "0"
          } else {
              return (time.toInt() / 60).toString()
          }*/
    }

    //单位为秒转换成分钟
    fun timeMillCoversionMin(time: String): String {


        val hms: DateUtil.HMS = DateUtil.getHMSFromMillis(time.toLong())

        if (hms.getHour() != 0) {


            if (AppUtil.isCN()) {
                return String.format(
                    "%2d时%2d'%2d\"",
                    hms.getHour(),
                    hms.getMinute(),
                    hms.getSecond()
                )
            } else {
                return String.format(
                    "%2dh%2d'%2d\"",
                    hms.getHour(),
                    hms.getMinute(),
                    hms.getSecond()
                )
            }

        } else {
            return String.format(
                "%2d'%2d\"",
                hms.getMinute(),
                hms.getSecond()
            )
        }

        /*  if (TextUtils.isEmpty(time)) {
              return "0"
          } else {
              return (time.toInt() / 60).toString()
          }*/
    }

    fun weightUnitCoversion(fWeight: Float, unit: String): String {
        var value = ""
        when (unit) {
            METRIC_UNITS -> {
                value = DateUtil.formatOnePoint(fWeight)
            }
            ENGLISH_UNITS -> {
                value = DateUtil.formatOnePoint(fWeight / 0.4536f)
            }
        }
        return value
    }

    fun mileToGongheight(fHeight: Float): String {
        var value = ""
        value = "" + fHeight * 2.54f
        return value
    }

    fun mileToGongWeight(fHeight: Float): String {
        var value = ""
        value = "" + fHeight * 0.4536f
        return value
    }

    fun heightUnitCoversion(fHeight: Float, unit: String): String {
        var value = ""
        when (unit) {
            METRIC_UNITS -> {
                value = DateUtil.formatOnePoint(fHeight)
            }
            ENGLISH_UNITS -> {
                value = DateUtil.formatOnePoint(fHeight / 2.54f)
            }
        }
        return value
    }


    //返回保留两位小数的字符串，传进来的数据为米，转换成千米，还是英里
    fun disUnitCoversion(dis: String, unit: String): String {

        var value = "0.00"
        var fDis = 0.0f
        if (!TextUtils.isEmpty(dis)) {
            fDis = dis.toFloat()
        }
        // 1 mile = 1.609 km
        when (unit) {
            METRIC_UNITS -> {
                value = DateUtil.formatTwoPoint(DateUtil.formatFloor(fDis, true))
            }
            ENGLISH_UNITS -> {
                value = DateUtil.formatTwoPoint(DateUtil.formatFloor(fDis / 1.609f, true))
            }
        }

        return value
    }

    //0.1kcal
    fun calCoversion(cal: String): String {
        var value = "0.0"
        var intCal = 0.0f
        try {
            if (TextUtils.isEmpty(cal)) {
                intCal = 0.0f
            } else {
                intCal = cal.toFloat()
            }

            value = DateUtil.formatOnePoint(DateUtil.formatFloor(intCal, true))
        } catch (e: Exception) {

        } finally {
            return value
        }


    }

    //0.01 kj
    fun powerCoversion(power: String): String {
        var value = "0.00"
        var intPower = 0.0f
        if (!TextUtils.isEmpty(power)) {
            intPower = power.toFloat()
            value = DateUtil.formatTwoPoint(DateUtil.formatFloor(intPower, true))
        }
        return value
    }

    fun weightUnitCoversion(dis: Float, unit: Int): Float {
        return 0f

    }

    fun heightUnitCoversion(dis: Float, unit: Int): Float {
        return 0f
    }
}