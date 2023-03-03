package com.jkcq.homebike.login

import android.content.Intent
import android.util.Log
import androidx.lifecycle.Observer
import com.jkcq.base.app.BaseApp
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.homebike.MainActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.mine.mvvm.viewmodel.UserModel
import com.jkcq.util.DateUtil
import com.jkcq.util.SiseUtil
import com.jkcq.util.StatusBarUtil
import com.jkcq.util.ktx.ToastUtil
import com.jkcq.viewlibrary.dialog.SelectPopupWindow
import kotlinx.android.synthetic.main.activity_supplement_user_info.*


class SupplementUserInfoActivity : BaseVMActivity<UserModel>() {

    val mUserModel: UserModel by lazy { createViewModel(UserModel::class.java) }

    override fun getLayoutResId(): Int = R.layout.activity_supplement_user_info


    var nikeName = ""
    var birthiday = ""
    var mGender = ""
    var mHeight = "170.0"
    var mWeight = "60.0"
    var measurement = "0"
    var srcnikeName = ""
    var srcbirthiday = ""
    var srcmGender = ""
    var srcmHeight = "170.0"
    var srcmWeight = "60.0"
    var srcmeasurement = "0"
    var mSelectPopupWindow: SelectPopupWindow? = null


    var currentUnitHeight = ""
    var currentUnitWeight = ""

    var currentHeightUnit = "cm"
    var currentWeightUnit = "kg"
    var currentIntHeight = 70
    var currentPointHeight = 0
    var currentWeighInt = 50
    var currentWeightPoint = 0

    override fun initView() {

        iv_back.setOnClickListener { finish() }
        itemview_birthday.setOnClickListener {
            mSelectPopupWindow?.setPopupWindow(
                this@SupplementUserInfoActivity,
                itemview_birthday,
                SelectPopupWindow.BIRTHDAY,
                itemview_birthday.getRightText()
            )
        }
        itemview_select_line.setOnClickListener {
            mSelectPopupWindow?.popWindowSelect(
                this@SupplementUserInfoActivity,
                itemview_select_line,
                SelectPopupWindow.GENDER,
                itemview_select_line.getRightText(), false
            )
        }
        itemview_pk_number.setOnClickListener {
            mSelectPopupWindow?.setPopupWindowTemp(
                this@SupplementUserInfoActivity,
                itemview_pk_number,
                SelectPopupWindow.HEIGHT,
                currentIntHeight,
                currentPointHeight,
                BikeConfig.userCurrentUtil,
                currentHeightUnit
            )
        }
        itemview_weight.setOnClickListener {


            mSelectPopupWindow?.setPopupWindowTemp(
                this@SupplementUserInfoActivity,
                itemview_weight,
                SelectPopupWindow.WEIGHT,
                currentWeighInt,
                currentWeightPoint,
                BikeConfig.userCurrentUtil,
                currentWeightUnit
            )
        }

        btn_commit.setOnClickListener {
            if (et_subname.text.toString().isBlank()) {
                ToastUtil.showTextToast(BaseApp.sApplicaton,getString(R.string.can_not_bank_nickname))
                return@setOnClickListener
            }
            var value = rbtn_metric_units.isChecked
            var measurement = "0"
            if (value) {
                measurement = "0"
            } else {
                measurement = "1"
            }
            mUserModel.editUserInfo(
                itemview_select_line.getRightText(),
                mHeight,
                mWeight,
                itemview_birthday.getRightText(), et_subname.text.toString(),
                measurement,
                ""
            )
        }
    }

    override fun initEvent() {
        super.initEvent()
        rbtn_metric_units.setOnClickListener {
            measurement = BikeConfig.METRIC_UNITS
            BikeConfig.userCurrentUtil = measurement
            setUserInfo()
        }
        rbtn_imperial_unit.setOnClickListener {
            measurement = BikeConfig.ENGLISH_UNITS
            BikeConfig.userCurrentUtil = measurement
            setUserInfo()
        }
    }

