package no.nordicsemi.android.ble.scanner;

import android.bluetooth.BluetoothDevice;


public interface ScanResultsCallBack {
    void onBatchScanResults(BluetoothDevice device, int rssi, byte[] scanRecord);

    void onScanFailed(int errorCode);


    void onScanFinished();
}
