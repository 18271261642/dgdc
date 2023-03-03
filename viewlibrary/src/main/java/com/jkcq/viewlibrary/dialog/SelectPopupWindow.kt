package com.jkcq.viewlibrary.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.PopupWindow
import android.widget.TextView
import com.jkcq.util.LogUtil
import com.jkcq.util.SiseUtil
import com.jkcq.viewlibrary.R
import com.jkcq.viewlibrary.dialog.dialog.BaseDialog
import com.jkcq.viewlibrary.pickerview.ArrayPickerView
import com.jkcq.viewlibrary.pickerview.DatePickerView
import java.util.*
import kotlin.collections.ArrayList

/**
 *  Created by BeyondWorlds
 *  on 2020/7/31
 */
class SelectPopupWindow(var mSelectPopupListener: OnSelectPopupListener?) :
    ArrayPickerView.ItemSelectedValue {

    companion object {
        const val GENDER = "gender"
        const val HEIGHT = "height"
        const val WEIGHT = "weight"
        const val PK_NUMBER = "pknumber"
        const val BIRTHDAY = "birthday"
    }

    // private var mMenuView: View? = null
    private var localData = ArrayList<String>()
    private var localDataNoUnit = ArrayList<String>()
    private val genderDatas = arrayOf<String>("男", "女")


    //   private var popupWindow: PopupWindow? = null
    private var localChooseStr: String? = null
    private var selectType: String? = null
    private var showIndex = 0


    /**
     * @param context
     * @param view
     * @param type
     * @param lastData 上次的数据
     */
    fun popWindowSelect(
        context: Context,
        view: View?,
        type: String?,
        lastData: String,
        isCyclic: Boolean
    ) {
//        AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
//        int checkOp = appOpsManager.checkOp(AppOpsManager.OPSTR_CAMERA, android.os.Process.myUid(), getPackageName());
        selectType = type
        localData.clear()
        localDataNoUnit.clear()
        when (type) {
            PK_NUMBER -> {
                var i = 2
                while (i < 7) {
                    localData.add(i.toString())
                    localDataNoUnit.add(i.toString())
                    i++
                }
                i = 0
                while (i < localData.size) {
                    if (localData[i] == lastData) {
                        showIndex = i
                    }
                    i++
                }
            }
            GENDER -> {
                localData.add(context.getString(R.string.gender_male))
                localData.add(context.getString(R.string.gender_female))
                localDataNoUnit.add(context.getString(R.string.gender_male))
                localDataNoUnit.add(context.getString(R.string.gender_female))
//                genderDatas[0] = context.getString(R.string.gender_male)
//                genderDatas[1] = context.getString(R.string.gender_female)
//                localData = genderDatas.toCollection(localData)
//              localDataNoUnit = genderDatas.toCollection(localDataNoUnit)
                var i = 0
                while (i < localData.size) {
                    if (localData[i] == lastData) {
                        showIndex = i
                    }
                    i++
                }
            }
            HEIGHT -> {
                var j = 0
                while (j < 101) {
                    localData.add((j + 140).toString())
                    localDataNoUnit.add("${j + 140} cm")
//                    heightDataNoUnit[j] = (j + 140).toString()
//                    heightData[j] = "${j + 140} cm"
                    if (localDataNoUnit[j] == lastData) {
                        showIndex = j
                    }
                    j++
                }
//                localData = heightData
//                localDataNoUnit = heightDataNoUnit
            }
            WEIGHT -> {
                var j = 0

                while (j < 300) {
                    localData.add(j.toString())
                    localDataNoUnit.add("$j kg")
//                    weightDataNoUnit[j] = j.toString()
//                    weightData[j] = "$j kg"


                    if (localDataNoUnit[j] == lastData) {
                        showIndex = j
                    }
                    LogUtil.e("localDataNoUnit[j] == lastData" + localDataNoUnit[j] + ",lastData" + lastData + "showIndex" + showIndex)
                    j++
                }
//                localData = weightData
//                localDataNoUnit = weightDataNoUnit
            }
        }


        val mMenuView: BaseDialog = BaseDialog.Builder(context)
            .setContentView(R.layout.pop_bottom_setting)
            .fullWidth()
            .setCanceledOnTouchOutside(true)
            .fromBottom(true)
            .setOnClickListener(R.id.tv_cancel,
                DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.cancel() })
            .setOnClickListener(R.id.tv_determine,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                })
            .show()


