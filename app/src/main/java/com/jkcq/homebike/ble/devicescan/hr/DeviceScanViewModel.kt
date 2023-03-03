package com.jkcq.homebike.ble.devicescan.hr

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.jkcq.base.app.BaseApp
import com.jkcq.base.base.BaseViewModel
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.ble.scanner.ExtendedBluetoothDevice
import com.jkcq.util.AppUtil
import com.jkcq.util.LogUtil
import com.jkcq.util.ktx.ToastUtil
import no.nordicsemi.android.ble.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.ble.scanner.ScanCallback
import no.nordicsemi.android.ble.scanner.ScanResult
import no.nordicsemi.android.ble.scanner.ScanSettings
import java.util.*

/**
 *  Created by BeyondWorlds
 *  on 2020/7/29
 */
class DeviceScanViewModel : BaseViewModel() {


    val mDeviceLiveData: MutableLiveData<List<ExtendedBluetoothDevice>> =
        MutableLiveData<List<ExtendedBluetoothDevice>>()
    val scanDeviceState: MutableLiveData<Int> =
        MutableLiveData<Int>()


    private val listBondedValues =
        ArrayList<ExtendedBluetoothDevice>()
    private val listValues =
        ArrayList<ExtendedBluetoothDevice>()


    @Volatile
    protected var sumSize = 0

    private val SCAN_DURATION: Long = 15000
    var handler = Handler(Looper.getMainLooper())
    var connectingPosition = -1
    var scanning = false


    private val stopScanTask = Runnable {
        scanTimeOut()
        stopLeScan()
    }

    fun scanTimeOut() {
        Log.e("scanTimeOut", "scanTimeOut")
        handler.post {
            scanDeviceState.value = BikeConfig.BIKE_CONN_IS_SCAN_TIMEOUT
        }

    }

    fun stopLeScan() {
        if (!scanning) return
        val scanner: BluetoothLeScannerCompat = BluetoothLeScannerCompat.getScanner()
        scanner.stopScan(scanCallback)
        handler.removeCallbacks(stopScanTask)
        scanning = false
    }


    fun startLeScan() {
        if (!AppUtil.isOpenBle()) {
            ToastUtil.showTextToast(BaseApp.sApplicaton, BaseApp.sApplicaton.getString(R.string.openBle))
            return
        }

        LogUtil.e("startLeScan")
        sumSize = 0
        if (connectingPosition >= 0) return
        if (scanning) {
            // Extend scanning for some time more
            handler.removeCallbacks(stopScanTask)
            handler.postDelayed(stopScanTask, SCAN_DURATION)
            return
        }
        scanDeviceState.value = BikeConfig.BIKE_CONN_IS_SCAN
        LogUtil.e("startLeScan2")
        val scanner: BluetoothLeScannerCompat = BluetoothLeScannerCompat.getScanner()
        val settings: ScanSettings =
            ScanSettings.Builder().setReportDelay(1000).setUseHardwareBatchingIfSupported(false)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()
        scanner.startScan(null, settings, scanCallback)


        LogUtil.e("startLeScan3")
        // Setup timer that will stop scanning
        handler.postDelayed(stopScanTask, SCAN_DURATION)
        scanning = true
    }


    private val scanCallback: ScanCallback =
        object : ScanCallback() {
            override fun onScanResult(
                callbackType: Int,
                result: ScanResult
            ) {
                // do nothing
            }

            override fun onBatchScanResults(results: List<ScanResult>) {
                LogUtil.e("startLeScan4")
                update(results)

                // adapter.update(results)
            }

            override fun onScanFailed(errorCode: Int) {
                // should never be called
            }
        }

    fun update(results: List<ScanResult>) {
        for (result in results) {
            val device = findDevice(result)
            if (device == null) {
                if (!TextUtils.isEmpty(result.device.name)) {
                    listValues.add(ExtendedBluetoothDevice(result))
                }
            } else {
                device.name = if (result.scanRecord != null) result.scanRecord!!
                    .deviceName else null
                device.rssi = result.rssi
            }
        }
        LogUtil.e("startLeScan5")
        handler.post({ mDeviceLiveData.value = listValues })

    }

    private fun findDevice(result: ScanResult): ExtendedBluetoothDevice? {
        for (device in listBondedValues) if (device.matches(result)) return device
        for (device in listValues) if (device.matches(result)) return device
        return null
    }

}