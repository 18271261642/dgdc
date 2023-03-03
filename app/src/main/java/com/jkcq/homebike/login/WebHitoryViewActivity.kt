package com.jkcq.homebike.login

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebSettings.LayoutAlgorithm
import android.webkit.WebSettings.ZoomDensity
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.example.utillibrary.PermissionUtil
import com.jkcq.base.app.BaseApp
import com.jkcq.base.base.BaseTitleActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.login.bean.ShareBean
import com.jkcq.homebike.login.bean.SharePicAdapter
import com.jkcq.homebike.util.FileUtil
import com.jkcq.util.TipsDialog
import com.jkcq.util.ktx.ToastUtil
import kotlinx.android.synthetic.main.activity_hitory_web_view.*
import java.io.File
import java.util.*


class WebHitoryViewActivity : BaseTitleActivity() {
    lateinit var bikeSportDetailAdapter: SharePicAdapter

    var whiteFile: File? = null
    var blackFile: File? = null
    var downMusicDialog: TipsDialog? = null

    fun downMusicDialog() {
        if (downMusicDialog != null && downMusicDialog!!.isShowing) {
            return
        }
        downMusicDialog = TipsDialog(this, this.getString(R.string.data_is_waiting))
        downMusicDialog!!.show()
    }

    fun hideMusicDialog() {
        if (downMusicDialog != null && downMusicDialog!!.isShowing) {
            downMusicDialog!!.dismiss();
            downMusicDialog = null;
        }
    }

    fun initPermission() {
        downMusicDialog()
        PermissionUtil.checkPermission(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            permissonCallback = object : PermissionUtil.OnPermissonCallback {
                override fun isGrant(grant: Boolean) {
                    if (grant) {
                        mDataList.clear()
                        if (!showOne) {
                            whiteFile = getFullScreenBitmap(scrollview_white, false)
                            mDataList.add(ShareBean(false, whiteFile, false))
                            handler.postDelayed(Runnable {
                                blackFile = getFullScreenBitmap(scrollview_black, true)
                                mDataList.add(ShareBean(true, blackFile, true))
                                setIvRightIcon(R.drawable.icon_share)
                                showRec()
                                hideMusicDialog()
                            }, 2000)
                        } else {
                            mDataList.clear()
                            whiteFile = getFullScreenBitmap(scrollview_pk, false)
                            mDataList.add(ShareBean(false, whiteFile, false))
                            setIvRightIcon(R.drawable.icon_share)
                            showRec()
                            hideMusicDialog()
                        }
                    } else {
                        ToastUtil.showTextToast(
                            BaseApp.sApplicaton,
                            this@WebHitoryViewActivity.getString(R.string.no_required_permission)
                        )
                    }
                }
            })
    }

    fun permission() {
        PermissionUtil.checkPermission(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            permissonCallback = object : PermissionUtil.OnPermissonCallback {
                override fun isGrant(grant: Boolean) {
                    if (grant) {

                    } else {
                        ToastUtil.showTextToast(
                            BaseApp.sApplicaton,
                            this@WebHitoryViewActivity.getString(R.string.no_required_permission)
                        )
                    }
                }
            })
    }


    override fun ivRight() {
        layout_share_pic.setVisibility(View.GONE)

        if (showOne) {
            initPermission()
        } else {
            if (mDataList.size > 0) {

                showRec()
            } else {
                initPermission()
            }
        }

    }


