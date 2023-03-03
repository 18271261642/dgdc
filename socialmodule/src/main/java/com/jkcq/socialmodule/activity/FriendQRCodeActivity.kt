package com.jkcq.socialmodule.activity

import android.view.View
import com.jkcq.base.app.CacheManager
import com.jkcq.base.base.BaseActivity
import com.jkcq.base.base.BaseTitleActivity
import com.jkcq.base.net.bean.UserInfo
import com.jkcq.socialmodule.FriendConstant
import com.jkcq.socialmodule.QRCodeManager
import com.jkcq.socialmodule.R
import com.jkcq.util.LoadImageUtil
import com.jkcq.util.SiseUtil
import kotlinx.android.synthetic.main.activity_friend_q_r_code.*

class FriendQRCodeActivity : BaseTitleActivity() {
    override fun getLayoutResId(): Int {
        return R.layout.activity_friend_q_r_code
    }

    override fun initView() {
        //        DecodeHintType.valueOf(DecodeHintType.PURE_BARCODE);
        iv_qrcode.setOnClickListener(View.OnClickListener {
            //                writeQRCode();
//            ScanQRCode();
//                startActivity(new Intent(FriendQRCodeActivity.this,FriendScanActivity.class));
        })
        //  StatusBarCompat.setStatusBarColor(getWindow(), getResources().getColor(R.color.transparent), true);
    }

    override fun initHeander() {
        super.initHeander()
        setTitleText(R.string.QRCode)
    }

    override fun ivRight() {
    }

    private var mUserInfo: UserInfo? = null
    override fun initData() {
        mUserInfo = CacheManager.mUserInfo
        //        mUserInfo = CommonUserAcacheUtil.getUserInfo(TokenUtil.getInstance().getPeopleIdStr(this));
        if (mUserInfo != null) {
            tv_nickname!!.text = mUserInfo!!.nickName
            LoadImageUtil.getInstance().load(
                this@FriendQRCodeActivity,
                mUserInfo!!.headUrl,
                iv_head,
                R.mipmap.friend_icon_default_photo
            )
        }
        writeQRCode()
    }

    private fun writeQRCode() {
        val qrString = FriendConstant.QR_HEAD + mUserInfo?.qrString
        val width = SiseUtil.dip2px(196f)
        iv_qrcode!!.setImageBitmap(
            QRCodeManager.createQRCodeBitmap(
                qrString,
                width,
                width
            )
        )

    }
}