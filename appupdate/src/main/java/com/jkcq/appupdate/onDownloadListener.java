package com.jkcq.appupdate;

/*
 *
 *
 * @author mhj
 * Create at 2019/3/26 14:21
 */
public interface onDownloadListener {
    void onStart(float length);

    void onProgress(long currntSize, long toalSize);
    void onProgress(float progress);

    void onComplete(String upgradeId, String path);
    void onComplete();

    void onFail();
}
