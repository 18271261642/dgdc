package com.jkcq.homebike.ride.pk.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.websocket.bean.PKType
import com.jkcq.homebike.R
import com.jkcq.homebike.ride.pk.bean.PKResultBean
import com.jkcq.homebike.ride.pk.bean.enumbean.PkBestState
import com.jkcq.util.LoadImageUtil
import com.jkcq.util.SiseUtil


/**
 *  Created by BeyondWorlds
 *  on 2020/6/30
 */
class PKResultListAdapter(data: MutableList<PKResultBean>) :
    BaseQuickAdapter<PKResultBean, BaseViewHolder>(R.layout.item_pk_result_item, data) {




    init {


    }


    override fun convert(holder: BaseViewHolder, item: PKResultBean) {


        holder.setText(R.id.tv_ranking, "" + item.ranking)

        holder.setText(R.id.tv_nickname, item.nickName)
        if (item.isFinishFlag) {
            holder.setTextColor(
                R.id.tv_state,
                context.resources.getColor(R.color.common_view_color)
            )
            when (item.recordBreakingStatus) {
                PkBestState.NO_RECORD.value -> {
                    holder.setText(R.id.tv_state, context.getString(R.string.Pk_complety))
                }
                PkBestState.PERSONAL_RECORDS.value -> {
                    holder.setText(R.id.tv_state, context.getString(R.string.PK_person))
                }
                PkBestState.WORLD_RECORD.value -> {
                    holder.setText(R.id.tv_state, context.getString(R.string.Pk_world))
                }
            }

        } else {
            holder.setTextColor(R.id.tv_state, context.resources.getColor(R.color.hint_color))
            holder.setText(R.id.tv_state, context.getString(R.string.Pk_unComplety))
        }
        val min = SiseUtil.timeMillCoversionMin(item.durationMillis)
        holder.setText(R.id.tv_create, min)
        LoadImageUtil.getInstance()
            .load(
                context,
                item.avatar,
                holder.getView<ImageView>(R.id.iv_head),
                R.drawable.icon_pk_waitting
            )

    }
}