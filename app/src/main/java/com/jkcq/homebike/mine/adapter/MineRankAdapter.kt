package com.jkcq.homebike.mine.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jkcq.homebike.R
import com.jkcq.homebike.net.bean.RankInfo
import com.jkcq.util.LogUtil

/**
 *  Created by BeyondWorlds
 *  on 2020/8/1
 */
class MineRankAdapter(data: MutableList<RankInfo>) :
    BaseQuickAdapter<RankInfo, BaseViewHolder>(R.layout.item_mine_rank,data) {
    init {

    }

    override fun convert(holder: BaseViewHolder, item: RankInfo) {
        LogUtil.e(holder.layoutPosition.toString())
    }
}