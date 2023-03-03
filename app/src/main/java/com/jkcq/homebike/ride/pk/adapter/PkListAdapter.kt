package com.jkcq.homebike.ride.pk.adapter

import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.db.SceneBean
import com.jkcq.homebike.ride.pk.bean.PKListBean
import com.jkcq.homebike.util.FileUtil
import com.jkcq.util.AppUtil
import com.jkcq.util.DateUtil
import com.jkcq.util.LoadImageUtil
import com.jkcq.util.SiseUtil
import java.io.File


/**
 *  Created by BeyondWorlds
 *  on 2020/6/30
 */
class PkListAdapter(data: MutableList<PKListBean>) :
    BaseQuickAdapter<PKListBean, BaseViewHolder>(R.layout.item_pk_lists_item, data) {




    init {

       // addChildClickViewIds(R.id.iv_not_down)

    }


    override fun convert(holder: BaseViewHolder, item: PKListBean) {

        Log.e("convert", "" + item)

        var videourl = item.scenario.videoUrl
        var currentSencFileName = videourl.substring(videourl.lastIndexOf("/"))
        //查询该视频是否已经下载过
        val file = File(
            FileUtil.getVideoDir().toString() + currentSencFileName
        )
        if (file.exists()) {
            Log.e(
                "covert",
                "file.length() >= item.length.toLong()=" + file.length()
                        + "---------------------" + item.scenario.videoSize.toLong()
            )
            if (file.length() >= item.scenario.videoSize.toFloat()) {
                holder.setVisible(R.id.iv_not_down, false)
            } else {
                holder.setVisible(R.id.iv_not_down, true)
            }
        } else {
            holder.setVisible(R.id.iv_not_down, true)
        }
        holder.setText(
            R.id.tv_pk_name,
            item.cyclingPkDetail.pkName
        )
        if (AppUtil.isCN()) {
            holder.setText(
                R.id.tv_sence_name,
                String.format(context.getString(R.string.scene_name), item.scenario.name)
            )
        } else {
            holder.setText(
                R.id.tv_sence_name,
                String.format(context.getString(R.string.scene_name), item.scenario.nameEn)
            )
        }
        //PK状态，0：未开始，1：进行中，2：已结束
        if (item.cyclingPkDetail.pkStatus == 0) {
            holder.setText(R.id.tv_pk_state, context.getString(R.string.Pk_not_start))
        } else {
            holder.setText(R.id.tv_pk_state, context.getString(R.string.Pk_started))
        }
        if (BikeConfig.userCurrentUtil.equals("0")) {
            holder.setText(
                R.id.tv_dis,
                String.format(
                    context.getString(R.string.scene_dis),
                    SiseUtil.disUnitCoversion(item.scenario.length, BikeConfig.userCurrentUtil)
                )
            )
        } else {
            holder.setText(
                R.id.tv_dis,
                String.format(
                    context.getString(R.string.scene_mile),
                    SiseUtil.disUnitCoversion(item.scenario.length, BikeConfig.userCurrentUtil)
                )
            )

        }
        holder.setText(
            R.id.tv_complety_count,
            String.format(
                context.getString(R.string.Pk_attent_number),
                item.cyclingPkDetail.joinNum, item.cyclingPkDetail.participantNum
            )
        )

        if (item.cyclingPkDetail.isHasPassword) {
            holder.setVisible(R.id.iv_pk_pwd, true)
        } else {
            holder.setVisible(R.id.iv_pk_pwd, false)
        }
        /*  holder.setText(
              R.id.tv_complety_count,
              String.format(context.getString(R.string.scene_completely), item.participantNum)
          )*/
        LoadImageUtil.getInstance()
            .loadCirc(
                context,
                item.scenario.imageUrl,
                holder.getView<ImageView>(R.id.iv_scene_pic),
                DateUtil.dip2px(20f)
            )

        var difRes = R.drawable.icon_difficulty_1

        when (item.scenario.difficulty) {
            "1" -> {
                difRes = R.drawable.icon_difficulty_1
            }
            "2" -> {
                difRes = R.drawable.icon_difficulty_2
            }
            "3" -> {
                difRes = R.drawable.icon_difficulty_3
            }
            "4" -> {
                difRes = R.drawable.icon_difficulty_4
            }
            "5" -> {
                difRes = R.drawable.icon_difficulty_5
            }
            else -> {
                difRes = R.drawable.icon_difficulty_5
            }
        }
        holder.setImageResource(R.id.iv_difficulty, difRes)


        /*if (TextUtils.isEmpty(item.sportName)) {
            holder.setText(R.id.tv_sport_name, context.getString(R.string.bike_type_free_bike))
        } else {
            holder.setText(R.id.tv_sport_name, item.sportName)
        }

        holder.setText(R.id.tv_sport_date, item.date)
        holder.setText(R.id.tv_sport_cal, item.sportCal)
        holder.setText(R.id.tv_sport_dis, item.sportDis)
        holder.setText(R.id.tv_sport_time, item.sportTime)*/
    }
}