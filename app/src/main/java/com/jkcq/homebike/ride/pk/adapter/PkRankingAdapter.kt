package com.jkcq.homebike.ride.pk.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.websocket.PKUserCacheManager
import com.example.websocket.bean.PkdataList
import com.jkcq.base.app.BaseApp
import com.jkcq.base.app.Preference
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.ride.pk.bean.enumbean.PkState
import com.jkcq.util.LoadImageUtil
import com.jkcq.util.SiseUtil
import com.jkcq.util.UserBitmapManager


/**
 *  Created by BeyondWorlds
 *  on 2020/6/30
 */
class PkRankingAdapter(data: MutableList<PkdataList>) :
    BaseQuickAdapter<PkdataList, BaseViewHolder>(R.layout.item_pk_ranking, data) {


    var mUserId: String by Preference(Preference.USER_ID, "")

    init {


    }


    override fun convert(holder: BaseViewHolder, item: PkdataList) {


        var nikename = ""
        var headUrl = ""
        var randNo = "" + item.rankNo



        if (mUserId.equals(item.userId)) {
            holder.setVisible(R.id.iv_gift, false)
        } else {
            holder.setVisible(R.id.iv_gift, true)
        }




        if (item.pkStatus == PkState.END.value) {

        } else {

        }

        if (PKUserCacheManager.pkuserHeadUrl.containsKey(item.userId)) {
            headUrl = PKUserCacheManager.pkuserHeadUrl.get(item.userId).toString()
        }
        if (PKUserCacheManager.pkuserName.containsKey(item.userId)) {
            nikename = PKUserCacheManager.pkuserName.get(item.userId).toString()
        }
        if (!UserBitmapManager.bitmapHashMap.containsKey(item.userId)) {
            if (mUserId.equals(item.userId)) {
                LoadImageUtil.getInstance()
                    .loadDownload(
                        false,
                        BaseApp.sApplicaton,
                        headUrl,
                        item.userId,
                        R.drawable.icon_pk_mine, 5, R.mipmap.friend_icon_default_photo
                    )
            } else {
                LoadImageUtil.getInstance()
                    .loadDownload(
                        false,
                        BaseApp.sApplicaton,
                        headUrl,
                        item.userId,
                        R.drawable.icon_pk_other, 5, R.mipmap.friend_icon_default_photo
                    )
            }
        }




        LoadImageUtil.getInstance()
            .load(
                context,
                headUrl,
                holder.getView<ImageView>(R.id.iv_head),
                R.mipmap.friend_icon_default_photo
            )
        holder.setText(R.id.tv_name, nikename)
        holder.setText(R.id.tv_ranking, randNo)
        val dis = SiseUtil.disUnitCoversion(
            item.distance.toString(),
            BikeConfig.userCurrentUtil
        )

        if (item.pkStatus.equals(PkState.END)) {
            holder.setVisible(R.id.tv_sub, true)
            holder.setTextColor(R.id.tv_sub, context.resources.getColor(R.color.white))
            holder.setText(
                R.id.tv_sub,
                context.resources.getString(R.string.Pk_complety)
            )

        } else {
            if (item.subvalue != 0) {
                holder.setVisible(R.id.tv_sub, true)
                if (item.subvalue > 0) {
                    holder.setTextColor(
                        R.id.tv_sub,
                        context.resources.getColor(R.color.sub_color)
                    )
                } else {
                    holder.setTextColor(
                        R.id.tv_sub,
                        context.resources.getColor(R.color.common_button_bg)
                    )
                }
                var absValue = Math.abs(item.subvalue)
                var subDis = SiseUtil.disUnitCoversion(
                    absValue.toString(),
                    BikeConfig.userCurrentUtil
                )
                if (item.subvalue < 0) {
                    subDis = subDis
                } else {
                    subDis = "-" + subDis
                }
                if (BikeConfig.METRIC_UNITS == BikeConfig.userCurrentUtil) {
                    holder.setText(
                        R.id.tv_sub,
                        subDis + context.resources.getString(R.string.sport_dis_unitl_km)
                    )
                } else {
                    holder.setText(
                        R.id.tv_sub,
                        subDis + context.resources.getString(R.string.sport_dis_unitl_mile)
                    )
                }
            } else {
                holder.setVisible(R.id.tv_sub, false)
            }
        }
        if (BikeConfig.METRIC_UNITS == BikeConfig.userCurrentUtil) {
            holder.setText(
                R.id.tv_dis,
                dis + context.resources.getString(R.string.sport_dis_unitl_km)
            )
        } else {
            holder.setText(
                R.id.tv_dis,
                dis + context.resources.getString(R.string.sport_dis_unitl_mile)
            )
        }

    }
}