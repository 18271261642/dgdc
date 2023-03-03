package com.jkcq.homebike.web;

import android.os.Build;
import android.text.TextUtils;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by wangyong on 2018/06/08 18:23
 */
public class WebViewHelper {
    private static final String TAG = WebViewHelper.class.getSimpleName();

    public static void enableJs(WebView webView, IJsCallback jsCallback) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);// 设置与Js交互的权限
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.addJavascriptInterface(new Android4Js(jsCallback), "andriod");//AndroidtoJS类对象映射到js的android对象
        // webView.addJavascriptInterface(new Android4Js(jsCallback), "andriod");//AndroidtoJS类对象映射到js的android对象
        // webView.addJavascriptInterface(BleHelper4WebView.getInstance(webView), "medica");//AndroidtoJS类对象映射到js的android对象
    }

    public static void returnData2Js(final WebView webView, final String json) {
        jsCallback(webView, "dataReceived('" + json + "')");
    }

    public static void jsCallback(final WebView webView, final String callback) {
        if (webView != null && !TextUtils.isEmpty(callback)) {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.evaluateJavascript("javascript:" + callback, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                        }
                    });
                }
            });
        }
    }
}