    fun initSportDetailRec() {
        recyclerview_message.visibility = View.INVISIBLE
        recyclerview_message.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, true)
        bikeSportDetailAdapter = SharePicAdapter(mDesDataList)
        recyclerview_message.adapter = bikeSportDetailAdapter
        bikeSportDetailAdapter.setOnItemChildClickListener { adapter, view, position ->
            Log.e("setOnItemClickListener", "setOnItemClickListener----" + position)
            mDesDataList.forEach {
                it.isSelect = false
            }
            mDesDataList.get(position).isSelect = true
            bikeSportDetailAdapter.notifyDataSetChanged()
        }
        bikeSportDetailAdapter.setOnItemClickListener(OnItemClickListener { adapter, view, position ->

            Log.e("setOnItemClickListener", "setOnItemClickListener----" + position)
            mDesDataList.forEach {
                it.isSelect = false
            }
            mDesDataList.get(position).isSelect = true
            bikeSportDetailAdapter.notifyDataSetChanged()

        })
    }


    fun showRec() {

        if (showOne) {
            if (mDataList.size >= 1) {
                mDesDataList.clear()
                mDesDataList.add(mDataList.get(0))
            }
        } else {
            if (mDataList.size >= 2) {
                mDesDataList.clear()
                mDesDataList.add(mDataList.get(0))
                mDesDataList.add(mDataList.get(1))
            }
        }
        recyclerview_message.visibility = View.VISIBLE
        if (mDesDataList.size > 1) {
            recyclerview_message.scrollToPosition(mDataList.size - 1)
        }
        layout_share.visibility = View.VISIBLE
        layout_share_pic.setVisibility(View.VISIBLE)
        bikeSportDetailAdapter.notifyDataSetChanged()
        //  mMessageAdapter.notifyDataSetChanged()
    }


    override fun getLayoutResId(): Int = R.layout.activity_hitory_web_view

    override fun initView() {
        //设置到自己的webview
        //设置到自己的webview
        initSportDetailRec()
        layout_share_pic.setOnClickListener {
            layout_share.visibility = View.GONE
            layout_share_pic.visibility = View.GONE
        }
        iv_share_all.setOnClickListener {

            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }

            if (mDataList.size > 2) {
                mDataList.removeAt(0)
                mDataList.removeAt(1)
            }

            if (showOne) {
                shareFile(mDataList.get(0).shareFile)
            } else {

                mDataList.forEach {
                    if (it.isSelect) {
                        shareFile(it.shareFile)
                    }
                }
            }

        }
    }

    //
    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface", "NewApi")
    private fun initSettings(mWebView: WebView) {
        mWebView.isHorizontalScrollBarEnabled = false //水平不显示
        mWebView.isVerticalScrollBarEnabled = false //垂直不显示
        val webSettings = mWebView.settings
        // 开启java script的支持
        webSettings.javaScriptEnabled = true
        // mWebView.addJavascriptInterface(new mHandler(), "mHandler");
        // 启用localStorage 和 essionStorage
        webSettings.domStorageEnabled = true

        // 开启应用程序缓存
        webSettings.setAppCacheEnabled(true)
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.layoutAlgorithm = LayoutAlgorithm.SINGLE_COLUMN
        val appCacheDir =
            this.applicationContext.getDir("cache", Context.MODE_PRIVATE)
                .path
        webSettings.setAppCachePath(appCacheDir)
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.displayZoomControls = false

//		webSettings.setAppCacheMaxSize(1024 * 1024 * 10);// 设置缓冲大小，我设的是10M
        webSettings.allowFileAccess = true
        webSettings.domStorageEnabled = true
        mWebView.webViewClient = mWebViewClient
        val dm = resources.displayMetrics
        val scale = dm.densityDpi
        if (scale == 240) { //
            mWebView.settings.defaultZoom = ZoomDensity.FAR
        } else if (scale == 160) {
            mWebView.settings.defaultZoom = ZoomDensity.MEDIUM
        } else {
            mWebView.settings.defaultZoom = ZoomDensity.CLOSE
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        mWebView.webChromeClient = wvcc
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_")
    }

    var scale = 0.0f;

    private val mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            view.loadUrl("javascript:window.mHandler.show(document.body.innerHTML);")
            super.onPageFinished(view, url)
        }

        override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
            super.onScaleChanged(view, oldScale, newScale)
            scale = newScale
            Log.e("onScaleChanged", "newScale" + newScale)
        }

        override fun onReceivedError(
            view: WebView,
            errorCode: Int,
            description: String,
            failingUrl: String
        ) {
            //circle_mainhtml_null.setVisibility(View.VISIBLE);
            webview.setVisibility(View.GONE)
        }
    }
    var mDataList = mutableListOf<ShareBean>()
    var mDesDataList = mutableListOf<ShareBean>()

    /**
     * 获取长截图
     *
     * @return
     */
    fun getFullScreenBitmap(scrollVew: NestedScrollView, isDark: Boolean): File? {
        var h = 0
        val bitmap: Bitmap
        for (i in 0 until scrollVew.childCount) {
            h += scrollVew.getChildAt(i).height
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(
            scrollVew.width, h,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        canvas.drawColor(this.resources.getColor(R.color.common_view_color))
        scrollVew.draw(canvas)

        //获取顶部布局的bitmap
        /*val head = Bitmap.createBitmap(
            rl_scale_report_head.getWidth(), rl_scale_report_head.getHeight(),
            Bitmap.Config.ARGB_8888
        )
        val canvasHead = Canvas(head)
        canvasHead.drawColor(Color.WHITE)
        rl_scale_report_head.draw(canvasHead)*/
        /*  val newbmp =
              Bitmap.createBitmap(scrollVew.width, h + head.height, Bitmap.Config.ARGB_8888)
          val cv = Canvas(newbmp)*/
        // cv.drawBitmap(head, 0f, 0f, null) // 在 0，0坐标开始画入headBitmap
        //  cv.drawBitmap(bitmap, 0f, head.height.toFloat(), null) // 在 0，headHeight坐标开始填充课表的Bitmap
        canvas.save() // 保存
        canvas.restore() // 存储
        //回收
        //head.recycle()
        // 测试输出
        var file = FileUtil.getImageFile(FileUtil.getPhotoFileName())
        if (isDark) {
            darkPath = file.path
        } else {
            lightPath = file.path
        }
        return FileUtil.writeImage(bitmap, file, 100)
    }

    var darkPath = ""
    var lightPath = ""


    var webdark = false;
    var weblight = false;

    var isFirst = false;

    var wvcc: WebChromeClient = object : WebChromeClient() {


        override fun onReceivedTitle(view: WebView, titlet: String) {
            super.onReceivedTitle(view, titlet)
            Log.e("onReceivedTitle", titlet);
            //  setTitleText(titlet)

        }

        override fun onProgressChanged(view: WebView, newProgress: Int) {

            Log.e(
                "onProgressChanged",
                "" + view + "--newProgress=" + newProgress + "view" + view.url
            )

            try {

                if (view != null) {
                    if (view.url.contains("rank") && !isFirst && !showOne) {
                        setHideRightIcon()
                        isFirst = true
                        showOne = true
                        webview_pk.loadUrl(view.url)
                        initSettings(webview_pk)
                        strtitle =
                            this@WebHitoryViewActivity.getString(R.string.sport_pk_rank_title);
                        setTitleText(strtitle)
                    }
                }
                if (newProgress == 100) {
                    load_pro.visibility = View.GONE
                } else {
                    if (load_pro.visibility == View.GONE) load_pro.visibility = View.VISIBLE
                    load_pro.progress = newProgress
                }
                if (!showOne) {
                    if (newProgress == 100) {
                        handler.postDelayed(Runnable {
                            setIvRightIcon(R.drawable.icon_share)
                        }, 10000)
                    }
                } else {
                    handler.postDelayed(Runnable {
                        setIvRightIcon(R.drawable.icon_share)
                    }, 5000)
                }
            } catch (e: Exception) {

            } finally {

            }


        }
    }

    override fun initEvent() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            android.webkit.WebView.enableSlowWholeDocumentDraw();
        }
    }

    var showOne = false
    var strtitle = "";
    override fun initData() {
        permission()

        var url = ""
        var darkUrl = ""
        showOne = intent.getBooleanExtra("showOne", false);
        url = intent.getStringExtra("lighturl")
        darkUrl = intent.getStringExtra("darkurl")

        strtitle = intent.getStringExtra("title")
        if (!TextUtils.isEmpty(strtitle))
            setTitleText(strtitle)


        Log.e("initData url", url)

        initSettings(webview)
        webview.loadUrl(url);

        if (!showOne) {
            initSettings(webview_black)
            webview_black.loadUrl(darkUrl);
            initSettings(webview_white)
            webview_white.loadUrl(url);
        } else {
            initSettings(webview_pk)
            webview_pk.loadUrl(url);
        }

    }

    fun shareFile(file: File?) {
        if (null != file && file.exists()) {
            val share = Intent(Intent.ACTION_SEND)
            var uri: Uri? = null
            // 判断版本大于等于7.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // "项目包名.fileprovider"即是在清单文件中配置的authorities

                //判读版本是否在7.0以上
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
//            Uri apkUri = FileProvider.getUriForFile(context, "com.jkcq.gym.phone.fileProvider", file);
                uri = FileProvider.getUriForFile(
                    this@WebHitoryViewActivity,
                    "com.jkcq.homebike.bike.fileProvider",
                    file
                )
                /*uri = FileProvider.getUriForFile(
                    this@DayShareActivity, "$packageName.fileprovider",
                    file
                )*/
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else {
                uri = Uri.fromFile(file)
            }
            share.putExtra(Intent.EXTRA_STREAM, uri)
            share.type = getMimeType(file.absolutePath) //此处可发送多种文件
            share.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            this.startActivity(Intent.createChooser(share, "Share Image"))
        }
    }

    // 根据文件后缀名获得对应的MIME类型。
    private fun getMimeType(filePath: String): String? {
        val mmr = MediaMetadataRetriever()
        /* if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }
        }*/return "image/*"
    }

    var handler = Handler()


    private fun captureWebView(webView: WebView, isDark: Boolean): File? {


        val scale = webView.scale
        val height = (webView.contentHeight * scale + 0.5).toInt()


        Log.e("captureWebView", "scale=" + scale + "height=" + height)

        val bitmap = Bitmap.createBitmap(
            webView.getMeasuredWidth(),
            height,
            Bitmap.Config.RGB_565
        )
        val canvas = Canvas(bitmap)
        webView.draw(canvas)
        //  return bitmap

        var file = FileUtil.getImageFile(FileUtil.getPhotoFileName())
        if (isDark) {
            darkPath = file.path
        } else {
            lightPath = file.path
        }
        return FileUtil.writeImage(bitmap, file, 100)
    }

}