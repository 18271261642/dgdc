package com.example.websocket;

import android.content.Context;
import android.widget.Toast;

public class Util {

    public static String userId;
    public static String roomId;
    public static boolean pkEnd = true;
    public static String token;
    public static final String ws = "wss://test.gateway.spinning.fitalent.com.cn/spinning-websocket/pk/";//websocket测试地址

    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
}
