package com.jkcq.homebike.ble.devicescan.hr

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.jkcq.base.base.BaseViewModel
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.ble.battery.BatteryManagerCallbacks
import com.jkcq.util.ktx.ToastUtil
import no.nordicsemi.android.ble.BleManagerCallbacks

/**
 *  Created by BeyondWorlds
 *  on 2020/7/29
 */
class HrDeviceConnectViewModel : BaseViewModel(), BleManagerCallbacks, HRManagerCallbacks,
    BatteryManagerCallbacks {
    lateinit var hrManager: HRManager
    var handler = Handler(Looper.getMainLooper())

    val mDeviceConnState: MutableLiveData<Int> =
        MutableLiveData<Int>()
    val mDeviceHrValue: MutableLiveData<Int> =
        MutableLiveData<Int>()


    fun setCallBack(context: Context) {
        hrManager = HRManager.getInstance(context);
        hrManager.setGattCallbacks(this)
    }


    fun conectHrDevice(device: BluetoothDevice) {
        if (ToastUtil.isFastConn(1000)) {
            return
        }

        if (hrManager.connectionState == BluetoothGatt.STATE_CONNECTING || hrManager.connectionState == BluetoothGatt.STATE_CONNECTED || hrManager.connectionState == BluetoothGatt.STATE_DISCONNECTING) {
            return
        }
        hrManager.clearQuery()
        hrManager.connect(device)
            .useAutoConnect(true).retry(3, 100).enqueue()
    }

    fun clearDevice() {
        hrManager.clearQuery()
    }

    fun disconectDevice() {
        if (hrManager.isConnected) {
            hrManager.disconnect().enqueue()
        }
    }

    override fun onDeviceDisconnecting(device: BluetoothDevice) {
        // Log.e("DeviceConnectViewModel", "onDeviceDisconnecting" + device.name)
    }

    override fun onDeviceDisconnected(device: BluetoothDevice) {
        BikeConfig.HRConnState = BikeConfig.BIKE_CONN_DISCONN
        mDeviceConnState.value = BikeConfig.BIKE_CONN_DISCONN


        //Log.e("DeviceConnectViewModel", "onDeviceDisconnected" + device.name)
    }

    override fun onBatteryLevelChanged(device: BluetoothDevice, batteryLevel: Int) {
        Log.e(
            "DeviceConnectViewModel",
            "onBatteryLevelChanged" + device.name + "batteryLevel" + batteryLevel
        )
    }

    override fun onDeviceConnected(device: BluetoothDevice) {
        BikeConfig.HRConnState = BikeConfig.BIKE_CONN_SUCCESS
        mDeviceConnState.value = BikeConfig.BIKE_CONN_SUCCESS
        Log.e("DeviceConnectViewModel", "onDeviceConnected" + device.name)
    }

    override fun onDeviceNotSupported(device: BluetoothDevice) {
        Log.e("DeviceConnectViewModel", "onDeviceNotSupported" + device.name)
    }

    override fun onBondingFailed(device: BluetoothDevice) {
        Log.e("DeviceConnectViewModel", "onBondingFailed" + device.name)
    }

    override fun onServicesDiscovered(device: BluetoothDevice, optionalServicesFound: Boolean) {
        Log.e("DeviceConnectViewModel", "onServicesDiscovered" + device.name)
    }

    override fun onBondingRequired(device: BluetoothDevice) {
        Log.e("DeviceConnectViewModel", "onBondingRequired" + device.name)

    }

    override fun onLinkLossOccurred(device: BluetoothDevice) {
        BikeConfig.HRConnState = BikeConfig.BIKE_CONN_DISCONN
        mDeviceConnState.value = BikeConfig.BIKE_CONN_DISCONN
        Log.e("DeviceConnectViewModel", "onLinkLossOccurred" + device.name)
    }

    override fun onBonded(device: BluetoothDevice) {
        Log.e("DeviceConnectViewModel", "onBonded" + device.name)
    }

    override fun onDeviceReady(device: BluetoothDevice) {
        BikeConfig.HRConnState = BikeConfig.BIKE_CONN_SUCCESS
        mDeviceConnState.value = BikeConfig.BIKE_CONN_SUCCESS
        Log.e("DeviceConnectViewModel", "onDeviceReady" + device.name)
    }

    override fun onRealData(device: BluetoothDevice, power: Int, speed: Int) {
        // TODO("Not yet implemented")
    }

    override fun onHeartRateMeasurementReceived(
        device: BluetoothDevice,
        heartRate: Int,
        contactDetected: Boolean?,
        energyExpanded: Int?,
        rrIntervals: MutableList<Int>?
    ) {
        handler.post {
            mDeviceHrValue.value = heartRate
            /* RealDataObservable.getInstance()
                 .sendHrValue(heartRate);*/
            Log.e(
                "DeviceConnectViewModel2",
                "onHeartRateMeasurementReceived" + device.name + "heartRate" + heartRate
            )
        }


        Log.e(
            "DeviceConnectViewModel",
            "onHeartRateMeasurementReceived" + device.name + "heartRate" + heartRate
        )
    }

    override fun onBodySensorLocationReceived(device: BluetoothDevice, sensorLocation: Int) {
        Log.e("DeviceConnectViewModel", "onBodySensorLocationReceived" + device.name)
    }

    override fun onError(device: BluetoothDevice, message: String, errorCode: Int) {
        Log.e(
            "DeviceConnectViewModel",
            "onError" + device.name + ",message=" + message + "errorCode=" + errorCode
        )
    }

    override fun onDeviceConnecting(device: BluetoothDevice) {
        BikeConfig.HRConnState = BikeConfig.BIKE_CONN_CONNECTING
        mDeviceConnState.value = BikeConfig.BIKE_CONN_CONNECTING
        Log.e("DeviceConnectViewModel", "onDeviceConnecting" + device.name)
    }


}