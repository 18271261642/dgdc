package com.jkcq.homebike.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebSettings.LayoutAlgorithm
import android.webkit.WebSettings.ZoomDensity
import android.webkit.WebView
import android.webkit.WebViewClient
import com.jkcq.base.base.BaseTitleActivity
import com.jkcq.homebike.R
import kotlinx.android.synthetic.main.activity_web_view.*


class WebViewActivity : BaseTitleActivity() {

    var fileChooserWebChromeClient: FileChooserWebChromeClient? = null

    override fun ivRight() {
    }


    override fun getLayoutResId(): Int = R.layout.activity_web_view

    override fun initView() {
        fileChooserWebChromeClient = FileChooserWebChromeClient.createBuild { intent ->
            startActivityForResult(
                intent,
                UploadMessage.FILE_CHOOSER_RESULT_CODE
            )
        }
        //设置到自己的webview
        //设置到自己的webview
        webview.setWebChromeClient(fileChooserWebChromeClient)
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
        // mWebView.webChromeClient = wvcc
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_")
    }

    private val mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            view.loadUrl("javascript:window.mHandler.show(document.body.innerHTML);")
            super.onPageFinished(view, url)
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


    var wvcc: WebChromeClient = object : WebChromeClient() {


        override fun onReceivedTitle(view: WebView, titlet: String) {
            super.onReceivedTitle(view, titlet)
            Log.e("onReceivedTitle", titlet);
            setTitleText(titlet)

        }

        override fun onProgressChanged(view: WebView, newProgress: Int) {
            if (newProgress == 100) {
                // load_pro.setVisibility(View.GONE)
            } else {
                /*  if (load_pro.getVisibility() == View.GONE) load_pro.setVisibility(
                      View.VISIBLE
                  )
                  load_pro.setProgress(newProgress)*/
            }
        }
    }

    override fun initEvent() {


    }

    var strtitle = "";
    override fun initData() {
        setTitle("锻炼记录")
        var url = ""
        url = intent.getStringExtra("url")
        strtitle = intent.getStringExtra("title")
        if (!TextUtils.isEmpty(strtitle))
            setTitleText(strtitle)

        Log.e("initData url", url)
        initSettings(webview)
        webview.loadUrl(url);
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UploadMessage.FILE_CHOOSER_RESULT_CODE) {
            fileChooserWebChromeClient!!.uploadMessage.onActivityResult(
                requestCode,
                resultCode,
                data
            )
        }
    }
}