package com.jkcq.homebike.web;

import android.webkit.JavascriptInterface;

/**
 * Created by wangyong on 2018/06/08 18:18
 */
public class Android4Js extends Object {

    private IJsCallback jsCallback;

    public Android4Js(IJsCallback jsCallback) {
        this.jsCallback = jsCallback;
    }

    @JavascriptInterface
    public void postMessage(String data) {
        jsCallback.postMessage(data);
    }

    @JavascriptInterface
    public void moreList(String data) {
        jsCallback.moreList(data);
    }


}
