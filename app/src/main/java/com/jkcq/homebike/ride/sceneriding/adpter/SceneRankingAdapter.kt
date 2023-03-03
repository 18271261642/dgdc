package com.jkcq.homebike.ride.sceneriding.adpter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jkcq.homebike.R
import com.jkcq.homebike.ble.bean.RankingBean
import com.jkcq.util.LoadImageUtil
import com.jkcq.util.SiseUtil


/**
 *  Created by BeyondWorlds
 *  on 2020/6/30
 */
class SceneRankingAdapter(data: MutableList<RankingBean>) :
    BaseQuickAdapter<RankingBean, BaseViewHolder>(R.layout.item_scene_ranking, data) {




    init {

        addChildClickViewIds(R.id.layout_ranking)

    }


    override fun convert(holder: BaseViewHolder, item: RankingBean) {

        LoadImageUtil.getInstance()
            .load(
                context,
                item.headUrl,
                holder.getView<ImageView>(R.id.iv_head),
                R.mipmap.friend_icon_default_photo
            )
        holder.setText(R.id.tv_name, item.nickName)
        holder.setText(R.id.tv_ranking, "" + item.rankingNo)
        holder.setText(R.id.tv_praise, item.praiseNums)
        val strCal = SiseUtil.calCoversion(item.totalCalorie.toString())
        holder.setText(R.id.tv_cal, strCal + context.resources.getString(R.string.sport_cal_unitl))
        if (item.isWhetherPraise) {
            holder.setImageResource(R.id.iv_praise, R.mipmap.icon_like_press)
        } else {
            holder.setImageResource(R.id.iv_praise, R.mipmap.icon_like_nor)
        }

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