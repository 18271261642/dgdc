package com.jkcq.homebike.ride.history.adpter

import android.text.TextUtils
import android.view.View
import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jkcq.homebike.R
import com.jkcq.homebike.ride.history.bean.BarInfo
import com.jkcq.util.DateUtil
import com.jkcq.util.SiseUtil


/**
 *  Created by BeyondWorlds
 *  on 2020/6/30
 */
class BarBlackAdapter(data: MutableList<BarInfo>, count: Int) :
    BaseQuickAdapter<BarInfo, BaseViewHolder>(R.layout.item_history_sport_black_bar_detail, data) {
    var listcount = 0

    init {
        listcount = count

    }


    override fun convert(holder: BaseViewHolder, item: BarInfo) {
        holder.setText(R.id.tv_date, item.date);
        val view = holder.getView<View>(R.id.view_current_bg)
        val view_width = holder.getView<View>(R.id.view_width)
        if (TextUtils.isEmpty(item.date)) {
            holder.setVisible(R.id.view_bg, false)
            holder.setVisible(R.id.view_current_bg, false)
        } else {
            val lineartopParams: RelativeLayout.LayoutParams =
                view_width.getLayoutParams() as RelativeLayout.LayoutParams
            var toalWith = DateUtil.getScreenWidth(context);
            lineartopParams.width = (toalWith / listcount).toInt();
            view_width.setLayoutParams(lineartopParams)


            val linearParams: RelativeLayout.LayoutParams =
                view.getLayoutParams() as RelativeLayout.LayoutParams
            val size = DateUtil.dip2px(102f)
            linearParams.height = (size * (item.currentValue * 1.0f / item.maxVlaue)).toInt()



            if (linearParams.height >= size) {
                linearParams.height = size;
            } else if (item.currentValue > 0 && linearParams.height < DateUtil.dip2px(8f)) {
                linearParams.height = DateUtil.dip2px(8f);
            }
            view.setLayoutParams(linearParams)
            holder.setVisible(R.id.view_bg, true)
            holder.setVisible(R.id.view_current_bg, true)
        }
        /*  holder.setText(R.id.tv_time,""+item.createTime)
          when (item.followStatus) {
              1 -> holder.setText(R.id.tv_follow, R.string.friend_each_follow)
              0, 2 -> holder.setText(R.id.tv_follow, R.string.friend_to_follow)
              3 -> holder.setText(R.id.tv_follow, R.string.friend_already_follow)
          }*/
    }

    /* override fun convert(holder: BaseViewHolder, item: BarInfo) {
         holder.setText(R.id.tv_date, item.date);
         val view = holder.getView<View>(R.id.view_current_bg)
         if (TextUtils.isEmpty(item.date)) {
             holder.setVisible(R.id.view_bg, false)
             holder.setVisible(R.id.view_current_bg, false)
         } else {
             val linearParams: RelativeLayout.LayoutParams =
                 view.getLayoutParams() as RelativeLayout.LayoutParams
             val size = SiseUtil.dip2px(130f)
             linearParams.height = (size * (item.currentValue * 1.0f / item.maxVlaue)).toInt()
             if (linearParams.height >= size) {
                 linearParams.height = size;
             }
             view.setLayoutParams(linearParams)
             Log.e(
                 "linearParams.height",
                 " " + linearParams.height + "--currentValue=" + item.currentValue + "---maxVlaue=" + item.maxVlaue + "size=" + size
             )
             holder.setVisible(R.id.view_bg, true)
             holder.setVisible(R.id.view_current_bg, true)
         }

         if (item.isSelect) {
             holder.setVisible(R.id.tv_cal, true)
             holder.setVisible(R.id.tv_cal, true)
             val cal = SiseUtil.calCoversion(item.currentValue.toString())
             holder.setText(R.id.tv_cal, cal + context.getString(R.string.sport_cal_unitl))
         } else {
             holder.setVisible(R.id.tv_cal, false)
             holder.setVisible(R.id.tv_cal, false)
         }
         *//*  holder.setText(R.id.tv_time,""+item.createTime)
          when (item.followStatus) {
              1 -> holder.setText(R.id.tv_follow, R.string.friend_each_follow)
              0, 2 -> holder.setText(R.id.tv_follow, R.string.friend_to_follow)
              3 -> holder.setText(R.id.tv_follow, R.string.friend_already_follow)
          }*//*
    }*/
}