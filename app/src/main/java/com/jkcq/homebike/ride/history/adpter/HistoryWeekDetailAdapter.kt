package com.jkcq.homebike.ride.history.adpter

import android.content.Intent
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jkcq.base.app.Preference
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.login.WebHitoryViewActivity
import com.jkcq.homebike.login.WebViewActivity
import com.jkcq.homebike.ride.history.bean.SportDetialBean
import com.jkcq.util.SiseUtil


/**
 *  Created by BeyondWorlds
 *  on 2020/6/30
 */
class HistoryWeekDetailAdapter(data: MutableList<SportDetialBean>) :
    BaseQuickAdapter<SportDetialBean, BaseViewHolder>(
        R.layout.item_rope_week_history_detail,
        data
    ) {
    init {

        addChildClickViewIds(R.id.layout_head)
    }

    var mUserId: String by Preference(Preference.USER_ID, "")

    private fun updateRecyclerView(recyclerView: RecyclerView) {


    }

    lateinit var historyDayDetailAdapter: BikeSportDetailAdapter

    private fun setItemRecyclerView(
        recyclerView: RecyclerView,
        mExerciseInfos: MutableList<SportDetialBean>
    ) {
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        historyDayDetailAdapter = BikeSportDetailAdapter(mExerciseInfos)
        historyDayDetailAdapter.setOnItemClickListener { v, note, position ->

            var intent = Intent(context, WebHitoryViewActivity::class.java)
            intent.putExtra(
                "lighturl",
                BikeConfig.detailUrlBean.light + "?exerciseId=" + mExerciseInfos.get(position).sportId + "&userId=" + mUserId
            )
            intent.putExtra(
                "darkurl",
                BikeConfig.detailUrlBean.dark + "?exerciseId=" + mExerciseInfos.get(position).sportId + "&userId=" + mUserId
            )
            intent.putExtra(
                "title",
                context.getString(R.string.sport_detail_title)
            )
            context.startActivity(intent)
            /* var intent = Intent(context, WebViewActivity::class.java)
             intent.putExtra(
                 "url",
                 BikeConfig.detailUrlBean.light + "?exerciseId=" + mExerciseInfos.get(position).sportId + "&userId=" + mUserId
             )
             intent.putExtra("title",context.getString(R.string.sport_detail_title))
             context.startActivity(intent)*/


            /*  var intent = Intent(context, WebViewActivity::class.java)
              intent.putExtra("title", "detail");*/
            /* intent.putExtra(
                 "urldark",
                 BikeConfig.detailUrlBean.dark+ "?userId=" + mUserId + "&ropeId=" + mExerciseInfos.get(position).ropeSportDetailId + "&language=" + AppLanguageUtil.getCurrentLanguageStr()
             )
             intent.putExtra(
                 "urlLigh",
                 AppConfiguration.ropedetailLighturl + "?userId=" + TokenUtil.getInstance()
                     .getPeopleIdInt(BaseApp.instance) + "&ropeId=" + mExerciseInfos.get(position).ropeSportDetailId + "&language=" + AppLanguageUtil.getCurrentLanguageStr()
             )*/
            // intent.putExtra("url", AppConfiguration.ropedetailurl + "?userId=" + TokenUtil.getInstance().getPeopleIdInt(BaseApp.instance) + "&ropeId=" + mExerciseInfos.get(position).ropeSportDetailId)
            // context.startActivity(intent)
        }
        recyclerView.adapter = historyDayDetailAdapter
    }

    override fun convert(holder: BaseViewHolder, item: SportDetialBean) {
        var rec = holder.getView<RecyclerView>(R.id.rec_day)
        val strdis = SiseUtil.disUnitCoversion(item.sportDis.toString(), BikeConfig.userCurrentUtil)
        val strCal = SiseUtil.calCoversion(item.sportCal.toString())

        BikeConfig.disUnit(holder.getView<TextView>(R.id.tv_dis_unit), context)


        holder.setText(R.id.tv_count, "" + item.sportCount)
        holder.setText(R.id.tv_cal, strCal)
        holder.setText(R.id.tv_dis, strdis)
        holder.setText(R.id.tv_date, item.date)
        holder.setText(R.id.tv_count, item.times)


        if (item.isOpen) {
            holder.setVisible(R.id.rec_day, true)
            if (item!!.list != null) {
                item!!.currentShowList.addAll(item!!.list)
                holder.setImageResource(R.id.iv_arrow_right, R.drawable.icon_rope_up)
                setItemRecyclerView(rec, item!!.currentShowList)
            } else {
                holder.setImageResource(R.id.iv_arrow_right, R.drawable.icon_rope_down)
            }
        } else {
            if (item!!.currentShowList != null) {
                item!!.currentShowList.clear()
                setItemRecyclerView(rec, item!!.currentShowList)
            }
            holder.setVisible(R.id.rec_day, false)
            holder.setImageResource(R.id.iv_arrow_right, R.drawable.icon_rope_down)
        }
        /*holder.setText(R.id.tv_date, item.date);
        val view = holder.getView<View>(R.id.view_current_bg)
        if (TextUtils.isEmpty(item.date)) {
            holder.setVisible(R.id.view_bg, false)
            holder.setVisible(R.id.view_current_bg, false)
        } else {
            val linearParams: RelativeLayout.LayoutParams =
                    view.getLayoutParams() as RelativeLayout.LayoutParams
            val size = DisplayUtils.dip2px(context, 130f)
            linearParams.height = (size * (item.currentValue * 1.0f / item.maxVlaue)).toInt()
            if (linearParams.height >= size) {
                linearParams.height = size;
            }
            view.setLayoutParams(linearParams)
            Log.e(
                    "linearParams.height",
                    " " + linearParams.height + "--currentValue=" + item.currentValue + "---maxVlaue=" + item.maxVlaue + "size=" + size
            )
            holder.setVisible(R.id.view_bg, true)
            holder.setVisible(R.id.view_current_bg, true)
        }

        if (item.isSelect) {
            holder.setVisible(R.id.tv_select, true)
        } else {
            holder.setVisible(R.id.tv_select, false)
        }*/
        /*  holder.setText(R.id.tv_time,""+item.createTime)
          when (item.followStatus) {
              1 -> holder.setText(R.id.tv_follow, R.string.friend_each_follow)
              0, 2 -> holder.setText(R.id.tv_follow, R.string.friend_to_follow)
              3 -> holder.setText(R.id.tv_follow, R.string.friend_already_follow)
          }*/
    }
}