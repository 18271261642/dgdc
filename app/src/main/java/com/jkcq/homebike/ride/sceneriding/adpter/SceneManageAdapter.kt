package com.jkcq.homebike.ride.sceneriding.adpter

import android.util.Log
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jkcq.homebike.R
import com.jkcq.homebike.ride.sceneriding.bean.SceneVideoManaBean
import com.jkcq.homebike.util.Arith
import com.jkcq.util.AppUtil
import com.jkcq.util.DateUtil
import com.jkcq.util.LoadImageUtil


/**
 *  Created by BeyondWorlds
 *  on 2020/6/30
 */
class SceneManageAdapter(data: MutableList<SceneVideoManaBean>, isC: Boolean) :
    BaseQuickAdapter<SceneVideoManaBean, BaseViewHolder>(R.layout.item_sence_manager_item, data) {


    var isCourse = false

    init {
        isCourse = isC
    }


    override fun convert(holder: BaseViewHolder, item: SceneVideoManaBean) {
        // var videourl = item.videoUrl
        // var currentSencFileName = videourl.substring(videourl.lastIndexOf("/"))


        //查询该视频是否已经下载过

        Log.e("SceneManageAdapter", "" + item + "--" + item.sceneName)

        if (item.sceneName == null || item.sceneName.size == 0) {

            if (isCourse) {
                holder.setText(
                    R.id.tv_sence_name,
                    context.getString(R.string.course_no_pair_scene)
                )
            } else {
                holder.setText(
                    R.id.tv_sence_name,
                    context.getString(R.string.scene_no_pair_scene)
                )
            }
            LoadImageUtil.getInstance()
                .loadCirc(
                    context,
                    item.path,
                    holder.getView<ImageView>(R.id.iv_scene_pic),
                    DateUtil.dip2px(20f)
                )

        } else {
            LoadImageUtil.getInstance()
                .loadCirc(
                    context,
                    item.picUrl,
                    holder.getView<ImageView>(R.id.iv_scene_pic),
                    DateUtil.dip2px(20f)
                )
            if (AppUtil.isCN()) {
                holder.setText(
                    R.id.tv_sence_name,
                    item.sceneName.get(0)
                )
            } else {
                holder.setText(
                    R.id.tv_sence_name,
                    item.sceneEnName.get(0)
                )
            }


        }
        holder.setText(
            R.id.tv_size,
            String.format(
                context.getString(R.string.scene_size),
                "" + Arith.div(item.lenth.toDouble(), 1024 * 1024.toDouble(), 2)
            )
        )

    }
}