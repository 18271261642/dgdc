package com.jkcq.homebike.ride.history.adpter

import android.text.TextUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.ride.history.bean.SportDetialBean
import com.jkcq.util.AppUtil
import com.jkcq.util.DateUtil
import com.jkcq.util.LoadImageUtil
import com.jkcq.util.SiseUtil
import kotlinx.android.synthetic.main.view_bike_sport_cal_time_pow.*


/**
 *  Created by BeyondWorlds
 *  on 2020/6/30
 */
class BikeSportDetailAdapter(data: MutableList<SportDetialBean>) :
    BaseQuickAdapter<SportDetialBean, BaseViewHolder>(R.layout.item_bike_sport_detail, data) {




    init {


    }


    override fun convert(holder: BaseViewHolder, item: SportDetialBean) {


        if (TextUtils.isEmpty(item.sportName)) {
            holder.setText(R.id.tv_sport_name, context.getString(R.string.bike_type_free_bike))

            if (AppUtil.isCN()) {
                LoadImageUtil.getInstance()
                    .loadCirc(
                        context,
                        R.drawable.icon_history_free_ch,
                        holder.getView(R.id.iv_sport),
                        DateUtil.dip2px(10f)
                    )
            } else {
                LoadImageUtil.getInstance()
                    .loadCirc(
                        context,
                        R.drawable.icon_history_free_en,
                        holder.getView(R.id.iv_sport),
                        DateUtil.dip2px(10f)
                    )
            }

        } else {
            if (AppUtil.isCN()) {
                holder.setText(R.id.tv_sport_name, item.sportName)
            }else{
                holder.setText(R.id.tv_sport_name, item.sportNameEn)
            }

            LoadImageUtil.getInstance()
                .loadCirc(
                    context,
                    item.sportUrl,
                    holder.getView(R.id.iv_sport),
                    DateUtil.dip2px(10f)
                )
        }

        holder.setText(R.id.tv_sport_date, item.date)


        var dis= item.sportDis+ BikeConfig.disUnit(context)
        holder.setText(R.id.tv_sport_cal, item.sportCal+context.getString(R.string.device_unitl_kcal))
        holder.setText(R.id.tv_sport_dis, dis)
        holder.setText(R.id.tv_sport_time, item.sportTime)
        /*   when (item.followStatus) {
             1 -> holder.setText(R.id.tv_follow, R.string.friend_each_follow)
             0, 2 -> holder.setText(R.id.tv_follow, R.string.friend_to_follow)
             3 -> holder.setText(R.id.tv_follow, R.string.friend_already_follow)
         }*/
    }
}