package com.jkcq.homebike.ride.history;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.jkcq.base.base.BaseActivity;
import com.jkcq.homebike.R;
import com.jkcq.homebike.ble.devicescan.hr.DeviceScanViewModel;
import com.jkcq.homebike.ble.scanner.ExtendedBluetoothDevice;
import com.jkcq.viewlibrary.TitleView;

import java.util.ArrayList;
import java.util.List;

class TestActivity extends BaseActivity {
    TitleView titleView;
    ArrayList<ExtendedBluetoothDevice> list;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public int getLayoutResId() {
        return R.layout.activity_exercise_record;
    }

    @Override
    public void initView() {

        DeviceScanViewModel mDeviceScanViewModel = null;

        mDeviceScanViewModel.getMDeviceLiveData().observe(this, new Observer<List<ExtendedBluetoothDevice>>() {
            @Override
            public void onChanged(List<ExtendedBluetoothDevice> extendedBluetoothDevices) {
                for (int i = 0; i < extendedBluetoothDevices.size(); i++) {
                    list.add(extendedBluetoothDevices.get(i));
                }
            }
        });


    }

    @Override
    public void initData() {

    }
}
