package com.jkcq.homebike.login.bean

import android.net.Uri
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jkcq.homebike.R

/**
 *  Created by BeyondWorlds
 *  on 2020/6/30
 */
class SharePicAdapter(data: MutableList<ShareBean>) :
    BaseQuickAdapter<ShareBean, BaseViewHolder>(R.layout.item_share_item, data) {
    init {
        addChildClickViewIds(R.id.layout_view)
    }

    override fun convert(holder: BaseViewHolder, item: ShareBean) {


        if (item.isDark) {
            holder.setBackgroundResource(
                R.id.layout_nestedScrollview,
                R.drawable.shape_btn_green_grey_20_bg
            )
        } else {
            holder.setBackgroundResource(
                R.id.layout_nestedScrollview,
                R.drawable.shape_layout_gray_bg_20
            )
        }
        try {
            if (item.isSelect) {
                holder.setVisible(R.id.iv_select, true)
            } else {
                holder.setVisible(R.id.iv_select, false)
            }
            // LoadImageUtil.getInstance().load(context, item.url, holder.getView(R.id.iv_pic))
            //


            var iv = holder.getView<ImageView>(R.id.iv_pic)
            iv.setImageURI(Uri.fromFile(item.shareFile))

        } catch (e: Exception) {

        }


        // imageview.setImageURI(Uri.fromFile(new File(这里填路径字符串)));
        //LoadImageUtil.getInstance().load(context, item!!.shareFile, iv, 20)
        //Glide.with(context).load(item.shareFile).into(iv);
        /* holder.setText(R.id.tv_nickname,item.fromNickName)
         holder.setText(R.id.tv_time,""+item.createTime)
         when (item.followStatus) {
             1 -> holder.setText(R.id.tv_follow, R.string.friend_each_follow)
             0, 2 -> holder.setText(R.id.tv_follow, R.string.friend_to_follow)
             3 -> holder.setText(R.id.tv_follow, R.string.friend_already_follow)
         }
         holder.setText(R.id.tv_time, TimeUtil.getDynmicTime(item.getCreateTime(), ""))
         LoadImageUtil.getInstance().load(context, item.fromHeadUrlTiny, holder.getView(R.id.iv_head_photo))*/
    }
}