//        val inflater = context
//            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val tv_determine =
            mMenuView!!.findViewById<View>(R.id.tv_determine) as TextView
        val tv_cancel =
            mMenuView!!.findViewById<View>(R.id.tv_cancel) as TextView
        val datePicker: ArrayPickerView =
            mMenuView!!.findViewById<View>(R.id.datePicker) as ArrayPickerView
        datePicker.setData(localData)
        datePicker.setCyclic(isCyclic)
        datePicker.setItemOnclick(this)
        datePicker.setSelectItem(showIndex)

//        localChooseStr = localData[showIndex]
        localChooseStr = localDataNoUnit[showIndex];
        tv_determine.setOnClickListener {

            mSelectPopupListener?.onSelect(selectType!!, localChooseStr!!)
            mMenuView!!.dismiss()
        }
        tv_cancel.setOnClickListener { mMenuView!!.dismiss() }
    }

    fun popWindowSelect(
        context: Context,
        view: View?,
        type: String?,
        starvalue: Int,
        endValue: Int,
        lastData: String,
        isCyclic: Boolean
    ) {
//        AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
//        int checkOp = appOpsManager.checkOp(AppOpsManager.OPSTR_CAMERA, android.os.Process.myUid(), getPackageName());
        selectType = type
        localData.clear()
        localDataNoUnit.clear()
        when (type) {
            PK_NUMBER -> {
                var i = starvalue
                while (i <= endValue) {
                    localData.add(i.toString())
                    localDataNoUnit.add(i.toString())
                    i++
                }
                i = 0
                while (i < localData.size) {
                    if (localData[i] == lastData) {
                        showIndex = i
                    }
                    i++
                }
            }
            GENDER -> {
                localData.add(context.getString(R.string.gender_male))
                localData.add(context.getString(R.string.gender_female))
                localDataNoUnit.add(context.getString(R.string.gender_male))
                localDataNoUnit.add(context.getString(R.string.gender_female))
//                genderDatas[0] = context.getString(R.string.gender_male)
//                genderDatas[1] = context.getString(R.string.gender_female)
//                localData = genderDatas.toCollection(localData)
//              localDataNoUnit = genderDatas.toCollection(localDataNoUnit)
                var i = 0
                while (i < localData.size) {
                    if (localData[i] == lastData) {
                        showIndex = i
                    }
                    i++
                }
            }
            HEIGHT -> {
                var j = 0
                while (j < 101) {
                    localData.add((j + 140).toString())
                    localDataNoUnit.add("${j + 140} cm")
//                    heightDataNoUnit[j] = (j + 140).toString()
//                    heightData[j] = "${j + 140} cm"
                    if (localDataNoUnit[j] == lastData) {
                        showIndex = j
                    }
                    j++
                }
//                localData = heightData
//                localDataNoUnit = heightDataNoUnit
            }
            WEIGHT -> {
                var j = 0

                while (j < 300) {
                    localData.add(j.toString())
                    localDataNoUnit.add("$j kg")
//                    weightDataNoUnit[j] = j.toString()
//                    weightData[j] = "$j kg"


                    if (localDataNoUnit[j] == lastData) {
                        showIndex = j
                    }
                    LogUtil.e("localDataNoUnit[j] == lastData" + localDataNoUnit[j] + ",lastData" + lastData + "showIndex" + showIndex)
                    j++
                }
//                localData = weightData
//                localDataNoUnit = weightDataNoUnit
            }
        }
//        val inflater = context
//            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


        val mMenuView: BaseDialog = BaseDialog.Builder(context)
            .setContentView(R.layout.pop_bottom_setting)
            .fullWidth()
            .setCanceledOnTouchOutside(true)
            .fromBottom(true)
            .setOnClickListener(R.id.tv_cancel,
                DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.cancel() })
            .setOnClickListener(R.id.tv_determine,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                })
            .show()


        val tv_determine =
            mMenuView!!.findViewById<View>(R.id.tv_determine) as TextView
        val datePicker: ArrayPickerView =
            mMenuView!!.findViewById<View>(R.id.datePicker) as ArrayPickerView
        datePicker.setData(localData)
        datePicker.setCyclic(isCyclic)
        datePicker.setItemOnclick(this)
        datePicker.setSelectItem(showIndex)

