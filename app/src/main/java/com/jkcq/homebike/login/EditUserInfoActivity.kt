package com.jkcq.homebike.login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import com.example.utillibrary.PermissionUtil
import com.jkcq.base.app.BaseApp
import com.jkcq.base.app.CacheManager
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.ali.AliYunModel
import com.jkcq.homebike.ali.BitmapUtils
import com.jkcq.homebike.ali.PhotoChoosePopUtil
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.mine.mvvm.viewmodel.UserModel
import com.jkcq.homebike.util.FileUtil
import com.jkcq.util.*
import com.jkcq.util.ktx.ToastUtil
import com.jkcq.viewlibrary.dialog.SelectPopupWindow
import kotlinx.android.synthetic.main.activity_edit_user_info.*
import java.io.File
import java.util.*

class EditUserInfoActivity : BaseVMActivity<UserModel>(), View.OnLayoutChangeListener,
    OnFocusChangeListener {

    val mUserModel: UserModel by lazy { createViewModel(UserModel::class.java) }
    val mAliYunModel: AliYunModel by lazy { createViewModel(AliYunModel::class.java) }

    var headUrl = ""
    var nikeName = ""
    var bgUrl = ""
    var myProfile = ""
    var birthiday = ""
    var mGender = ""
    var mHeight = "170.0"
    var mWeight = "60.0"
    var measurement = "0"
    var srcheadUrl = ""
    var srcnikeName = ""
    var srcbgUrl = ""
    var srcmyProfile = ""
    var srcbirthiday = ""
    var srcmGender = ""
    var srcmHeight = "170.0"
    var srcmWeight = "60.0"
    var srcmeasurement = "0"
    var mSelectPopupWindow: SelectPopupWindow? = null


    var currentUnitHeight = ""
    var currentUnitWeight = ""

    override fun getLayoutResId(): Int = R.layout.activity_edit_user_info
    fun setItemValue() {
        if (CacheManager.mUserInfo != null) {
            nikeName = CacheManager.mUserInfo!!.nickName
            headUrl = CacheManager.mUserInfo!!.headUrlTiny
            bgUrl = CacheManager.mUserInfo!!.backgroundUrl
            mHeight = DateUtil.formatfloorOnePoint(CacheManager.mUserInfo!!.height)
            mWeight = DateUtil.formatfloorOnePoint(CacheManager.mUserInfo!!.weight)
            birthiday = CacheManager.mUserInfo!!.birthday
            measurement = CacheManager.mUserInfo!!.measurement
            myProfile = CacheManager.mUserInfo!!.myProfile
            srcnikeName = CacheManager.mUserInfo!!.nickName
            srcheadUrl = CacheManager.mUserInfo!!.headUrlTiny
            srcbgUrl = CacheManager.mUserInfo!!.backgroundUrl
            srcmHeight = DateUtil.formatfloorOnePoint(CacheManager.mUserInfo!!.height)
            srcmWeight = DateUtil.formatfloorOnePoint(CacheManager.mUserInfo!!.weight)
            srcbirthiday = CacheManager.mUserInfo!!.birthday
            srcmeasurement = CacheManager.mUserInfo!!.measurement
            srcmyProfile = CacheManager.mUserInfo!!.myProfile
            if (CacheManager.mUserInfo!!.gender.equals("Male")) {
                srcmGender = this.getString(R.string.gender_male)
                mGender = this.getString(R.string.gender_male)
            } else {
                srcmGender = this.getString(R.string.gender_female)
                mGender = this.getString(R.string.gender_female)
            }
            if (measurement.equals("0")) {
                currentHeightUnit = this.resources.getString(R.string.user_height_cm)
                currentWeightUnit = this.resources.getString(R.string.user_weight_kg)
            } else {
                currentHeightUnit = this.resources.getString(R.string.user_height_inch)
                currentWeightUnit = this.resources.getString(R.string.user_weight_lb)
            }


            currentUnitHeight =
                SiseUtil.heightUnitCoversion(
                    CacheManager.mUserInfo!!.height,
                    BikeConfig.userCurrentUtil
                )
            currentUnitWeight =
                SiseUtil.weightUnitCoversion(
                    CacheManager.mUserInfo!!.weight,
                    BikeConfig.userCurrentUtil
                )


            var stringsHeight = currentUnitHeight.split(".")
            currentIntHeight = stringsHeight[0].toInt()
            currentPointHeight = stringsHeight[1].toInt()
            var stringsWeight = currentUnitWeight.split(".")
            currentWeighInt = stringsWeight[0].toInt()
            currentWeightPoint = stringsWeight[1].toInt()

            Log.e(
                "initDataUser",
                "srcmWeight=" + srcmWeight + "currentUnitWeight =" + currentUnitWeight + " currentWeighInt =" + currentWeighInt + " currentWeightPoint =" + currentWeightPoint
            )
            Log.e(
                "initDataUser",
                "currentIntHeight=" + currentIntHeight + "currentPointHeight=" + currentPointHeight
            )

        }
        et_subname.setText(nikeName)
        et_myProfile.setText(myProfile)
        LoadImageUtil.getInstance().load(this, headUrl, iv_head, R.mipmap.friend_icon_default_photo)
        LoadImageUtil.getInstance()
            .load(this, bgUrl, iv_head_bg, R.mipmap.friend_icon_default_photo)
        itemview_pk_number.setRightText(currentUnitHeight + currentHeightUnit)
        itemview_weight.setRightText(currentUnitWeight + currentWeightUnit)
        itemview_birthday.setRightText(birthiday)
        itemview_select_line.setRightText(mGender)

    }

    override fun initView() {

    }

    var handler = Handler()

    var currentCount = 0


    override fun initData() {

        //改变默认的单行模式
        //改变默认的单行模式
        et_myProfile.isSingleLine = false

        //水平滚动设置为False

        setItemValue()
        //水平滚动设置为False
        et_myProfile.setHorizontallyScrolling(false)
        et_myProfile.rootView.setBackgroundColor(Color.WHITE)

        et_myProfile.setMovementMethod(ScrollingMovementMethod.getInstance())

        mSelectPopupWindow = SelectPopupWindow(object : SelectPopupWindow.OnSelectPopupListener {
            override fun onSelect(type: String, data: String) {
                when (type) {
                    SelectPopupWindow.BIRTHDAY -> {
                        birthiday = data
                        itemview_birthday.setRightText(data)
                    }
                    SelectPopupWindow.HEIGHT -> {
                        if (measurement.equals("0")) {
                            mHeight = data
                        } else {

                            mHeight = SiseUtil.mileToGongheight(data.toFloat())

                        }
                        currentUnitHeight = data
                        var stringsHeight = currentUnitHeight.split(".")
                        currentIntHeight = stringsHeight[0].toInt()
                        currentPointHeight = stringsHeight[1].toInt()
                        itemview_pk_number.setRightText(data + currentHeightUnit)
                    }
                    SelectPopupWindow.WEIGHT -> {
                        if (measurement.equals("0")) {
                            mWeight = data
                        } else {
                            mWeight = SiseUtil.mileToGongWeight(data.toFloat())
                        }
                        currentUnitWeight = data
                        var stringsWeight = currentUnitWeight.split(".")
                        currentWeighInt = stringsWeight[0].toInt()
                        currentWeightPoint = stringsWeight[1].toInt()
                        itemview_weight.setRightText(data + currentWeightUnit)
                    }
                    SelectPopupWindow.GENDER -> {
                        mGender = data
                        itemview_select_line.setRightText(data)
                    }
                }
            }

        })
    }

    var isUpgradeHead = false

    var customHead = ""
    var customUserBg = ""
    override fun startObserver() {
        mAliYunModel.mUpdatePhotoBean.observe(this, Observer {
            LoadImageUtil.getInstance()
                .load(this, it.headUrlTiny, iv_head, R.mipmap.friend_icon_default_photo)

        })
        mAliYunModel.mUpdateSuccess.observe(this, Observer {

        })
        mAliYunModel.mUpdatePath.observe(this, Observer {
            LoadImageUtil.getInstance()
                .load(this, it, iv_head_bg, R.mipmap.friend_icon_default_photo)

            bgUrl = it
            mUserModel.editBgUrl(it)
            // customUserBg = it


        })
        mAliYunModel.mOssBean.observe(this, Observer {

            var imgName = "" + System.currentTimeMillis() + "CustomBg$mUserId.jpg"
            mAliYunModel.upgradePic(
                it.getBucketName(),
                it.getAccessKeyId(),
                it.getAccessKeySecret(),
                it.getSecurityToken(),
                imgName,
                mImgPath
            )
        })
        mUserModel.mEditState.observe(this, Observer {
            if (it) {
                ToastUtil.showTextToast(BaseApp.sApplicaton, this.getString(R.string.save_success))
            } else {
                ToastUtil.showTextToast(BaseApp.sApplicaton, this.getString(R.string.save_fail))
            }
            finish()
        })
    }


    override fun setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, iv_back)
        StatusBarUtil.setLightMode(this)
    }

    var edtName: EditText? = null

    //偏移量
    private val offset = 0

    //当前页面获取了焦点的editText
    private var mainScrollView: NestedScrollView? = null
    private var scrollRunnable: Runnable? = null
    private var normalCanScroll = true
    private var currentFocusEt: EditText? = null

    //当前页面获取了所有的editText
    var editTexts: MutableList<EditText>? = null
    var scroll: NestedScrollView? = null

    /*  override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(R.layout.activity_edit_user_info)
          scroll = findViewById(R.id.layout_ScrollView)
          edtName = findViewById(R.id.et_nickname)
          initKeyBoardListener(scroll)
      }
  */
    /**
     * 要键盘弹出时，scrollView做滑动需，调用此方法
     *
     * @return
     */
    protected fun initKeyBoardListener(scrollView: NestedScrollView?) {
        mainScrollView = scrollView
        editTexts = ArrayList()
        findEditText(scrollView)
        setFoucesListener()
    }

    override fun onBackPressed() {
        back()
    }

    fun back() {
        if (isChange()) {

            var yesOrNoDialog = YesOrNoDialog(
                this,
                "",
                this.resources.getString(R.string.userinfo_change),
                "",
                resources.getString(R.string.userinfo_save)
            )
            yesOrNoDialog.show()
            yesOrNoDialog.setBtnOnclick(object : OnButtonListener {
                override fun onCancleOnclick() {
                    finish()
                }

                override fun onSureOnclick() {
                    if (et_subname.text.toString().isBlank()) {
                        ToastUtil.showTextToast(
                            BaseApp.sApplicaton,
                            getString(R.string.can_not_bank_nickname)
                        )
                        return
                    }
                    mUserModel.editUserInfo(
                        itemview_select_line.getRightText(),
                        mHeight,
                        mWeight,
                        itemview_birthday.getRightText(), nikeName, measurement, myProfile
                    )
                }
            })

        } else {
            finish()
        }
    }

    var currentHeightUnit = "cm"
    var currentWeightUnit = "kg"
    var currentIntHeight = 70
    var currentPointHeight = 0
    var currentWeighInt = 50
    var currentWeightPoint = 0
    override fun initEvent() {
        super.initEvent()
        et_myProfile.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                v.parent.requestDisallowInterceptTouchEvent(false)
            } else {
                v.parent.requestDisallowInterceptTouchEvent(true)
            }
            false
        })

        iv_back.setOnClickListener {
            back()
        }
        itemview_birthday.setOnClickListener {
            mSelectPopupWindow?.setPopupWindow(
                this@EditUserInfoActivity,
                itemview_birthday,
                SelectPopupWindow.BIRTHDAY,
                itemview_birthday.getRightText()
            )
        }
        itemview_select_line.setOnClickListener {
            mSelectPopupWindow?.popWindowSelect(
                this@EditUserInfoActivity,
                itemview_select_line,
                SelectPopupWindow.GENDER,
                itemview_select_line.getRightText(), false
            )
        }
        itemview_pk_number.setOnClickListener {
            mSelectPopupWindow?.setPopupWindowTemp(
                this@EditUserInfoActivity,
                itemview_pk_number,
                SelectPopupWindow.HEIGHT,
                currentIntHeight,
                currentPointHeight,
                BikeConfig.userCurrentUtil,
                currentHeightUnit
            )
        }
        itemview_weight.setOnClickListener {


            mSelectPopupWindow?.setPopupWindowTemp(
                this@EditUserInfoActivity,
                itemview_weight,
                SelectPopupWindow.WEIGHT,
                currentWeighInt,
                currentWeightPoint,
                BikeConfig.userCurrentUtil,
                currentWeightUnit
            )
        }

        iv_save.setOnClickListener {
            if (et_subname.text.toString().isBlank()) {
                ToastUtil.showTextToast(
                    BaseApp.sApplicaton,
                    getString(R.string.can_not_bank_nickname)
                )
                return@setOnClickListener
            }
            /* var measurement = "0"
             if (value) {
                 measurement = "0"
             } else {
                 measurement = "1"
             }*/

            if (isChange()) {
                mUserModel.editUserInfo(
                    itemview_select_line.getRightText(),
                    mHeight,
                    mWeight,
                    itemview_birthday.getRightText(),
                    nikeName,
                    measurement,
                    myProfile
                )
            } else {
                finish()
            }
        }


        et_myProfile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {


                myProfile = s.toString()
                // Logger.e("onTextChanged:" + s.toString() + ",s.toString().length():" + s.toString().length() + ",start:" + start + ",before:" + before + ",count:" + count);
                currentCount = s.toString().length
                // Logger.e("onTextChanged:" + s.toString() + ",s.toString().length():" + s.toString().length() + ",start:" + start + ",before:" + before + ",count:" + count);
                handler.post(Runnable {
                    tv_currentcount.text = (currentCount.toString())
                })

                //  tv_currentcount.setText(currentCount + "");
            }

            override fun afterTextChanged(s: Editable) {}
        })
        et_subname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {


                nikeName = s.toString()
                // Logger.e("onTextChanged:" + s.toString() + ",s.toString().length():" + s.toString().length() + ",start:" + start + ",before:" + before + ",count:" + count);

                //  tv_currentcount.setText(currentCount + "");
            }

            override fun afterTextChanged(s: Editable) {}
        })


        tv_chang_bg.setOnClickListener {
            isUpgradeHead = false
            initPermission()
        }
        iv_head.setOnClickListener {
            isUpgradeHead = true
            initPermission()
        }
    }

    override fun onResume() {
        super.onResume()
        //添加layout大小发生改变监听器
        //getContentView().addOnLayoutChangeListener(this)
    }

    fun getContentView(): View {
        return (findViewById<View>(R.id.layout_top) as ViewGroup).getChildAt(0)
    }

    var TAG = "onLayoutChange"

    override fun onLayoutChange(
        v: View?,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        oldLeft: Int,
        oldTop: Int,
        oldRight: Int,
        oldBottom: Int
    ) { //获取屏幕高度
        Log.e(TAG, "onLayoutChange")
        val screenHeight = this.windowManager.defaultDisplay.height
        //阀值设置为屏幕高度的1/3
        val keyHeight = screenHeight / 3
        if (mainScrollView != null && normalCanScroll) {
            normalCanScroll = mainScrollView!!.canScrollVertically(1)
            Log.e(TAG, "mainScrollView:canScroll:$normalCanScroll")
        }


        Log.e(
            TAG,
            "bottom=" + bottom + "oldBottom=" + oldBottom + "bottom + DateUtil.dip2px(220f)=" + (bottom + DateUtil.dip2px(
                220f
            )
                    )
        )
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && oldBottom - bottom > keyHeight) {
            Log.e(TAG, "弹起")
            if (currentFocusEt != null && currentFocusEt!!.id == edtName!!.id) {
            } else if (currentFocusEt != null) {
                Log.e(TAG, currentFocusEt!!.text.toString())
                val location = IntArray(2)
                currentFocusEt!!.getLocationOnScreen(location)
                val x = location[0]
                var y = location[1]
                val height = currentFocusEt!!.height
                y = y + height
                Log.e(
                    TAG,
                    "bottom:" + bottom + " currentFocusEt.bottom:" + y + "height:" + height + "offset:" + offset
                )
                if (y > bottom && mainScrollView != null) {
                    val finalY = y
                    if (normalCanScroll) {
                        scrollRunnable = Runnable {
                            Log.e(
                                TAG,
                                "finalY - bottom + offset:" + (finalY - bottom + offset)
                            )
                            mainScrollView!!.scrollBy(0, finalY - bottom + offset)
                        }
                        mainScrollView!!.postDelayed(scrollRunnable, 100)
                    } else {
                        mainScrollView!!.scrollBy(0, finalY - bottom + offset)
                    }
                }
            }
        } else if (oldBottom != 0 && bottom != 0 && bottom - oldBottom > keyHeight) {
            Log.e(TAG, "收回")
        }
    }

    /**
     * 监听焦点获取当前的获取焦点的editText
     *
     * @param v
     * @param hasFocus
     */
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus && v is EditText) {
            currentFocusEt = v
        }
    }

    /**
     * 找出当前页面的所有editText
     *
     * @param view
     */
    private fun findEditText(view: View?) {
        if (view is ViewGroup) {
            val viewGroup = view
            for (i in 0 until viewGroup.childCount) {
                val v = viewGroup.getChildAt(i)
                findEditText(v)
            }
        } else if (view is EditText) {
            editTexts!!.add(view)
        }
    }

    /**
     * 当前页面的所有editText设置焦点监听
     */
    private fun setFoucesListener() {
        for (et in editTexts!!) {
            et.setOnFocusChangeListener(this)
        }
    }

    fun initPermission() {
        PermissionUtil.checkPermission(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            permissonCallback = object : PermissionUtil.OnPermissonCallback {
                override fun isGrant(grant: Boolean) {
                    if (grant) {
                        showPhotoChoosePop()

                        // toast("success")
                    } else {
                        // toast("failed")
                    }
                }
            })
    }


    /**
     * 从照片选择或照相
     */

    // 调用系统相片 拍照
    private val PHOTO_REQUEST_CAMERA = 1

    // 从相册中选择
    private val PHOTO_REQUEST_GALLERY = 2

    private val PHOTO_CUT = 3
    private var tempFile: File? = null
    private var cutFile: File? = null
    private var mImgPath: String? = null
    var uri: Uri? = null
    private val cameraSavePath //拍照照片路径
            : File? = null
    var photoChoosePopUtil: PhotoChoosePopUtil? = null

    private fun showPhotoChoosePop() {
        if (null == photoChoosePopUtil) {
            photoChoosePopUtil = PhotoChoosePopUtil(this)
        }
        photoChoosePopUtil?.show(window.decorView)
        photoChoosePopUtil?.setOnPhotoChooseListener(object :
            PhotoChoosePopUtil.OnPhotoChooseListener {
            override fun onChooseCamera() {
                camera()
            }

            override fun onChoosePhotograph() {
                gallery()
            }
        })
    }


    /*
     * 从相机获取
     */
    fun camera() {
//        NetProgressObservable.getInstance().show(false);
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        if (FileUtil.isSDExists()) {
            val date = System.currentTimeMillis().toString()
            tempFile = File(FileUtil.getImageFile().getAbsolutePath(), "$date.jpeg")
            if (!tempFile!!.exists()) {
                FileUtil.createFile(tempFile!!.getAbsolutePath())
            }
            if (Build.VERSION.SDK_INT >= 24) {
                uri = FileProvider.getUriForFile(
                    this.getApplicationContext(),
                    "com.jkcq.homebike.bike.fileProvider",
                    tempFile!!
                )
            } else {
                uri = Uri.fromFile(tempFile)
            }
            Log.e("NewShareActivity", "uri=" + uri?.getPath())
            Log.e("NewShareActivity", "tempFile=" + tempFile?.getName() + this@EditUserInfoActivity)
            //  var path: String = BitmapUtils.getRealFilePath(this@EditUserInfoActivity, uri!!)
            /* Log.e(
                 "NewShareActivity",
                 "tempFileRealFilePath" + path + "tempFile:" + tempFile?.length()
             )*/
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(intent, PHOTO_REQUEST_CAMERA)
        }
    }

    /**
     * 从相册获取
     */
    fun gallery() {
//        NetProgressObservable.getInstance().show(false);
        val intent = Intent(Intent.ACTION_PICK) // 激活系统图库，选择一张图片
        intent.type = "image/*"
        startActivityForResult(
            intent,
            PHOTO_REQUEST_GALLERY
        ) // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(
            "onActivityResult",
            "requestCode=" + requestCode + "resultCode=" + resultCode
        )
        when (requestCode) {
            PHOTO_REQUEST_GALLERY ->
                // 从相册返回的数据
                if (data != null && resultCode == RESULT_OK) {
                    // 得到图片的全路径
                    val uri = data.data
                    val path = BitmapUtils.getRealFilePath(this, uri)
                    //                   setCustomBg(path);
                    if ("Redmi4A" == AppUtil.getModel()) {
                        //setCustomBg(path);
                        /* try {
                             val file: File = Compressor(this)
                                 .setMaxWidth(480)
                                 .setMaxHeight(480)
                                 .setQuality(75)
                                 .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                 .setDestinationDirectoryPath(FileUtil.getSDPath())
                                 .compressToFile(File(path))
                             mImgPath = file.absolutePath
                         } catch (e: IOException) {
                             e.printStackTrace()
                         }*/
                        mAliYunModel.getAliYunToken()
                    } else {
                        startPhotoZoom(uri!!)
                    }
                } else {
                    //NetProgressObservable.getInstance().hide()
                }
            PHOTO_REQUEST_CAMERA -> if (resultCode == RESULT_OK) {
                val uri: Uri = getUriForFile(this, tempFile)
                val path = BitmapUtils.getRealFilePath(this, uri)
                if ("Redmi4A" == DateUtil.getModel()) {
                    Log.e("setCustomBg", tempFile.toString() + "")
                    /*try {
                        val file: File = Compressor(this)
                            .setMaxWidth(480)
                            .setMaxHeight(480)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(FileUtil.getSDPath())
                            .compressToFile(File(path))
                        mImgPath = file.absolutePath
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }*/
                    mAliYunModel.getAliYunToken()
                } else {
                    startPhotoZoom(uri)
                }
            } else {
                //NetProgressObservable.getInstance().hide()
            }
            PHOTO_CUT -> Thread(Runnable { setCustomBg(mImgPath!!) }).start()
            else -> {
            }
        }
    }


    private fun setCustomBg(filePath: String) {
        runOnUiThread { //BleProgressObservable.getInstance().show(UIUtils.getString(R.string.));

            // LoadImageUtil.getInstance().displayImagePath(EditUserInfo.this, filePath, ivBg);
            val file = File(filePath)
            if (file != null && file.length() > 0) {
                // Logger.e("filePath" + filePath + "file:" + file.length())
                if (isUpgradeHead) {
                    mAliYunModel.updateHeadFile(file)
                } else {
                    mAliYunModel.getAliYunToken()
                }

            }
            /*BitmapDrawable bd = new BitmapDrawable(ImageUtils.getBitmap(filePath));
                        layout_bgiamge.setBackground(bd);*/
            /* iv_sure1.setVisibility(View.GONE);
                        iv_sure2.setVisibility(View.GONE);
                        iv_sure3.setVisibility(View.GONE);
                        fl_share_content.setBackground(bd);
                        //setAlpha 0-255
                        fl_share_content.getBackground().mutate().setAlpha(153);*/
        }
    }


    /**
     * 调用系统裁剪
     *
     * @param uri
     */
    private fun startPhotoZoom(uri: Uri) {
        try {
            val size = DateUtil.getScreenWidth(this)
            val intent = Intent("com.android.camera.action.CROP")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            //裁剪后输出路径
            if (FileUtil.isSDExists()) {
                val date = System.currentTimeMillis().toString()
                //            mImgPath=getExternalFilesDir(null).toString()+ "/" + date + ".jpg";
                //            mImgPath= getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()+ "/" + date + ".jpg";
                mImgPath =
                    FileUtil.getImageFile().absolutePath.toString() + "/" + date + ".jpg"
                cutFile = File(mImgPath)
                if (!cutFile!!.exists()) {
                    FileUtil.createFile(cutFile!!.getAbsolutePath())
                }
                //所有版本这里都这样调用
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cutFile))
                //               intent.putExtra(MediaStore.EXTRA_OUTPUT,  getUriForFile(NewShareActivity.this,cutFile));
//                intent.putExtra(MediaStore.EXTRA_OUTPUT,  uri);
                //输入图片路径
                intent.setDataAndType(uri, "image/*")
                intent.putExtra("crop", "true")
                if (Build.MANUFACTURER == "HUAWEI") {
                    intent.putExtra("aspectX", 9998)
                    intent.putExtra("aspectY", 9999)
                } else {
                    intent.putExtra("aspectX", 1)
                    intent.putExtra("aspectY", 1)
                }
                intent.putExtra("outputX", 480)
                intent.putExtra("outputY", 480)
                intent.putExtra("scale", true)
                intent.putExtra("scaleUpIfNeeded", true)
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
                intent.putExtra("return-data", false)

//                uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/brandapp/" + "small.jpg");
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
//                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                startActivityForResult(intent, PHOTO_CUT)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取uri
     *
     * @param context
     * @param file
     * @return
     */
    fun getUriForFile(
        context: Context,
        file: File?
    ): Uri {
        var fileUri: Uri? = null
        fileUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                "com.jkcq.homebike.bike.fileProvider",
                file!!
            )
        } else {
            Uri.fromFile(file)
        }
        return fileUri
    }

    /**
     * var headUrl=""
    var nikeName=""
    var bgUrl=""
    var myProfile=""
    var birthiday=""
    var mGender=""
    var mHeight = "170"
    var mWeight = "60"
    var measurement="0"
     */
    fun isChange(): Boolean {


        Log.e(
            "isChange", ",nikeName=" + nikeName + ",srcnikeName=" + srcnikeName
                    + "myProfile=" + myProfile + "srcmyProfile=" + srcmyProfile + ","
                    + "srcbgUrl=" + srcbgUrl + "bgUrl=" + bgUrl + ","
                    + "birthiday=" + birthiday + "srcbirthiday=" + srcbirthiday + ","
                    + "mGender=" + mGender + "srcbirthiday=" + srcmGender + ","
                    + "mHeight=" + mHeight + "srcmHeight=" + srcmHeight + ","
                    + "mWeight=" + mWeight + "mWeight=" + mWeight + ","
                    + "measurement=" + measurement + "srcmeasurement=" + srcmeasurement + ","
        )
        var isChange = false;


        /*if (srcbgUrl.equals(bgUrl)) {
            Log.e(
                "isChange", "srcbgUrl same"
            )
        } else {
            isChange = true
        }
        if (headUrl.equals(srcheadUrl)) {
            Log.e(
                "isChange", "headUrl same"
            )
        } else {
            isChange = true
        }*/
        if (nikeName.equals(srcnikeName)) {
            Log.e(
                "isChange", "nikeName same"
            )
        } else {
            isChange = true
        }
        if (myProfile.equals(srcmyProfile)) {
            Log.e(
                "isChange", "myProfile same"
            )
        } else {
            isChange = true
        }

        if (mWeight.equals(srcmWeight)) {
            Log.e(
                "isChange", "mWeight same"
            )
        } else {
            isChange = true
        }
        if (birthiday.equals(srcbirthiday)) {
            Log.e(
                "isChange", "birthiday same"
            )
        } else {
            isChange = true
        }
        if (mGender.equals(srcmGender)) {
            Log.e(
                "isChange", "mGender same"
            )
        } else {
            isChange = true
        }
        if (mHeight.equals(srcmHeight)) {
            Log.e(
                "isChange", "mHeight same"
            )
        } else {
            isChange = true
        }
        if (measurement.equals(srcmeasurement)) {
            Log.e(
                "isChange", "measurement same"
            )
        } else {
            isChange = true
        }

        Log.e(
            "isChange", "isChange same" + isChange
        )
        return isChange

    }

}