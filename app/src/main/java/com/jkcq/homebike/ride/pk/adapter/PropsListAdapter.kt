package com.jkcq.homebike.ride.pk.adapter

import android.animation.Animator
import android.animation.ObjectAnimator
import android.widget.ImageView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jkcq.base.view.LoadingAnimatorView
import com.jkcq.homebike.R
import com.jkcq.homebike.ride.pk.bean.PropesBean
import com.jkcq.util.LoadImageUtil
import com.jkcq.viewlibrary.CircleImageView


/**
 *  Created by BeyondWorlds
 *  on 2020/6/30
 */
class PropsListAdapter(data: MutableList<PropesBean>) :
    BaseQuickAdapter<PropesBean, BaseViewHolder>(R.layout.item_props, data) {




    init {

    }


    override fun convert(holder: BaseViewHolder, item: PropesBean) {


        LoadImageUtil.getInstance()
            .load(
                context,
                item.thumbnailUrl,
                holder.getView<ImageView>(R.id.iv_gift),
                R.mipmap.friend_icon_default_photo
            )
        var view = holder.getView<LoadingAnimatorView>(R.id.circe)
        if (item.isLengque) {
            view.addObserver()
            /*Log.e("progresss", "progresss----" + BikeConfig.progress + "BikeConfig.progress.toInt()=" + BikeConfig.progress.toInt() + "(5000 - BikeConfig.progress.toInt() * 50).toLong()=" + ((5000 - BikeConfig.progress.toInt() * 50).toLong()))

            val animator: ObjectAnimator = ObjectAnimator.ofFloat(
                    view, "progress", BikeConfig.progress.toInt() * 1.0f, 100f
            )
//            Logger.myLog("progreesVaule  == " + progreesVaule + "goalValue == " + goalValue);
            //            Logger.myLog("progreesVaule  == " + progreesVaule + "goalValue == " + goalValue);
            var tempDu = (5000 - BikeConfig.progress * 50).toLong()
            //  tempDu = tempDu - tempDu % 1000
            animator.duration = tempDu
            animator.start()
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator?) {
                    item.isLengque = false
                    view.progress = 0f

                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }
            })*/
        } else {
            view.deletObserVer()
        }
    }
}