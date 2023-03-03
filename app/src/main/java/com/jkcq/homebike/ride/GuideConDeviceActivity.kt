package com.jkcq.homebike.ride

import android.content.IntentFilter
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.ble.devicescan.hr.DeviceScanViewModel
import com.jkcq.util.LoadImageUtil
import com.jkcq.viewlibrary.TitleView
import kotlinx.android.synthetic.main.activity_exercise_record.*
import kotlinx.android.synthetic.main.activity_exercise_record.view_title
import kotlinx.android.synthetic.main.activity_guide_con_device.*

class GuideConDeviceActivity : BaseVMActivity<DeviceScanViewModel>() {


    override fun getLayoutResId(): Int {
        return R.layout.activity_guide_con_device
    }

    override fun initView() {
    }


    override fun initData() {
        view_title.setTitleText(this.resources.getString(R.string.guide_conn_device_title))
        LoadImageUtil.getInstance().loadGif(this, R.drawable.connect_describe, iv_bg)
    }


    override fun onPause() {
        super.onPause()
    }


    override fun onStart() {
        super.onStart()
    }


    override fun onResume() {
        super.onResume()
        //android 23以上才判断有没有开定位服务

    }


    override fun initEvent() {
        super.initEvent()
        view_title.onTitleOnClick = object : TitleView.OnTitleViewOnclick {
            override fun onLeftOnclick() {

                finish()
            }

            override fun onRightOnclick() {
                finish()
            }

            override fun onCalenderOnclick() {
            }
        }
    }

    override fun startObserver() {


    }
}