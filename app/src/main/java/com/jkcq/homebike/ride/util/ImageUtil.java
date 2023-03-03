package com.jkcq.homebike.ride.util;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.util.Log;

public class ImageUtil {

    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        try {



            // 获取第一个关键帧
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(videoPath);
            bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

            Log.e("getVideoThumbnail",bitmap.getWidth()+""+bitmap.getHeight());

            //mTextView02.setBackground(new BitmapDrawable(bmp));


            // 获取视频的缩略图 最大关键帧
            /* bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind); //調用ThumbnailUtils類的靜態方法createVideoThumbnail獲取視頻的截圖；
             */
            if (bitmap != null) {
                if(bitmap.getWidth()>=bitmap.getHeight()) {
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth(), bitmap.getHeight(),
                            ThumbnailUtils.OPTIONS_RECYCLE_INPUT);//調用ThumbnailUtils類的靜態方法extractThumbnail將原圖片（即上方截取的圖片）轉化為指定大小；
                }else{
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap,  width,height,
                            ThumbnailUtils.OPTIONS_RECYCLE_INPUT);//調用ThumbnailUtils類的靜態方法extractThumbnail將原圖片（即上方截取的圖片）轉化為指定大小；
                }
            }

        }catch (Exception e){

        }
        return bitmap;
    }

}
