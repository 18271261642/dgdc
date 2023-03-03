package com.jkcq.homebike.ble.setting

import android.content.Intent
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.example.websocket.observable.PkObservable
import com.example.websocket.observable.TimeOutObservable
import com.jkcq.base.app.BaseApp
import com.jkcq.base.app.Preference
import com.jkcq.base.base.BaseActivity
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.ble.devicescan.bike.BikeDeviceConnectViewModel
import com.jkcq.homebike.ble.devicescan.hr.DeviceScanViewModel
import com.jkcq.homebike.ota.OtaActivity
import com.jkcq.util.AppUtil
import com.jkcq.util.ConnectDeviceDialog
import com.jkcq.util.OnButtonListener
import com.jkcq.util.YesOrNoDialog
import com.jkcq.util.ktx.ToastUtil
import com.jkcq.viewlibrary.TitleView
import kotlinx.android.synthetic.main.activity_bike_device_setting.*
import kotlinx.android.synthetic.main.activity_exercise_record.*
import kotlinx.android.synthetic.main.activity_exercise_record.view_title

class BikeDeviceSettingActivity : BaseVMActivity<BikeDeviceConnectViewModel>() {

    val mDeviceScanViewModel: DeviceScanViewModel by lazy { createViewModel(DeviceScanViewModel::class.java) }
    val mBikeDeviceConnectViewModel: BikeDeviceConnectViewModel by lazy {
        createViewModel(
            BikeDeviceConnectViewModel::class.java
        )

    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_bike_device_setting
    }

    override fun initView() {
    }

    override fun initData() {

        updateValue()
        mBikeDeviceConnectViewModel.setCallBack(this)
    }


    fun updateValue() {
        if (BikeConfig.BikeConnState == BikeConfig.BIKE_CONN_SUCCESS) {
            btn_conn.visibility = View.GONE
            tv_device_state.text = this.getString(R.string.device_state_success)
        } else {
            btn_conn.visibility = View.VISIBLE
            tv_device_state.text = this.getString(R.string.device_state_fail)
        }
        if (!TextUtils.isEmpty(BikeConfig.currentVersion)) {
            mVersion = BikeConfig.currentVersion
        }
        if (TextUtils.isEmpty(mVersion)) {

        } else {
            tv_version.text = mVersion
        }
    }

    override fun initEvent() {
        super.initEvent()
        tv_ota.setOnClickListener {
            if (BikeConfig.BIKE_CONN_SUCCESS == BikeConfig.BikeConnState) {
                startActivity(Intent(this, OtaActivity::class.java))
            } else {
                startActivity(Intent(this, OtaActivity::class.java))
                ToastUtil.showTextToast(BaseApp.sApplicaton, this.getString(R.string.device_unbind))
            }
        }


        view_title.onTitleOnClick = object : TitleView.OnTitleViewOnclick {
            override fun onLeftOnclick() {
                finish()
            }

            override fun onRightOnclick() {


                showEndPk()

                // finish()
            }

            override fun onCalenderOnclick() {
            }
        }

        btn_conn.setOnClickListener {
            mDeviceScanViewModel.startLeScan()
        }
    }


    var endPk: YesOrNoDialog? = null
    fun showEndPk() {

        if (endPk != null && endPk!!.isShowing) {
            return
        }
        var endValue = this.resources.getString(R.string.device_unbind_tips)


        endPk = YesOrNoDialog(
            this,
            "",
            endValue,
            "",
            this.resources.getString(R.string.device_setting_unbind)

        )
        endPk?.setBtnOnclick(object : OnButtonListener {
            override fun onCancleOnclick() {
            }

            override fun onSureOnclick() {

                mBikeDeviceConnectViewModel.unBindeDevice()
                finish()
            }
        })
        endPk?.show()

    }

    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                //连接超时
                1001 -> {
                    hideConnectDialog()
                    ToastUtil.showTextToast(
                        this@BikeDeviceSettingActivity,
                        this@BikeDeviceSettingActivity.getString(R.string.device_state_fail)
                    )

                }
            }
        }
    }
    var connecetHr: ConnectDeviceDialog? = null

    fun hideConnectDialog() {
        updateValue()
        handler.removeMessages(1001)
        if (connecetHr != null && connecetHr!!.isShowing) {
            connecetHr!!.dismiss()
            connecetHr = null
            finish()
        }
    }

    fun showConnetDialog() {


        Log.e("showConnetDialog", "showConnetDialog---")
        handler.removeMessages(1001)
        handler.sendEmptyMessageDelayed(1001, 10000)
        if (connecetHr != null && connecetHr!!.isShowing) {
            return
        }
        connecetHr = ConnectDeviceDialog(this, "");
        connecetHr?.show()
    }


    override fun startObserver() {

        mDeviceScanViewModel.mDeviceLiveData.observe(this, Observer {


            it.forEach {
                if (!TextUtils.isEmpty(it.name)) {
                    if (it.name.equals(mBikeName)) {
                        mDeviceScanViewModel.stopLeScan()
                        mBikeDeviceConnectViewModel.conectBikeDevice(it.device)
                    }
                }
            }

        })
        mBikeDeviceConnectViewModel.mDeviceConnState.observe(this, Observer {
            if (it == BikeConfig.BIKE_CONN_SUCCESS) {
                hideConnectDialog()
            }

        })
    }
}