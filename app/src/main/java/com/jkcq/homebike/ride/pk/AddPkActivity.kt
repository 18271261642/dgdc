package com.jkcq.homebike.ride.pk

import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.Observer
import com.example.websocket.WsManager
import com.jkcq.base.app.BaseApp
import com.jkcq.base.base.BaseTitleVMActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.db.SceneBean
import com.jkcq.homebike.mine.mvvm.viewmodel.UserModel
import com.jkcq.homebike.ride.mvvm.viewmodel.PKModel
import com.jkcq.homebike.ride.sceneriding.SceneRidingListActivity
import com.jkcq.util.AppUtil
import com.jkcq.util.KeyboardUtils
import com.jkcq.util.ktx.ToastUtil
import com.jkcq.viewlibrary.dialog.SelectPopupWindow
import kotlinx.android.synthetic.main.activity_add_pk.*


class AddPkActivity : BaseTitleVMActivity<PKModel>() {


    override fun getLayoutResId(): Int = R.layout.activity_add_pk

    val mPkModel: PKModel by lazy { createViewModel(PKModel::class.java) }

    var mSelectPopupWindow: SelectPopupWindow? = null

    var numberCout = "2"
    var mSceneBean: SceneBean? = null
    var pkname = ""
    var pkPwd = ""
    var starValue = 2;
    var endValue = 6;

    override fun initView() {

        mPkModel.getPkParticipantNum()

        itemview_pk_number.setOnClickListener {
            KeyboardUtils.hideKeyboard(itemview_pk_number)
            mSelectPopupWindow?.popWindowSelect(
                this@AddPkActivity,
                itemview_select_line,
                SelectPopupWindow.PK_NUMBER,
                starValue,
                endValue,
                itemview_pk_number.getRightText(), false
            )
        }
        itemview_select_line.setOnClickListener {
            var intent = Intent(this@AddPkActivity, SceneRidingListActivity::class.java)
            intent.putExtra("from", true);
            startActivityForResult(
                intent,
                1
            )
        }

        btn_start.setOnClickListener {
            pkname = et_subname.text.trim().toString()

            if (TextUtils.isEmpty(pkname)) {
                ToastUtil.showTextToast(
                    BaseApp.sApplicaton, R.string.pk_hide_enter_name
                )
                return@setOnClickListener
            }
            if (mSceneBean == null) {
                ToastUtil.showTextToast(
                    BaseApp.sApplicaton, R.string.pk_hide_line_select
                )
                return@setOnClickListener
            }
//


            mPkModel.createPkRoom(numberCout, pkname, et_pk_pwd.text.toString(), mSceneBean!!.id)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            mSceneBean = data?.getSerializableExtra("result") as SceneBean
            if (AppUtil.isCN()) {
                itemview_select_line.setRightText(mSceneBean!!.name)
            } else {
                itemview_select_line.setRightText(mSceneBean!!.nameEn)
            }

        } catch (e: Exception) {

        }

        //得到新Activity 关闭后返回的数据

    }

    override fun initEvent() {
        super.initEvent()
    }

    override fun initData() {

        setTitleText(this.resources.getString(R.string.pk_create_pk_title))

        mSelectPopupWindow = SelectPopupWindow(object : SelectPopupWindow.OnSelectPopupListener {
            override fun onSelect(type: String, data: String) {
                when (type) {
                    SelectPopupWindow.PK_NUMBER -> {
                        itemview_pk_number.setRightText(data)
                        numberCout = data
                    }

                }
            }

        })
    }

    override fun startObserver() {
        mPkModel.mPkNumberBean.observe(this, Observer {
            starValue = it.minimum
            endValue = it.maximum
        })
        mPkModel.mPkRoomBean.observe(this, Observer {


            var intent = Intent(this@AddPkActivity, PrepareActivity::class.java)
            intent.putExtra("scene", mSceneBean)
            intent.putExtra("pkinfo", it)
            startActivity(intent)

            finish()
        })

    }

    override fun ivRight() {
    }


}