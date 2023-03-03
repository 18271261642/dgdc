package com.jkcq.homebike.ride.pk.adapter

import android.text.TextUtils
import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.websocket.bean.PkUsers
import com.jkcq.base.app.Preference
import com.jkcq.homebike.R
import com.jkcq.util.LoadImageUtil
import com.jkcq.util.ThreadPoolUtils


/**
 *  Created by BeyondWorlds
 *  on 2020/6/30
 */
class PrepareListAdapter(data: MutableList<PkUsers>) :
    BaseQuickAdapter<PkUsers, BaseViewHolder>(R.layout.item_pk_prepare_item, data) {

    var mUserId: String by Preference(Preference.USER_ID, "")


    init {


    }


    override fun convert(holder: BaseViewHolder, item: PkUsers) {


        holder.setText(R.id.tv_nickname, item.nickName)
        if (item.isCreatorFlag) {
            holder.setText(R.id.tv_create, context.getString(R.string.Pk_creater))
        } else {
            holder.setText(R.id.tv_create, "")
        }

        // Log.e("item.avatar", "item.avatar=" + item.avatar)

        if (!TextUtils.isEmpty(item.avatar)) {
            LoadImageUtil.getInstance()
                .load(
                    context,
                    item.avatar,
                    holder.getView(R.id.iv_head),
                    R.mipmap.friend_icon_default_photo
                )

        } else {
            LoadImageUtil.getInstance()
                .load(
                    context,
                    item.avatar,
                    holder.getView(R.id.iv_head),
                    R.drawable.icon_pk_waitting
                )

        }


    }
}