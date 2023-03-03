package com.jkcq.appupdate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * created by wq on 2019/6/25
 */
public class ApkDownLoadManager {
    private static final String TAG = ApkDownLoadManager.class.getSimpleName();
    DownloadTask task;
    private long fileTotalLength = 1;
    private long increaseLength = 0;
    private String mFileName = "jkcq.apk";
    private Handler mHandler = new Handler(Looper.getMainLooper());
    UpdateDialog updateDialog;
    private Activity mActivity;

    public ApkDownLoadManager(Activity activity) {
        this.mActivity = activity;
    }

    public void startDownLoad(String apkDownloadUrl) {
        updateDialog = new UpdateDialog(mActivity, 1, mActivity.getResources().getString(R.string.downloading));
        updateDialog.setOnDialogClickListener(updateDialogListener);
        updateDialog.show();
        downloadAPK(apkDownloadUrl, mFileName);
    }

    private OnDialogClickListener updateDialogListener = new OnDialogClickListener() {
        @Override
        public void dialogClickType(int type) {
            updateDialog.dismiss();
            task.cancel();
            increaseLength = 0;
            fileTotalLength = 1;
            if (AppUpdateUtils.isUpdateMandatory) {
                mActivity.finish();
            }
        }
    };

    /**
     * 下载apk
     *
     * @param url
     * @param fileName
     */
    public void downloadAPK(String url, final String fileName) {
        task = new DownloadTask.Builder(url, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
                .setFilename(fileName)
                .setReadBufferSize(8192)
                .setFlushBufferSize(30768)
                // the minimal interval millisecond for callback progress
                .setMinIntervalMillisCallbackProcess(100)
                // do re-download even if the task has already been completed in the past.
                .setPassIfAlreadyCompleted(false)
//                .setAutoCallbackToUIThread(true)
                .build();
        task.enqueue(mDownloadListener);

    }

    /**
     * 通过隐式意图调用系统安装程序安装APK
     */
    public void install(Context context, File file) {
        AppUpdateUtils.isDownLoad = true;
        updateDialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
//            Uri apkUri = FileProvider.getUriForFile(context, "com.jkcq.gym.phone.fileProvider", file);
            Uri apkUri = FileProvider.getUriForFile(context, "com.jkcq.gym.phone.fileProvider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    DownloadListener mDownloadListener = new DownloadListener() {
        @Override
        public void taskStart(DownloadTask task) {
            Log.e(TAG, "-------taskStart----");
        }

        @Override
        public void connectTrialStart(DownloadTask task, Map<String, List<String>> requestHeaderFields) {
            Log.e(TAG, "-------connectTrialStart----");
        }

        @Override
        public void connectTrialEnd(DownloadTask task, int responseCode, Map<String, List<String>> responseHeaderFields) {
            Log.e(TAG, "-------connectTrialEnd----");
        }

        @Override
        public void downloadFromBeginning(DownloadTask task, BreakpointInfo info, ResumeFailedCause cause) {
            Log.e(TAG, "-------downloadFromBeginning----");
            fileTotalLength = info.getTotalLength();
            // updateDialog.setTvPackgeSize(AppUpdateUtils.div(fileTotalLength, 1024 * 1024, 2));
        }

        @Override
        public void downloadFromBreakpoint(DownloadTask task, BreakpointInfo info) {
            Log.e(TAG, "-------downloadFromBreakpoint----" + " offset=" + info.getTotalOffset());
            increaseLength = OkDownload.with().breakpointStore().get(task.getId()).getTotalOffset();
            fileTotalLength = info.getTotalLength();
            // updateDialog.setTvPackgeSize(AppUpdateUtils.div(fileTotalLength, 1024 * 1024, 2));
        }

        @Override
        public void connectStart(DownloadTask task, int blockIndex, Map<String, List<String>> requestHeaderFields) {
            Log.e(TAG, "-------connectStart----");
        }

        @Override
        public void connectEnd(DownloadTask task, int blockIndex, int responseCode, Map<String, List<String>> responseHeaderFields) {
            Log.e(TAG, "-------connectEnd----");
        }

        @Override
        public void fetchStart(DownloadTask task, int blockIndex, long contentLength) {
            Log.e(TAG, "-------fetchStart----");
        }

        @Override
        public void fetchProgress(DownloadTask task, int blockIndex, long increaseBytes) {
            Log.e(TAG, "-------fetchProgress----" + increaseBytes + " increaseLength=" + increaseLength + " fileTotalLength=" + fileTotalLength);

            increaseLength = increaseLength + increaseBytes;
            updateDialog.updateProgress((increaseLength * 100 / fileTotalLength));
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                },500);
//                updateDialog.updateProgress((float) Arith.div(increaseLength,fileTotalLength,0));
        }

        @Override
        public void fetchEnd(DownloadTask task, int blockIndex, long contentLength) {
            Log.e(TAG, "-------fetchEnd----");
        }

        @Override
        public void taskEnd(DownloadTask task, EndCause cause, @Nullable Exception realCause) {
            Log.e(TAG, "-------taskEnd----" + cause.name() + " realCause=" + " task.getId()=" + task.getId());
            //下载完成
//                OkDownload.with().breakpointStore().remove(task.getId());
            if (cause.name().equals(EndCause.ERROR.name())) {
                task.enqueue(mDownloadListener);
                return;
            }
            if (cause.name().equals(EndCause.COMPLETED.name())) {
                File file = new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        , mFileName);
                Log.e("ApkDownLoadManager", file.getAbsolutePath());
                install(mActivity, file);
            }
//                if (increaseLength == fileTotalLength) {
//                    increaseLength = 0;
//                    fileTotalLength = 0;
//                    updateDialog.dismiss();
//                    // 下载完成后，开启系统安装apk功能！
//                    File file = new File(
//                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                            , mFileName);
//                    Log.e("ApkDownLoadManager", file.getAbsolutePath());
//                    install(mActivity, file);
////                    Intent intent = new Intent();
////                    intent.setAction("android.intent.action.VIEW");
////                    intent.addCategory("android.intent.category.DEFAULT");
////                    intent.setDataAndType(
////                            Uri.parse("file:" + new File(MyFileUtil.getVideoDir() + "/" + fileName).getAbsolutePath()),
////                            "application/vnd.android.package-archive");
////                    startActivityForResult(intent, 1);
//                }
        }
    };
}
