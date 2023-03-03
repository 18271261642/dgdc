package com.jkcq.homebike.ble.devicescan.bike

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.example.utillibrary.PermissionUtil
import com.jkcq.base.app.BaseApp
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.ble.devicescan.hr.DeviceScanViewModel
import com.jkcq.homebike.ble.scanner.ExtendedBluetoothDevice
import com.jkcq.homebike.ride.util.TimeUtils
import com.jkcq.util.*
import com.jkcq.util.ktx.ToastUtil
import com.jkcq.viewlibrary.TitleView
import kotlinx.android.synthetic.main.activity_bike_device_scan.*
import kotlinx.android.synthetic.main.activity_exercise_record.view_title
import kotlinx.android.synthetic.main.view_bike_disconnect.*
import java.util.*

class BikeDeviceScanActivity : BaseVMActivity<DeviceScanViewModel>() {

    var resutl = false

    val mDeviceScanViewModel: DeviceScanViewModel by lazy { createViewModel(DeviceScanViewModel::class.java) }


    lateinit var map: TreeMap<String, ExtendedBluetoothDevice>

    val mBikeDeviceConnectViewModel: BikeDeviceConnectViewModel by lazy {
        createViewModel(
            BikeDeviceConnectViewModel::class.java
        )
    }


    var mDataList = mutableListOf<ExtendedBluetoothDevice>()
    lateinit var mBikeDeviceListAdapter: BikeDeviceListAdapter

    override fun getLayoutResId(): Int {
        return R.layout.activity_bike_device_scan
    }

    override fun initView() {
    }


    override fun initData() {
        resutl = intent.getBooleanExtra("resutl", false)
        BikeConfig.isBikeScanPage = true;
        //toolbar.setBackgroundColor(getResources().getColor(R.color.common_bind_title));
        filter = IntentFilter()
        filter!!.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        map = TreeMap<String, ExtendedBluetoothDevice>()
        initSportDetailRec()
        initPermission()
        mBikeDeviceConnectViewModel.setCallBack(this)
    }

