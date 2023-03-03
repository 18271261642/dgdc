package com.jkcq.util

import android.app.Activity
import android.app.Dialog
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_scene_line_layout.*


/**
 * @author WuJianhua
 */
class ScenAdapterLineNameDialog(
    private val mContext: Activity,
    values: String,
    titles: String,
    tips: String

) :
    Dialog(mContext, R.style.SimpleHUD1) {

    private var clickListener: OnDialogClickListener? = null

    init {
        val view = LayoutInflater.from(mContext).inflate(R.layout.dialog_scene_line_layout, null)
        setContentView(view)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        //设置Dialog大小位置
        val dialogWindow = window
        val lp = dialogWindow!!.attributes
        dialogWindow.setGravity(Gravity.CENTER)

        if (TextUtils.isEmpty(values)) {
            tv_adapter_name.visibility = View.GONE
            tv_title.visibility = View.GONE
            tv_tips1.text = mContext.getText(R.string.scene_line_del)

        } else {
            tv_title.text = tips
            tv_adapter_name.text = values
            tv_tips1.text = titles
        }
        tv_cancel.setOnClickListener {
            if (clickListener != null) {
                clickListener?.dialogClickType(3)
            }
        }
        tv_sure.setOnClickListener {
            if (clickListener != null) {
                clickListener?.dialogClickType(1)
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


    fun setOnDialogClickListener(clickListener: OnDialogClickListener) {
        this.clickListener = clickListener
    }

    fun onClick() {
    }
}
