package com.jkcq.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

public class PackageUtil {
    //微博  com.sina.weibo
//QQ   com.tencent.mobileqq

    public static final String qqPakage = "com.tencent.mobileqq";
    public static final String weichatPakage = "com.tencent.mm";
    public static final String weiboPakage = "com.sina.weibo";
    public static final String facebookPakage = "com.facebook.katana";
    public static final String twitterPakage = "com.twitter.android";

    public static boolean isWxInstall( String pacage) {
        final PackageManager packageManager = AppUtil.mApp.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(pacage)) {
                    return true;
                }
            }
        }

        return false;
    }
}
