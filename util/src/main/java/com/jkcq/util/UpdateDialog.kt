package com.jkcq.util

import android.app.Activity
import android.app.Dialog
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jkcq.util.NetUtil
import com.jkcq.util.ktx.ToastUtil
import kotlinx.android.synthetic.main.dialog_update_layout.*


/**
 * @author WuJianhua
 */
class UpdateDialog(private val mContext: Activity, type: Int, values: String) :
    Dialog(mContext, R.style.SimpleHUD1) {

    private var clickListener: OnDialogClickListener? = null

    init {
        val view = LayoutInflater.from(mContext).inflate(R.layout.dialog_update_layout, null)
        setContentView(view)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        //设置Dialog大小位置
        val dialogWindow = window
        val lp = dialogWindow!!.attributes
        dialogWindow.setGravity(Gravity.CENTER)
        tv_sure.setTag("1")
        tv_cancel.setOnClickListener {
            if (clickListener != null) {
                clickListener?.dialogClickType(3)
            }
        }
        iv_success.setOnClickListener {
            if (clickListener != null) {
                clickListener?.dialogClickType(4)
            }
        }
        iv_scene_option.setOnClickListener {
            if (clickListener != null) {
                if (isCommplety) {
                    clickListener?.dialogClickType(4)
                } else {
                    clickListener?.dialogClickType(2)
                }
            }
        }
        tv_title.text = values
        tv_sure.setOnClickListener {


            if (!NetUtil.isNetworkConnected(context)) {
                ToastUtil.showTextToast(context, context.getString(R.string.net_error))

            } else {
                if (tv_sure.getTag().equals("1") && !NetUtil.checkWifiState(mContext)) {
                    if (clickListener != null) {
                        tv_title.text = mContext.resources.getString(R.string.no_wify_tips)
                        tv_sure.setTag("2")
                    }
                } else {
                    if (clickListener != null) {
                        tv_title.text = mContext.resources.getString(R.string.downloading)
                        layout_prgress.visibility = View.VISIBLE
                        iv_scene_option.visibility = View.VISIBLE
                        layout_sucess.visibility = View.GONE
                        layout_button.visibility = View.GONE
                        clickListener?.dialogClickType(1)
                    }
                }
            }
        }
//        int desity = (int) ScreenUtils.getScreenDensity();
//        lp.width= LinearLayout.LayoutParams.MATCH_PARENT;
//        lp.height=LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
        lp.width = (AppUpdateUtils.getScreenWidth(mContext) * 0.8).toInt()
        dialogWindow.attributes = lp
        /* if (AppUpdateUtils.isUpdateMandatory) {
             cancel_upload.visibility = View.GONE
         }
         cancel_upload.setOnClickListener { clickListener?.dialogClickType(1) }*/
    }


    var isCommplety = false
    fun updateSuccess() {
        layout_sucess.visibility = View.VISIBLE
        layout_prgress.visibility = View.GONE
        iv_scene_option.visibility = View.VISIBLE
        isCommplety = true
        iv_scene_option.setImageResource(R.mipmap.icon_scene_download_close)
        tv_title.text = mContext.resources.getString(R.string.download_success)
    }

    fun updateProgress(progress: Int) {
        tv_title.text = mContext.resources.getString(R.string.downloading)

        var currentPro = progress
        if (currentPro < 6) {
            currentPro =6;
        }

        Log.e("updateProgress", "progress=" + currentPro)
//        Log.e("updateProgress", ""+progress)
        progress_bar.setProgress(currentPro)
    }


    fun setTvPackgeSize(currentvalue: String, totalValue: String) {
        tv_curent_size.text = currentvalue
        tv_packge_total.text = totalValue
        //   val tips = mContext.resources.getString(R.string.package_size, "" + size)
//        tv_packge.setText(mContext.resources.getString(R.string.package_size,""+size))
        // tv_packge.setText("更新包大小:" + size + " M")
    }

    fun setOnDialogClickListener(clickListener: OnDialogClickListener) {
        this.clickListener = clickListener
    }

    fun onClick() {
    }
}
