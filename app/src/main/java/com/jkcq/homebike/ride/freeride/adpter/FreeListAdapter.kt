package com.jkcq.homebike.ride.freeride.adpter

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
class FreeListAdapter(data: MutableList<SceneBean>) :
    BaseQuickAdapter<SceneBean, BaseViewHolder>(R.layout.item_sence_lists_item, data) {




    init {

        addChildClickViewIds(R.id.iv_not_down)

    }


    override fun convert(holder: BaseViewHolder, item: SceneBean) {
        var videourl = item.videoUrl
        var currentSencFileName = videourl.substring(videourl.lastIndexOf("/"))

        holder.setVisible(R.id.tv_dis, false)
        holder.setVisible(R.id.tv_complety_count, false)
        holder.setVisible(R.id.iv_difficulty, false)

        //查询该视频是否已经下载过
        val file = File(

            FileUtil.getFreeDir().toString() + currentSencFileName
        )

        if (file.exists()) {
            holder.setVisible(R.id.iv_not_down, false)
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
        LoadImageUtil.getInstance()
            .loadCirc(
                context,
                item.imageUrl,
                holder.getView<ImageView>(R.id.iv_scene_pic),
                DateUtil.dip2px(20f)
            )


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