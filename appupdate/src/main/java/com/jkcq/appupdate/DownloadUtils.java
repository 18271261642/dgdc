package com.jkcq.appupdate;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 *
 *
 * @author mhj
 * Create at 2019/3/26 12:03
 */
public class DownloadUtils {


    private DownloadUtils() {

    }

    private static class SingletonHolder {
        private final static DownloadUtils instance = new DownloadUtils();
    }

    public static DownloadUtils getInstance() {

        return SingletonHolder.instance;
    }


    private OnDialogClickListener updateDialogListener = new OnDialogClickListener() {
        @Override
        public void dialogClickType(int type) {
            //updateDialog.dismiss();
            task.cancel();
            increaseLength = 0;
            fileTotalLength = 1;
        }
    };

    public void downSingleloadSenceOnSerial(final String downloadSence, String url, String fileName, final onDownloadListener listener) {


        DownloadTask senceTask;

        senceTask = new DownloadTask.Builder(url, new File(FileUtil.getVideoDir()))
                .setFilename(fileName)
                .setMinIntervalMillisCallbackProcess(500)
                .setPassIfAlreadyCompleted(false)
                .build();
        senceTask.setTag(downloadSence);
        addTask.add(senceTask);
        senceTask.enqueue(new DownloadListener() {
            @Override
            public void taskStart(DownloadTask task) {
                increaseLength = 0;
                Log.e("shao", "-------taskStart----");
            }

            @Override
            public void connectTrialStart(DownloadTask task, Map<String, List<String>> requestHeaderFields) {
                Log.e("shao", "-------connectTrialStart----");
            }

            @Override
            public void connectTrialEnd(DownloadTask task, int responseCode, Map<String, List<String>> responseHeaderFields) {
                Log.e("shao", "-------connectTrialEnd----");
            }

            @Override
            public void downloadFromBeginning(@NonNull DownloadTask task, @NonNull BreakpointInfo info, @NonNull ResumeFailedCause cause) {
                Log.e("shao", "-------downloadFromBeginning----" + " fileLength: " + info.getTotalLength());
                fileTotalLength = info.getTotalLength();
                //listener.onStart((float) Arith.div(fileTotalLength, 1024 * 1024, 2));
                /*mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateDialog.setTvPackgeSize(AppUpdateUtils.div(fileTotalLength, 1024 * 1024, 2));
                    }
                });*/

            }

            @Override
            public void downloadFromBreakpoint(@NonNull DownloadTask task, @NonNull BreakpointInfo info) {
                Log.e("shao", "-------downloadFromBreakpoint----" + " fileLength: " + info.getTotalLength());

                increaseLength = OkDownload.with().breakpointStore().get(task.getId()).getTotalOffset();
                fileTotalLength = info.getTotalLength();
              /*  mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateDialog.setTvPackgeSize(AppUpdateUtils.div(fileTotalLength, 1024 * 1024, 2));
                    }
                });*/

            }

            @Override
            public void connectStart(@NonNull DownloadTask task, int blockIndex, @NonNull Map<String, List<String>> requestHeaderFields) {
                Log.e("shao", "-------connectStart----");
            }

            @Override
            public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {
                Log.e("shao", "-------connectEnd----");
            }

            @Override
            public void fetchStart(@NonNull DownloadTask task, int blockIndex, long contentLength) {
                Log.e("shao", "-------fetchStart----" + " blockIndex: " + blockIndex + " contentLength : " + contentLength);
            }

            @Override
            public void fetchProgress(@NonNull DownloadTask task, int blockIndex, long increaseBytes) {
                Log.e("shao", "-------fetchProgress----blockIndex" + blockIndex + "increaseBytes=" + increaseBytes+"fileTotalLength="+fileTotalLength+"increaseLength="+increaseLength);
                increaseLength = increaseLength + increaseBytes;
                listener.onProgress(increaseLength, fileTotalLength);

            }

            @Override
            public void fetchEnd(@NonNull DownloadTask task, int blockIndex, long contentLength) {
                Log.e("shao", "-------fetchEnd----" + " blockIndex " + blockIndex + " contentLength: " + contentLength);
            }

            @Override
            public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause) {
                Log.e("shao", "-------taskEnd----" + "increaseLength >= fileTotalLength" + increaseLength + ":" + fileTotalLength + "cause=" + cause);
                try {
                    if (cause == EndCause.COMPLETED) {

                        //listener.onComplete();
                        //保存场景文件
                        clear();
                        String upgradeId = ((String) task.getTag());
                        listener.onComplete(upgradeId, task.getFile().getPath());
                        /*mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                updateDialog.dismiss();
                            }
                        });*/

                        // data.setVideoPath(task.getFile().getPath());
                        //FileSaveUtils.saveSenceData(data);
                    } else if (cause == EndCause.CANCELED) {
                        //下载失败

                    } else if (cause == EndCause.ERROR) {
                        clear();
                       listener.onFail();
                    }
                } catch (Exception e) {
                    Log.e("shao", "-------fetchEnd----" + e.toString());
                }


            }
        });
    }


    private volatile long fileTotalLength = 0;
    private volatile long increaseLength;
    public DownloadTask task;

    public void downLoadApkCancel() {
        if (task != null) {
            task.cancel();
        }
    }

    ArrayList<DownloadTask> addTask = new ArrayList<>();
    DownloadTask[] tasks;

    public void downLoadSenceCancle() {
        tasks = new DownloadTask[addTask.size()];
        for (int i = 0; i < addTask.size(); i++) {
            tasks[i] = addTask.get(i);
        }
        DownloadTask.cancel(tasks);
    }


    public void clear() {
        this.fileTotalLength = 0;
        this.increaseLength = 0;
    }


}