    var isFirstComming = true


    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state =
                    intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)
                Log.e(
                    "BleService",
                    "ACTION_STATE_CHANGED" + state + BluetoothAdapter.STATE_ON
                )
                if (state == BluetoothAdapter.STATE_ON) {
                    startScan()
                } else if (state == BluetoothAdapter.STATE_OFF) {
                    //需要停止动画然后清除数据
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            if (AppUtil.isOpenBle()) {
                unregisterReceiver(broadcastReceiver)
            }
        } catch (e: Exception) {
        }
    }

    var filter: IntentFilter? = null

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (LocationUtils.isLocationEnabled()) {
                registerReceiver(broadcastReceiver, filter)
            } else {
            }
        } else {
            registerReceiver(broadcastReceiver, filter)

        }
    }


    override fun onResume() {
        super.onResume()
        //android 23以上才判断有没有开定位服务

        //android 23以上才判断有没有开定位服务
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isFirstComming) {
                if (!LocationUtils.isLocationEnabled()) {
                    openLocationDialog()
                    isFirstComming = false
                }
            } else {
                if (!LocationUtils.isLocationEnabled()) {
                    finish()
                }
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
                        startScan()
                        // toast("success")
                    } else {
                        ToastUtil.showTextToast(
                            BaseApp.sApplicaton,
                            this@BikeDeviceScanActivity.getString(R.string.no_required_permission)
                        )
                    }
                }
            })
    }


    private fun startScan() {
        if (AppUtil.isOpenBle()) {
            mDeviceScanViewModel.startLeScan()
        } else {
            //ToastUtils.showToast(context, UIUtils.getString(R.string.bluetooth_is_not_enabled));
            openBlueDialog()
        }
    }

    fun openLocationDialog() {


        var yesOrNoDialog = YesOrNoDialog(
            this,
            "",
            this.resources.getString(R.string.app_loaction_server),
            "",
            resources.getString(R.string.app_loaction_on)
        )
        yesOrNoDialog.show()
        yesOrNoDialog.setBtnOnclick(object : OnButtonListener {
            override fun onCancleOnclick() {
            }

            override fun onSureOnclick() {
                LocationUtils.openGpsSettings()
            }
        })

    }


    fun openBlueDialog() {

        var yesOrNoDialog =
            YesOrNoDialog(
                this,
                "",
                this.resources.getString(R.string.app_open_blue_tips),
                "",
                this.resources.getString(R.string.app_open_blue)
            );
        yesOrNoDialog.setBtnOnclick(object : OnButtonListener {
            override fun onCancleOnclick() {
            }

            override fun onSureOnclick() {
                val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                bluetoothAdapter?.enable()
            }
        })
        yesOrNoDialog.show()

    }

    fun initSportDetailRec() {
        recycle_device_list.layoutManager = LinearLayoutManager(this)
        mBikeDeviceListAdapter =
            BikeDeviceListAdapter(mDataList)
        recycle_device_list.adapter = mBikeDeviceListAdapter

        mBikeDeviceListAdapter.setOnItemClickListener(OnItemClickListener { adapter, view, position ->

            mDeviceScanViewModel.stopLeScan()
            showConnetDialog()
            mBikeDeviceConnectViewModel.conectBikeDevice(mDataList.get(position).device)

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
        tv_bike_conn.setOnClickListener {

            mBikeDeviceConnectViewModel.disconectDevice()
        }
    }

    override fun startObserver() {


        mBikeDeviceConnectViewModel.mDeviceConnStateTips.observe(this, Observer {


        })
        // TimeUtils.string2Date()

        mBikeDeviceConnectViewModel.mDeviceConnState.observe(this, Observer {

            runOnUiThread {
                if (it == BikeConfig.BIKE_CONN_SUCCESS) {
                    tv_bike_conn.text = this.getString(R.string.device_state_success)
                    hideConnectDialog()
                    if (resutl) {
                        val intent = Intent()
                        //把返回数据存入Intent
                        //把返回数据存入Intent
                        //设置返回数据
                        //设置返回数据
                        this@BikeDeviceScanActivity.setResult(RESULT_OK, intent)
                    }
                    finish()

                } else if (it == BikeConfig.BIKE_CONN_DISCONN) {
                    tv_bike_conn.text = this.getString(R.string.device_state_fail)

                } else if (it == BikeConfig.BIKE_CONN_CONNECTING) {
                    tv_bike_conn.text = this.getString(R.string.device_state_connecting)
                }
            }
        })
        mDeviceScanViewModel.mDeviceLiveData.observe(this, Observer {
            LogUtil.e("startLeScan6")
            it.forEach {
                if (!TextUtils.isEmpty(it.name)) {
                    if (it.name.contains("S003") || it.name.contains("S005")) {
                        map.put(it.name, it)
                    }
                }

            }
            mDataList.clear()
            map.forEach {
                mDataList.add(it.value)
            }
            mBikeDeviceListAdapter.notifyDataSetChanged()

            /* for (i in it.indices) {
                 mDataList.add(it.get(i))
             }
             mBikeDeviceListAdapter.notifyDataSetChanged()*/
        })
    }


    var connecetHr: ConnectDeviceDialog? = null


    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                //连接超时
                1001 -> {
                    hideConnectDialog()
                    ToastUtil.showTextToast(
                        this@BikeDeviceScanActivity,
                        this@BikeDeviceScanActivity.getString(R.string.device_state_fail)
                    )

                }
            }
        }
    }

    fun hideConnectDialog() {
        handler.removeMessages(1001)
        if (connecetHr != null && connecetHr!!.isShowing) {
            connecetHr!!.dismiss()
            connecetHr = null
            mBikeDeviceConnectViewModel.clearDevice()
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

}