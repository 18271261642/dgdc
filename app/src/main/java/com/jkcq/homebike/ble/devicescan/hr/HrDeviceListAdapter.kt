package com.jkcq.homebike.ble.devicescan.hr

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jkcq.homebike.R
import com.jkcq.homebike.ble.scanner.ExtendedBluetoothDevice


/**
 *  Created by BeyondWorlds
 *  on 2020/6/30
 */
class HrDeviceListAdapter(data: MutableList<ExtendedBluetoothDevice>) :
    BaseQuickAdapter<ExtendedBluetoothDevice, BaseViewHolder>(R.layout.item_hr_scan_device, data) {
    init {

    }

    override fun convert(holder: BaseViewHolder, item: ExtendedBluetoothDevice) {
        holder.setText(R.id.tv_device_type_name, item.name)
        holder.setText(R.id.tv_device_name, item.device.address)
        /* holder.setText(R.id.tv_time,""+item.createTime)
         when (item.followStatus) {
             1 -> holder.setText(R.id.tv_follow, R.string.friend_each_follow)
             0, 2 -> holder.setText(R.id.tv_follow, R.string.friend_to_follow)
             3 -> holder.setText(R.id.tv_follow, R.string.friend_already_follow)
         }*/
    }
}