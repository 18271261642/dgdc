package com.jkcq.homebike.ble.devicescan.hr

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.example.utillibrary.PermissionUtil
import com.jkcq.base.app.BaseApp
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.ble.battery.LoggableBleManager
import com.jkcq.homebike.ble.scanner.ExtendedBluetoothDevice
import com.jkcq.util.LogUtil
import com.jkcq.util.ktx.ToastUtil
import com.jkcq.viewlibrary.TitleView
import kotlinx.android.synthetic.main.activity_bike_device_scan.*
import kotlinx.android.synthetic.main.activity_exercise_record.view_title
import no.nordicsemi.android.ble.BleManagerCallbacks
import java.util.*


class HrDeviceScanActivity : BaseVMActivity<DeviceScanViewModel>() {
    lateinit var map: TreeMap<String, ExtendedBluetoothDevice>
    private val bleManager: LoggableBleManager<out BleManagerCallbacks?>? = null
    val mDeviceScanViewModel: DeviceScanViewModel by lazy { createViewModel(DeviceScanViewModel::class.java) }
    val mHrDeviceConnectViewModel: HrDeviceConnectViewModel by lazy {

        createViewModel(
            HrDeviceConnectViewModel::class.java
        )

    }

    var mDataList = mutableListOf<ExtendedBluetoothDevice>()
    lateinit var mHrDeviceListAdapter: HrDeviceListAdapter

    override fun getLayoutResId(): Int {
        return R.layout.activity_bike_device_scan
    }

    override fun initView() {
    }

    override fun initData() {
        map = TreeMap<String, ExtendedBluetoothDevice>()
        initSportDetailRec()
        initPermission()
        mHrDeviceConnectViewModel.setCallBack(this)
    }


    fun initSportDetailRec() {
        recycle_device_list.layoutManager = LinearLayoutManager(this)
        mHrDeviceListAdapter =
            HrDeviceListAdapter(mDataList)
        recycle_device_list.adapter = mHrDeviceListAdapter

        mHrDeviceListAdapter.setOnItemClickListener(OnItemClickListener { adapter, view, position ->

            mDeviceScanViewModel.stopLeScan()


            mHrDeviceConnectViewModel.conectHrDevice(mDataList.get(position).device)


        })
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


    fun initPermission() {
        PermissionUtil.checkPermission(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissonCallback = object : PermissionUtil.OnPermissonCallback {
                override fun isGrant(grant: Boolean) {
                    if (grant) {
                        mDeviceScanViewModel.startLeScan()
                    } else {
                        ToastUtil.showTextToast(
                            BaseApp.sApplicaton,
                            this@HrDeviceScanActivity.getString(R.string.no_required_permission)
                        )
                    }
                }
            })
    }


    override fun startObserver() {


        mDeviceScanViewModel.mDeviceLiveData.observe(this, Observer {
            LogUtil.e("startLeScan6")
            it.forEach {
                if (it.name.equals(mBikeName)) {
                    if (!it.name.contains("S003")) {
                        map.put(it.name, it)
                    }
                }
            }
            mDataList.clear()
            map.forEach {
                mDataList.add(it.value)
            }
            mHrDeviceListAdapter.notifyDataSetChanged()

        })

        mHrDeviceConnectViewModel.mDeviceHrValue.observe(this, Observer {
            Log.e("mDeviceHrValue", "" + it)

        })
        mHrDeviceConnectViewModel.mDeviceConnState.observe(this, Observer {

            if (it == 1) {
                //finish();
            }

        })

        /*mDeviceScanViewModel.mDeviceLiveData.observe(this, Observer {
            LogUtil.e("startLeScan6")
            for (i in it.indices) {
                mDataList.add(it.get(i))
            }
            mHrDeviceListAdapter.notifyDataSetChanged()
        })*/

        /*mDeviceScanViewModel.mDeviceLiveData.observe(this, Observer {

        })*/
    }


    private fun getAppDetailSettingIntent(): Intent? {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            localIntent.data = Uri.fromParts("package", packageName, null)
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.action = Intent.ACTION_VIEW
            localIntent.setClassName(
                "com.android.settings",
                "com.android.settings.InstalledAppDetails"
            )
            localIntent.putExtra("com.android.settings.ApplicationPkgName", packageName)
        }
        return localIntent
    }


}

