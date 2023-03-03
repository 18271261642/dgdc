/*
 * Copyright (c) 2015, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jkcq.homebike.ble.devicescan.bike;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.jkcq.homebike.bike.BikeConfig;
import com.jkcq.homebike.bike.arithmetic.Utils;
import com.jkcq.homebike.ble.battery.BatteryManager;
import com.jkcq.homebike.ble.devicescan.bike.receivecallback.BikeRealDataCallback;

import java.util.Arrays;
import java.util.UUID;

import no.nordicsemi.android.ble.callback.DataReceivedCallback;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.data.MutableData;

/**
 * HRSManager class performs BluetoothGatt operations for connection, service discovery,
 * enabling notification and reading characteristics.
 * All operations required to connect to device with BLE Heart Rate Service and reading
 * heart rate values are performed here.
 */
public class BikeBleManager extends BatteryManager<BikeBleManagerCallbacks> {


    //W516
    public static final UUID BIKE_SERVICE_UUID = UUID.fromString("7658fd00-878a-4350-a93e-da553e719ed0");
    public static final UUID BIKE_SEND_WRITE_UUID = UUID.fromString("7658fd01-878a-4350-a93e-da553e719ed0");//Write
    public static final UUID BIKE_RESPONCE_NOTIFY_UUID = UUID.fromString("7658fd02-878a-4350-a93e-da553e719ed0");//notify
    public static final UUID BIKE_REALTIMEDATA_NOTIFY_UUID = UUID.fromString("7658fd04-878a-4350-a93e-da553e719ed0");//notify


    // public final static UUID DEVICEINFORMATION_SERVICE = UUID.fromString("0000180A-0000-1000-8000-00805f9b34fb");
    // public final static UUID FIRMWAREREVISION_CHARACTERISTIC = UUID.fromString("00002A26-0000-1000-8000-00805f9b34fb");

   /* static final UUID HR_SERVICE_UUID = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb");
    private static final UUID BODY_SENSOR_LOCATION_CHARACTERISTIC_UUID = UUID.fromString("00002A38-0000-1000-8000-00805f9b34fb");
    private static final UUID HEART_RATE_MEASUREMENT_CHARACTERISTIC_UUID = UUID.fromString("00002A37-0000-1000-8000-00805f9b34fb");*/


    private BluetoothGattCharacteristic sendWriteCharacteristic, responseNotifyCharacteristic, realDataNotifyCharacteristic, versionCharateristic;

    //private BluetoothGattCharacteristic heartRateCharacteristic, bodySensorLocationCharacteristic;

    private static BikeBleManager managerInstance = null;

    /**
     * Singleton implementation of HRSManager class.
     */
    public static synchronized BikeBleManager getInstance(final Context context) {
        if (managerInstance == null) {
            managerInstance = new BikeBleManager(context);
        }
        return managerInstance;
    }

