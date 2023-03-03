package com.jkcq.homebike.ride.sceneriding.adpter

import android.util.Log
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.db.SceneBean
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
class SceneRidingListAdapter(data: MutableList<SceneBean>) :
    BaseQuickAdapter<SceneBean, BaseViewHolder>(R.layout.item_sence_lists_item, data) {




    init {

        addChildClickViewIds(R.id.iv_not_down)

    }


    override fun convert(holder: BaseViewHolder, item: SceneBean) {
        var videourl = item.videoUrl
        var currentSencFileName = videourl.substring(videourl.lastIndexOf("/"))

        //查询该视频是否已经下载过
        val file = File(

            FileUtil.getVideoDir().toString() + currentSencFileName
        )

        if (file.exists()) {
            Log.e(
                "covert",
                "file.length() >= item.length.toLong()=" + file.length()
                        + "---------------------" + item.videoSize.toLong()
            )
            if (file.length() >= item.videoSize.toFloat()) {
                holder.setVisible(R.id.iv_not_down, false)
            } else {
                holder.setVisible(R.id.iv_not_down, true)
            }
        } else {
            holder.setVisible(R.id.iv_not_down, true)
        }
        if (AppUtil.isCN()) {
            holder.setText(
                R.id.tv_sence_name,
                String.format(context.getString(R.string.scene_name), item.name)
            )
        } else {
            holder.setText(
                R.id.tv_sence_name,
                String.format(context.getString(R.string.scene_name), item.nameEn)
            )
        }
        if (BikeConfig.userCurrentUtil.equals("0")) {
            holder.setText(
                R.id.tv_dis,
                String.format(
                    context.getString(R.string.scene_dis),
                    SiseUtil.disUnitCoversion(item.length, BikeConfig.userCurrentUtil)
                )
            )
        } else {
            holder.setText(
                R.id.tv_dis,
                String.format(
                    context.getString(R.string.scene_mile),
                    SiseUtil.disUnitCoversion(item.length, BikeConfig.userCurrentUtil)
                )
            )

        }
        holder.setText(
            R.id.tv_complety_count,
            String.format(context.getString(R.string.scene_completely), item.participantNum)
        )
        LoadImageUtil.getInstance()
            .loadCirc(
                context,
                item.imageUrl,
                holder.getView<ImageView>(R.id.iv_scene_pic),
                DateUtil.dip2px(20f)
            )

        var difRes = R.drawable.icon_difficulty_1

        when (item.difficulty) {
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