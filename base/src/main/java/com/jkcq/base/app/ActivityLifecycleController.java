package com.jkcq.base.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * created by wq on 2019/6/26
 */
public class ActivityLifecycleController implements Application.ActivityLifecycleCallbacks {

    /**
     * 维护Activity 的list
     */
    private static List<Activity> mActivitys = Collections.synchronizedList(new LinkedList<Activity>());

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.e("activity", "activity=" + activity.getClass().getSimpleName());
        pushActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        //自动模式不用埋点，本质也是在ActivityLifecycleCallbacks中调用
//        MobclickAgent.onResume(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
//        MobclickAgent.onPause(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        popActivity(activity);

    }


    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    public void pushActivity(Activity activity) {
        mActivitys.add(activity);
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    public void popActivity(Activity activity) {
        mActivitys.remove(activity);
    }

    public static void finishAllActivity(String className) {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
            if (className != null) {
                Log.e("activity", "className=" + className + "activity = " + activity.getClass().getSimpleName());
                if (!className.equals(activity.getClass().getSimpleName())) {
                    activity.finish();
                }
            } else {
                activity.finish();
            }

        }
        mActivitys.clear();
    }

    public static void finishAllActivity(String className, String className2) {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
            if (className != null) {
                if (!className.equals(activity.getClass().getSimpleName()) && !className2.equals(activity.getClass().getSimpleName())) {
                    activity.finish();
                }
            } else {
                activity.finish();
            }

        }
        mActivitys.clear();
    }
}
