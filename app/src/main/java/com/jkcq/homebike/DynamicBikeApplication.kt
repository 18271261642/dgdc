package com.jkcq.homebike

import android.net.http.HttpResponseCache
import com.alibaba.android.arouter.launcher.ARouter
import com.jkcq.base.app.BaseApp
import com.jkcq.homebike.db.BikeDbModel
import com.tencent.bugly.crashreport.CrashReport
import java.io.File


/**
 *  Created by BeyondWorlds
 *  on 2020/7/24
 */
class DynamicBikeApplication : BaseApp() {


    override fun onCreate() {
        super.onCreate()
        ARouter.openLog();
        ARouter.openDebug();
        ARouter.init(this)
        BikeDbModel.init(this)
        CrashReport.initCrashReport(getApplicationContext(), "a0a2afe900", true);
        HttpResponseCache.install(
            File(this.getExternalFilesDir(null), "svga"),
            1024 * 1024 * 128
        )
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/
        // PlatformManager(this).init()
    }


}