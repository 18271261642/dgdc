package com.jkcq.util.ktx;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;


public class ToastUtil {
    private static Toast toast = null;
    private static Context context;

    public static void init(Context cnt) {
        context = cnt;
    }

   /* public static void showTextToast(String msg) {
        if (TextUtils.isEmpty(msg) || msg.contains("没有访问权限！")) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }*/

    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(500);
    }

    private static long mLastClickTime = 0L;
    private static long mLastConn = 0L;
    private static long mLastSock = 0L;
    private static long mInterLastClickTime = 0L;

    private static boolean isFastDoubleClick(long timeGap) {
        boolean isFastDoubleClick = false;
        //获取当前时间
        long currTime = System.currentTimeMillis();
        if ((currTime - mLastClickTime) < timeGap) {
            isFastDoubleClick = true;
        } else {
            mLastClickTime = currTime;
        }
        return isFastDoubleClick;
    }
    public static boolean isFastSockt(long timeGap) {
        boolean isFastDoubleClick = false;
        //获取当前时间
        long currTime = System.currentTimeMillis();
        if ((currTime - mLastSock) < timeGap) {
            isFastDoubleClick = true;
        } else {
            mLastSock = currTime;
        }
        return isFastDoubleClick;
    }
    public static boolean isFastConn(long timeGap) {
        boolean isFastDoubleClick = false;
        //获取当前时间
        long currTime = System.currentTimeMillis();
        if ((currTime - mLastConn) < timeGap) {
            isFastDoubleClick = true;
        } else {
            mLastConn = currTime;
        }
        return isFastDoubleClick;
    }
    public static boolean isInterFastDoubleClick(long timeGap) {
        boolean isFastDoubleClick = false;
        //获取当前时间
        long currTime = System.currentTimeMillis();
        if ((currTime - mInterLastClickTime) < timeGap) {
            isFastDoubleClick = true;
        } else {
            mInterLastClickTime = currTime;
        }
        return isFastDoubleClick;
    }


    public static void showTextToast(Context context, int msg) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), context.getApplicationContext().getString(msg), Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    public static void showTextToastById(int msg) {
        if (toast == null) {
            toast = Toast.makeText(context, context.getResources().getString(msg), Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showTextToast(Context context, String msg) {
        if (TextUtils.isEmpty(msg) || msg.contains("没有访问权限！")) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        //	toast.show();
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showTextToastById(Context context, int msg) {
        if (toast == null) {
            toast = Toast.makeText(context, context.getResources().getString(msg), Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
