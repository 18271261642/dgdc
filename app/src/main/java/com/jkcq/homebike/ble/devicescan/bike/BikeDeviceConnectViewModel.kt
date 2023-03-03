package com.jkcq.homebike.ble.devicescan.bike

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.jkcq.base.app.BaseApp
import com.jkcq.base.app.Preference
import com.jkcq.base.base.BaseViewModel
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.bike.arithmetic.BikeUtil
import com.jkcq.homebike.bike.bean.RealDataBean
import com.jkcq.homebike.ble.battery.BatteryManagerCallbacks
import com.jkcq.homebike.ble.devicescan.bike.receivecallback.BikeRealDataCallback
import com.jkcq.homebike.ride.freeride.ReconDeviceUtil
import com.jkcq.homebike.ride.util.DeviceDataCache
import com.jkcq.util.ktx.ToastUtil
import no.nordicsemi.android.ble.BleManagerCallbacks
import no.nordicsemi.android.ble.data.Data

/**
 *  Created by BeyondWorlds
 *  on 2020/7/29
 */
class BikeDeviceConnectViewModel : BaseViewModel(), BleManagerCallbacks,
    BatteryManagerCallbacks, BikeRealDataCallback {
    var mBikeName: String by Preference(Preference.BIKENAME, "")
    var mBikeMac: String by Preference(Preference.BIKEMAC, "")


    lateinit var bikeManager: BikeBleManager;


    val mDeviceConnState: MutableLiveData<Int> =
        MutableLiveData<Int>()
    val mRealData: MutableLiveData<RealDataBean> =
        MutableLiveData<RealDataBean>()
    val mDeviceConnStateTips: MutableLiveData<String> =
        MutableLiveData<String>()


    fun setCallBack(context: Context) {
        bikeManager = BikeBleManager.getInstance(context);
        bikeManager.setGattCallbacks(this)
    }


    fun sendQuitData(number: Int) {

        if (bikeManager.isConnected) {
            bikeManager.sendWritData(number)
        }
    }


    fun conectBikeDevice(device: BluetoothDevice) {


        if (ToastUtil.isFastConn(1000)) {
            return
        }
        Log.e("conectBikeDevice", "conectBikeDevice---" + "bikeManager.connectionState =" + bikeManager.connectionState)
        if (bikeManager.connectionState == BluetoothGatt.STATE_CONNECTING || bikeManager.connectionState == BluetoothGatt.STATE_CONNECTED || bikeManager.connectionState == BluetoothGatt.STATE_DISCONNECTING) {
            return
        }
        Log.e("conectBikeDevice", "conectBikeDevice")

        ReconDeviceUtil.getInstance().init(object : ReconDeviceUtil.OnCountTimeChangeLister {
            override fun onCountTimeChange() {
                // TODO("Not yet implemented")
                if (BikeConfig.device != null) {
                    conectBikeDevice(BikeConfig.device)
                }
            }
        });
        BikeConfig.device = device
        BikeUtil.currentBikeName = device.name
        //  bikeManager.disconnect().enqueue()
        //  bikeManager.cleareAllQueue()
        //   if(bikeManager.connectionState
        bikeManager.connect(device)
            .useAutoConnect(true).retry(3, 100).enqueue()
    }


    fun unBindeDevice() {
        if (bikeManager.isConnected) {
            disconectDevice()
        }
        bikeManager.cleareAllQueue()
        BikeConfig.device = null
        BikeUtil.currentBikeName = ""
        mBikeName = ""
        mBikeMac = ""

        ToastUtil.showTextToast(
            BaseApp.sApplicaton, R.string.device_unbind_success
        )
    }

    fun clearDevice() {
        bikeManager.cleareAllQueue()
    }

    fun disconectDevice() {
        if (bikeManager != null && bikeManager.isConnected) {
            bikeManager.disconnect().enqueue()
        }

        //  bikeManager.disconnect()
    }

    override fun onDeviceDisconnecting(device: BluetoothDevice) {
        // Log.e("DeviceConnectViewModel", "onDeviceDisconnecting" + device.name)
        mDeviceConnStateTips.value = "DeviceDisconnecting"
        BikeConfig.BikeConnState = BikeConfig.BIKE_CONN_DISCONNECTING
    }

    override fun onDeviceDisconnected(device: BluetoothDevice) {
        Log.e("DeviceConnectViewModel", "onDeviceDisconnected" + device.name)
        ReconDeviceUtil.getInstance().startReconDevice()
        mDeviceConnStateTips.value = "DeviceDisconnected"
        BikeConfig.BikeConnState = BikeConfig.BIKE_CONN_DISCONN
        mDeviceConnState.value = BikeConfig.BIKE_CONN_DISCONN
    }

    override fun onBatteryLevelChanged(device: BluetoothDevice, batteryLevel: Int) {
        Log.e(
            "BikeDeviceConnect",
            "onBatteryLevelChanged" + device.name + "batteryLevel" + batteryLevel
        )
    }

    override fun onDeviceConnected(device: BluetoothDevice) {

        try {
            Log.e("BikeDeviceConnect", "onDeviceConnected" + device.name)
            ReconDeviceUtil.getInstance().endReconDevice()
            mDeviceConnStateTips.value = "DeviceConnected"
            mBikeName = device.name
            mBikeMac = device.address
            BikeConfig.BikeConnState = BikeConfig.BIKE_CONN_SUCCESS
            DeviceDataCache.saveDevcie(BaseApp.sApplicaton, device.name, device)
        } catch (e: Exception) {

        }

        //
    }

    override fun onDeviceNotSupported(device: BluetoothDevice) {
        Log.e("BikeDeviceConnect", "onDeviceNotSupported" + device.name)
    }

    override fun onBondingFailed(device: BluetoothDevice) {
        Log.e("BikeDeviceConnect", "onBondingFailed" + device.name)
    }

    override fun onServicesDiscovered(device: BluetoothDevice, optionalServicesFound: Boolean) {
        Log.e("BikeDeviceConnect", "onServicesDiscovered" + device.name)
    }

    override fun onBondingRequired(device: BluetoothDevice) {
        Log.e("BikeDeviceConnect", "onBondingRequired" + device.name)

    }

    override fun onLinkLossOccurred(device: BluetoothDevice) {
        //
        mDeviceConnStateTips.value = "DeviceDisconnected"
        BikeConfig.BikeConnState = BikeConfig.BIKE_CONN_DISCONN
        mDeviceConnState.value = BikeConfig.BIKE_CONN_DISCONN
        Log.e("BikeDeviceConnect", "onLinkLossOccurred" + device.name)
    }

    override fun onBonded(device: BluetoothDevice) {
        Log.e("BikeDeviceConnect", "onBonded" + device.name)
    }

    override fun onDeviceReady(device: BluetoothDevice) {
        Log.e("BikeDeviceConnect", "onDeviceReady" + device.name)
        mDeviceConnStateTips.value = "DeviceReady"
        mDeviceConnState.value = BikeConfig.BIKE_CONN_SUCCESS
        if (bikeManager.isConnected) {
            //  bikeManager.readVersionCharacteristic()
        }
    }

    override fun onRealData(device: BluetoothDevice, power: Int, speed: Int) {
        // TODO("Not yet implemented")
        var realdata = RealDataBean()
        realdata.power = power
        realdata.speed = speed
        mRealData.value = realdata
    }


    override fun onError(device: BluetoothDevice, message: String, errorCode: Int) {
        Log.e(
            "BikeDeviceConnect",
            "onError" + device.name + ",message=" + message + "errorCode=" + errorCode
        )
    }

    override fun onDeviceConnecting(device: BluetoothDevice) {
        Log.e("DeviceConnectViewModel", "onDeviceConnecting" + device.name)


        mDeviceConnStateTips.value = "DeviceConnecting"
        mDeviceConnState.value = BikeConfig.BIKE_CONN_CONNECTING
    }

    override fun onDataReceived(device: BluetoothDevice, data: Data) {

        Log.e("onDataReceived", "onDataReceived------------------------")

        /**
         * 数据格式：55 AA 01 00FE 0002 ED
         * 格式解释：
         * 55AA广播实时转速和功率标识，0～1位2bytes
         * 01消息类型,上传采集卡数据
         * 00FE转速数据，2bytes
         * 00FD功率数据，2bytes
         * ED数据结束标识
         */
        //转速数据


        // TODO("Not yet implemented")
    }


}