    override fun initData() {
        mUserModel.getUserInfo()
        mSelectPopupWindow = SelectPopupWindow(object : SelectPopupWindow.OnSelectPopupListener {
            override fun onSelect(type: String, data: String) {
                when (type) {
                    SelectPopupWindow.BIRTHDAY -> {
                        itemview_birthday.setRightText(data)
                    }
                    SelectPopupWindow.HEIGHT -> {
                        if (measurement.equals("0")) {
                            mHeight = data
                        } else {

                            mHeight = SiseUtil.mileToGongheight(data.toFloat())

                        }
                        currentUnitHeight = data
                        var stringsHeight = currentUnitHeight.split(".")
                        currentIntHeight = stringsHeight[0].toInt()
                        currentPointHeight = stringsHeight[1].toInt()
                        itemview_pk_number.setRightText(data + currentHeightUnit)
                    }
                    SelectPopupWindow.WEIGHT -> {
                        if (measurement.equals("0")) {
                            mWeight = data
                        } else {
                            mWeight = SiseUtil.mileToGongWeight(data.toFloat())
                        }
                        currentUnitWeight = data
                        var stringsWeight = currentUnitWeight.split(".")
                        currentWeighInt = stringsWeight[0].toInt()
                        currentWeightPoint = stringsWeight[1].toInt()
                        itemview_weight.setRightText(data + currentWeightUnit)
                    }
                    SelectPopupWindow.GENDER -> {
                        itemview_select_line.setRightText(data)
                    }
                }
            }

        })
    }

    override fun startObserver() {
        mUserModel.mEditState.observe(this, Observer {
            if (it) {
                startActivity(Intent(this@SupplementUserInfoActivity, MainActivity::class.java))
            }
        })
        mUserModel.mUserInfo.observe(this, Observer {
            nikeName = it.nickName
            et_subname.setText(nikeName)
            measurement = it.measurement
            mHeight = DateUtil.formatfloorOnePoint(it.height)
            mWeight = DateUtil.formatfloorOnePoint(it.weight)
            srcnikeName = it.nickName
            srcbirthiday = it.birthday
            srcmeasurement = it.measurement
            if (it.gender.equals("Male")) {
                srcmGender = this.getString(R.string.gender_male)
                mGender = this.getString(R.string.gender_male)
            } else {
                srcmGender = this.getString(R.string.gender_female)
                mGender = this.getString(R.string.gender_female)
            }
            if (measurement.equals("0")) {
                currentHeightUnit = this.resources.getString(R.string.user_height_cm)
                currentWeightUnit = this.resources.getString(R.string.user_weight_kg)
            } else {
                currentHeightUnit = this.resources.getString(R.string.user_height_inch)
                currentWeightUnit = this.resources.getString(R.string.user_weight_lb)
            }


            currentUnitHeight =
                SiseUtil.heightUnitCoversion(
                    it.height,
                    measurement
                )
            currentUnitWeight =
                SiseUtil.weightUnitCoversion(
                    it.weight,
                    measurement
                )


            var stringsHeight = currentUnitHeight.split(".")
            currentIntHeight = stringsHeight[0].toInt()
            currentPointHeight = stringsHeight[1].toInt()
            var stringsWeight = currentUnitWeight.split(".")
            currentWeighInt = stringsWeight[0].toInt()
            currentWeightPoint = stringsWeight[1].toInt()

            Log.e(
                "initDataUser",
                "srcmWeight=" + srcmWeight + "currentUnitWeight =" + currentUnitWeight + " currentWeighInt =" + currentWeighInt + " currentWeightPoint =" + currentWeightPoint
            )
            Log.e(
                "initDataUser",
                "currentIntHeight=" + currentIntHeight + "currentPointHeight=" + currentPointHeight
            )


            itemview_pk_number.setRightText(currentUnitHeight + currentHeightUnit)
            itemview_weight.setRightText(currentUnitWeight + currentWeightUnit)
            itemview_birthday.setRightText(srcbirthiday)
            itemview_select_line.setRightText(mGender)
        })
    }

    fun setUserInfo() {
        if (measurement.equals("0")) {
            currentHeightUnit = this.resources.getString(R.string.user_height_cm)
            currentWeightUnit = this.resources.getString(R.string.user_weight_kg)
        } else {
            currentHeightUnit = this.resources.getString(R.string.user_height_inch)
            currentWeightUnit = this.resources.getString(R.string.user_weight_lb)
        }


        currentUnitHeight =
            SiseUtil.heightUnitCoversion(mHeight.toFloat(), BikeConfig.userCurrentUtil)
        currentUnitWeight =
            SiseUtil.weightUnitCoversion(mWeight.toFloat(), BikeConfig.userCurrentUtil)

        var stringsHeight = currentUnitHeight.split(".")
        currentIntHeight = stringsHeight[0].toInt()
        currentPointHeight = stringsHeight[1].toInt()
        var stringsWeight = currentUnitWeight.split(".")
        currentWeighInt = stringsWeight[0].toInt()
        currentWeightPoint = stringsWeight[1].toInt()
        itemview_pk_number.setRightText(currentUnitHeight + currentHeightUnit)
        itemview_weight.setRightText(currentUnitWeight + currentWeightUnit)
    }


    override fun setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, iv_back)
        StatusBarUtil.setLightMode(this)
    }
}