    private BikeBleManager(final Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected BatteryManagerGattCallback getGattCallback() {
        return new HeartRateManagerCallback();
    }

    /**
     * BluetoothGatt callbacks for connection/disconnection, service discovery,
     * receiving notification, etc.
     */
    private final class HeartRateManagerCallback extends BatteryManagerGattCallback {

        @Override
        protected void initialize() {
            super.initialize();

            Log.e("BikeBleManager", "initialize");

            setNotificationCallback(realDataNotifyCharacteristic)
                    .with(new BikeRealDataCallback() {
                        @Override
                        public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {

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

                            byte[] values = data.getValue();
                            if (values.length >= 7) {
                                int speed = (Utils.byte2Int(values[3]) << 8) + Utils.byte2Int(values[4]);
                                int power = (Utils.byte2Int(values[5]) << 8) + Utils.byte2Int(values[6]);
                                //实时数据
                                if (speed == 0 && power == 0) {

                                } else {
                                    try {
                                        BikeConfig.speed = speed * 10;
                                        BikeConfig.power = power;
                                        //mCallbacks.onRealData(device, power, speed);
                                    } catch (Exception e) {

                                    }

                                    // RealDataObservable.getInstance().setRealData(speed, power);
                                }
                                Log.e("onDataReceived", "realDataNotifyCharacteristic" + data.toString() + "speed=" + speed + "power=" + power + ",Utils.byte2Int(values[6])=" + Utils.byte2Int(values[6]) + ",values" + Arrays.toString(values));
                            }
                        }

                    });
            enableNotifications(realDataNotifyCharacteristic).enqueue();

            setNotificationCallback(responseNotifyCharacteristic)
                    .with(new BikeRealDataCallback() {
                        @Override
                        public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
                            Log.e("onDataReceived", "responseNotifyCharacteristic" + data.toString());
                        }

                    });
            enableNotifications(responseNotifyCharacteristic).enqueue();


        }

        @Override
        protected boolean isRequiredServiceSupported(@NonNull final BluetoothGatt gatt) {
            final BluetoothGattService service = gatt.getService(BIKE_SERVICE_UUID);
            if (service != null) {
                sendWriteCharacteristic = service.getCharacteristic(BIKE_SEND_WRITE_UUID);
                responseNotifyCharacteristic = service.getCharacteristic(BIKE_RESPONCE_NOTIFY_UUID);
                realDataNotifyCharacteristic = service.getCharacteristic(BIKE_REALTIMEDATA_NOTIFY_UUID);
            }
            return true;
        }

        @Override
        protected boolean isOptionalServiceSupported(@NonNull final BluetoothGatt gatt) {
            super.isOptionalServiceSupported(gatt);
            /*final BluetoothGattService service = gatt.getService(DEVICEINFORMATION_SERVICE);
            if (service != null) {
                versionCharateristic = service.getCharacteristic(FIRMWAREREVISION_CHARACTERISTIC);
            }*/
            //  return versionCharateristic != null;
            /*final BluetoothGattService service = gatt.getService(BIKE_SERVICE_UUID);
            if (service != null) {
                bodySensorLocationCharacteristic = service.getCharacteristic(BODY_SENSOR_LOCATION_CHARACTERISTIC_UUID);
            }
            return bodySensorLocationCharacteristic != null;*/
            return true;
        }

        @Override
        protected void onDeviceDisconnected() {
            super.onDeviceDisconnected();
            BikeBleManager.this.cancelQueue();
            sendWriteCharacteristic = null;
            responseNotifyCharacteristic = null;
            realDataNotifyCharacteristic = null;


            // bodySensorLocationCharacteristic = null;
            //  heartRateCharacteristic = null;
        }
    }

    public void cleareAllQueue() {
        //refreshDeviceCache();
        cancelQueue();
    }

    public void sendWritData(int number) {
        if (sendWriteCharacteristic != null) {
            writeCharacteristic(sendWriteCharacteristic, createQuickData(number)).enqueue();
        }
        cancelQueue();

    }

    public void readVersionCharacteristic() {
        if (isConnected()) {
            readCharacteristic(versionCharateristic)
                    .with(new DataReceivedCallback() {
                        @Override
                        public void onDataReceived(@NonNull BluetoothDevice device, @NonNull Data data) {
                            Log.e("onDataReceived", "readVersionCharacteristic" + data.toString());
                            Log.e("onDataReceived", "***FirmwareVersion111***" + new String(data.getValue()));
                            BikeConfig.currentVersion = new String(data.getValue());
                        }
                    })
                    .fail((device, status) -> log(Log.WARN, "Battery Level characteristic not found"))
                    .enqueue();
        }
    }


    /**
     * 0x00,//控制命令回复
     * 0x01,//上传采集卡数据
     * 0x02,//下发阻力设置命令
     * 0x03,//下发灯带颜色命令
     * 0x04//下发设置单车的最大阻力等级
     *
     * @param number
     * @return
     */


    private static Data createQuickData(int number) {

        byte[] cmd = new byte[5];
        cmd[0] = (byte) 0x55;
        cmd[1] = (byte) 0xaa;
        cmd[2] = (byte) 0x02;
        cmd[3] = (byte) number;
        cmd[4] = (byte) 0xfe;

        final MutableData data = new MutableData(new byte[5]);
        data.setValue(cmd);
        return data;
    }
}