//        localChooseStr = localData[showIndex]
        localChooseStr = localDataNoUnit[showIndex];
        tv_determine.setOnClickListener {
            mSelectPopupListener?.onSelect(selectType!!, localChooseStr!!)
            mMenuView!!.dismiss()
        }
    }

    override fun onItemSelectedValue(str: String?) {
        localChooseStr = str
    }

    //温度设置选择器
    private var mTmeapView: View? = null
    private var popupWindowTemp: PopupWindow? = null

    // 时间选择器

    // 时间选择器
    fun setPopupWindowTemp(
        context: Context,
        view: View?,
        type: String,
        value: Int,
        point: Int,
        userUnit: String,
        unit: String
    ) {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mTmeapView = inflater.inflate(R.layout.pop_bottom_temp, null)
        val tv_determine =
            mTmeapView!!.findViewById<View>(R.id.tv_determine) as TextView
        val tv_cancel =
            mTmeapView!!.findViewById<View>(R.id.tv_cancel) as TextView
        val datePicker1: ArrayPickerView =
            mTmeapView!!.findViewById<View>(R.id.datePicker1) as ArrayPickerView
        val datePicker2: ArrayPickerView =
            mTmeapView!!.findViewById<View>(R.id.datePicker2) as ArrayPickerView
        val datePicker3: ArrayPickerView =
            mTmeapView!!.findViewById<View>(R.id.datePicker3) as ArrayPickerView
        val datePicker_point: ArrayPickerView =
            mTmeapView!!.findViewById<View>(R.id.datePicker_point) as ArrayPickerView
        popupWindowTemp = PopupWindow(context)
        popupWindowTemp!!.width = ViewGroup.LayoutParams.MATCH_PARENT
        popupWindowTemp!!.height = ViewGroup.LayoutParams.WRAP_CONTENT
        //popupWindowBirth.setHeight(DisplayUtils.dip2px(context, 150));
        popupWindowTemp!!.contentView = mTmeapView
        popupWindowTemp!!.setBackgroundDrawable(ColorDrawable(0x00000000))
        popupWindowTemp!!.isOutsideTouchable = false
        popupWindowTemp!!.isFocusable = true
        popupWindowTemp!!.animationStyle = R.style.popwin_anim_style
        popupWindowTemp!!.showAtLocation(
            view, Gravity.BOTTOM
                    or Gravity.CENTER_HORIZONTAL, 0, 0
        )
        var showIndex1 = 0
        var showIndex2 = 0
        var showIndex3 = 0
        showIndex3 = point
        val localData2 = ArrayList<String>()
        when (type) {


            HEIGHT -> {

                var startIndex = (SiseUtil.heightUnitCoversion(140.0f, userUnit)).toFloat().toInt()
                var sumCount = (SiseUtil.heightUnitCoversion(101.0f, userUnit)).toFloat().toInt()
                var j = 0
                while (j < sumCount) {
                    localData2.add((j + startIndex).toString())
                    if (value == (j + startIndex)) {
                        showIndex2 = j
                    }
                    j++
                }
            }
            WEIGHT -> {
                var j = 0
                var startIndex = (SiseUtil.weightUnitCoversion(30.0f, userUnit)).toFloat().toInt()
                var sumCount = (SiseUtil.weightUnitCoversion(300f, userUnit)).toFloat().toInt()
                while (j <= sumCount) {
                    localData2.add((j + startIndex).toString())
                    if (value == (j + startIndex)) {
                        showIndex2 = j
                    }
//                    weightDataNoUnit[j] = j.toString()
//                    weightData[j] = "$j kg"
                    j++
                }
//                localData = weightData
//                localDataNoUnit = weightDataNoUnit
            }
        }

        val localData1 = ArrayList<String>()
        localData1.add(unit)


        val localData3 = ArrayList<String>()
        val localDataPoint =
            ArrayList<String>()
        localDataPoint.add(".")
        for (i in 0..9) {
            localData3.add(i.toString() + "")
        }
        datePicker1.setData(localData1)
        datePicker1.setCyclic(false)
        datePicker1.setItemOnclick(this)
        datePicker1.setSelectItem(showIndex1)
        datePicker_point.setData(localDataPoint)
        datePicker_point.setCyclic(false)
        datePicker_point.setSelectItem(0)
        datePicker2.setData(localData2)
        datePicker2.setCyclic(false)
        datePicker2.setItemOnclick(this)
        datePicker2.setSelectItem(showIndex2)
        datePicker3.setData(localData3)
        datePicker3.setCyclic(false)
        datePicker3.setItemOnclick(this)
        datePicker3.setSelectItem(showIndex3)
        mTmeapView!!.setOnTouchListener { v, event ->
            val height =
                mTmeapView!!.findViewById<View>(R.id.pop_layout).top
            val y = event.y.toInt()
            if (event.action == MotionEvent.ACTION_UP) {
                if (y < height) {
                    popupWindowTemp!!.dismiss()
                }
            }
            true
        }
        tv_determine.setOnClickListener {
            mSelectPopupListener?.let {
                val value: String =
                    datePicker2.getItem() + datePicker_point.getItem() + datePicker3.getItem()
                it.onSelect(type, value)
            }
            popupWindowTemp!!.dismiss()
        }
        tv_cancel.setOnClickListener { popupWindowTemp!!.dismiss() }
    }

    private var mMenuViewBirth: View? = null
    private var popupWindowBirth: PopupWindow? = null
    // 时间选择器

    // 时间选择器
    fun setPopupWindow(
        context: Context,
        view: View?,
        type: String,
        defaultDay: String?
    ) {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mMenuViewBirth = inflater.inflate(R.layout.pop_date, null)
        val tv_determine =
            mMenuViewBirth!!.findViewById<View>(R.id.tv_determine) as TextView
        val tv_cancel =
            mMenuViewBirth!!.findViewById<View>(R.id.tv_cancel) as TextView
        val datePicker: DatePickerView =
            mMenuViewBirth!!.findViewById<View>(R.id.datePicker) as DatePickerView
        if (type == "3") {
            datePicker.setType(3)
        }
        datePicker.setDefaultItemAdapter(defaultDay)
        datePicker.setCyclic(false)
        popupWindowBirth = PopupWindow(context)
        popupWindowBirth!!.width = ViewGroup.LayoutParams.MATCH_PARENT
        popupWindowBirth!!.height = ViewGroup.LayoutParams.WRAP_CONTENT
        //popupWindowBirth.setHeight(DisplayUtils.dip2px(context, 150));
        popupWindowBirth!!.contentView = mMenuViewBirth
        popupWindowBirth!!.setBackgroundDrawable(ColorDrawable(0x00000000))
        popupWindowBirth!!.isOutsideTouchable = false
        popupWindowBirth!!.isFocusable = true
        popupWindowBirth!!.animationStyle = R.style.popwin_anim_style
        popupWindowBirth!!.showAtLocation(
            view, Gravity.BOTTOM
                    or Gravity.CENTER_HORIZONTAL, 0, 0
        )
        mMenuViewBirth!!.setOnTouchListener { v, event ->
            val height =
                mMenuViewBirth!!.findViewById<View>(R.id.pop_layout).top
            val y = event.y.toInt()
            if (event.action == MotionEvent.ACTION_UP) {
                if (y < height) {
                    popupWindowBirth!!.dismiss()
                }
            }
            true
        }
        tv_determine.setOnClickListener { //                calculationAgeAndConstellation(datePicker.getTime());
            //                localUserChooseBirthday = datePicker.getTime();
            mSelectPopupListener?.onSelect(type, datePicker.getTime())
            popupWindowBirth!!.dismiss()
        }
        tv_cancel.setOnClickListener { popupWindowBirth!!.dismiss() }
    }

    interface OnSelectPopupListener {
        fun onSelect(type: String, data: String)
    }
}