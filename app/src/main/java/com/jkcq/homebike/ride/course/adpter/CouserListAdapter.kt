package com.jkcq.homebike.ride.course.adpter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jkcq.homebike.R
import com.jkcq.homebike.db.SceneBean
import com.jkcq.homebike.util.FileUtil
import com.jkcq.util.DateUtil
import com.jkcq.util.LoadImageUtil
import java.io.File


/**
 *  Created by BeyondWorlds
 *  on 2020/6/30
 */
class CouserListAdapter(data: MutableList<SceneBean>) :
    BaseQuickAdapter<SceneBean, BaseViewHolder>(R.layout.item_sence_lists_item, data) {




    init {


    }


    override fun convert(holder: BaseViewHolder, item: SceneBean) {

        var videourl = item.videoUrl
        var currentSencFileName = videourl.substring(videourl.lastIndexOf("/"))

        val file = File(
            FileUtil.getVideoCorseDir().toString() + currentSencFileName
        )

        if (file.exists()) {
            holder.setVisible(R.id.iv_not_down, false)
        } else {
            holder.setVisible(R.id.iv_not_down, true)
        }
        //查询该视频是否已经下载过
        holder.setText(
            R.id.tv_sence_name,
            String.format(context.getString(R.string.scene_name), item.name)
        )

        val hms: DateUtil.HMS = DateUtil.getHMSFromMillis(item.videoDuration.toLong() * 1000)
        holder.setText(
            R.id.tv_dis,
            String.format(
                context.getString(R.string.course_time),
                String.format(
                    "%02d:%02d:%02d",
                    hms.getHour(),
                    hms.getMinute(),
                    hms.getSecond()
                )
            )
        )

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


    }
}