package com.jkcq.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * Created by Bravo  图片加载的基类。
 */
public class LoadImageUtil {

    private static LoadImageUtil instance;

    public static LoadImageUtil getInstance() {
        if (null == instance) {
            synchronized (LoadImageUtil.class) {
                if (null == instance) {
                    instance = new LoadImageUtil();
                }
            }
        }
        return instance;
    }

    public void downLoadVideo(Context ctx) {
        String picUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555243545061&di=26dfcd1e30fad29adc2fb2ba06a042c3&imgtype=0&src=http%3A%2F%2Fs7.sinaimg.cn%2Forignal%2F0063R5gqzy7maPm9Z4y46%26690";
        try {
            File file = Glide.with(ctx).downloadOnly().load(picUrl).submit().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void loadCirc(Context ctx, String url, final ImageView iv, int roundCirc) {
        if (ctx == null) {
            return;
        }
        try {


            /**
             * 圆角图片 new RoundedCornersTransformation 参数为 ：半径 , 外边距 , 圆角方向(ALL,BOTTOM,TOP,RIGHT,LEFT,BOTTOM_LEFT等等)
             */
            MultiTransformation mation5 = new MultiTransformation(
                    new RoundedCornersTransformation(roundCirc, 0, RoundedCornersTransformation.CornerType.ALL));
            RequestOptions myOptions = new RequestOptions().transform(new GlideCerterTransformation(ctx, roundCirc));

            Glide.with(ctx)
                    .load(url)
                    .apply(myOptions
                            // 圆角图片
                    ).into(iv);
            // .apply(RequestOptions.bitmapTransform(mation5).centerCrop())


           /* Glide.with(ctx).load(url)
                    .centerCrop()
                    .placeholder(R.drawable.icon_defs)
                    .transform(new CenterCrop(ctx), new GlideRoundTransform(ctx, roundCirc))
                    .error(R.drawable.icon_defs)
                    .dontAnimate()
                    *//*   .skipMemoryCache(false)
                       .priority(Priority.LOW)
                       .diskCacheStrategy(DiskCacheStrategy.ALL)*//*
                    .into(iv);
               *//* .into(new SimpleTarget<GlideDrawable>() { // 加上这段代码 可以解决
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        iv.setImageDrawable(resource); //显示图片
                    }
                });*/
        } catch (Exception e) {

        }
    }

    public void loadCirc(Context ctx, int url, final ImageView iv, int roundCirc) {
        if (ctx == null) {
            return;
        }
        try {


            /**
             * 圆角图片 new RoundedCornersTransformation 参数为 ：半径 , 外边距 , 圆角方向(ALL,BOTTOM,TOP,RIGHT,LEFT,BOTTOM_LEFT等等)
             */
            MultiTransformation mation5 = new MultiTransformation(
                    new RoundedCornersTransformation(roundCirc, 0, RoundedCornersTransformation.CornerType.ALL));
            RequestOptions myOptions = new RequestOptions().transform(new GlideCerterTransformation(ctx, roundCirc));

            Glide.with(ctx)
                    .load(url)
                    .apply(myOptions
                            // 圆角图片
                    ).into(iv);
            // .apply(RequestOptions.bitmapTransform(mation5).centerCrop())


           /* Glide.with(ctx).load(url)
                    .centerCrop()
                    .placeholder(R.drawable.icon_defs)
                    .transform(new CenterCrop(ctx), new GlideRoundTransform(ctx, roundCirc))
                    .error(R.drawable.icon_defs)
                    .dontAnimate()
                    *//*   .skipMemoryCache(false)
                       .priority(Priority.LOW)
                       .diskCacheStrategy(DiskCacheStrategy.ALL)*//*
                    .into(iv);
               *//* .into(new SimpleTarget<GlideDrawable>() { // 加上这段代码 可以解决
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        iv.setImageDrawable(resource); //显示图片
                    }
                });*/
        } catch (Exception e) {

        }
    }

    public void load(Context ctx, String url, final ImageView iv, final int defIcon) {
        if (ctx == null) {
            return;
        }
        try {
            RequestOptions options = new RequestOptions();
            options.centerCrop().placeholder(defIcon).error(defIcon).dontAnimate();
            Glide.with(ctx.getApplicationContext()).load(url).apply(options).into(iv);
        } catch (Exception e) {

        }

    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        try {
            // 获得图片的宽高
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            // 计算缩放比例
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 得到新的图片
            Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            return newBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Bitmap getCirleBitmap(Context context, Bitmap bmpSrc) {
        //获取bmp的宽高 小的一个做为圆的直径r
//        int w = bmp.getWidth();
//        int h = bmp.getHeight();
//        int r = Math.min(w, h);

        //固定大小
        int r = 50;
        Bitmap bmp = zoomBitmap(bmpSrc, DateUtil.dip2px(18), DateUtil.dip2px(18));

        int w = bmp.getWidth();
        int h = bmp.getHeight();
        r = Math.min(w, h);
        if (r == 0) {
            r = 50;
        }
        //创建一个paint
        Paint paint = new Paint();
        paint.setAntiAlias(true);


        //新创建一个Bitmap对象newBitmap 宽高都是r
        Bitmap newBitmap = Bitmap.createBitmap(r, r, Bitmap.Config.ARGB_8888);

        //创建一个使用newBitmap的Canvas对象
        Canvas canvas = new Canvas(newBitmap);

        //canvas画一个圆形
        canvas.drawCircle(r / 2, r / 2, r / 2, paint);

        //然后 paint要设置Xfermode 模式为SRC_IN 显示上层图像（后绘制的一个）的相交部分
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //canvas调用drawBitmap直接将bmp对象画在画布上 因为paint设置了Xfermode，所以最终只会显示这个bmp的一部分 也就
        //是bmp的和下层圆形相交的一部分圆形的内容
        canvas.drawBitmap(bmp, 0, 0, paint);

        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(1);
        borderPaint.setColor(context.getResources().getColor(R.color.common_view_color));
        borderPaint.setAntiAlias(true);

        //添加边框
        canvas.drawCircle(r / 2, r / 2, r / 2 - 1, borderPaint);

        return newBitmap;
    }


    public static Bitmap combineBitmap(boolean alpha, Bitmap background, Bitmap foreground, int jishu) {
        if (background == null) {
            return null;
        }
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        int fgWidth = foreground.getWidth();
        int fgHeight = foreground.getHeight();


        Bitmap newmap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newmap);

        if (alpha) {
            // 建立Paint 物件
            Paint vPaint = new Paint();
            vPaint.setStyle(Paint.Style.STROKE);   //空心
            vPaint.setAlpha(75);   //
            canvas.drawBitmap(background, 0, 0, vPaint);
        } else {
            canvas.drawBitmap(background, 0, 0, null);
        }
        canvas.drawBitmap(foreground, (bgWidth - fgWidth) / 2,
                (bgHeight - fgHeight) / jishu, null);
        canvas.save();
        canvas.restore();


        return newmap;
    }

    public void loadDownload(final boolean alpha, final Context ctx, final String url, final String userId, final int resId, final int jishu, final int def) {

        Log.e("loadDownload", "url=" + url + "userId=" + userId);
        if (TextUtils.isEmpty(url)) {
            return;
        }

        ThreadPoolUtils.getInstance().addTask(new Runnable() {
            @Override
            public void run() {
                if (ctx == null) {
                    return;
                }
                try {
                    Bitmap imageFile = null;

                    try {
                        RequestOptions myOptions = new RequestOptions().priorityOf(Priority.HIGH).onlyRetrieveFromCache(true).transform(new GlideCerterTransformation(ctx, DateUtil.dip2px(20)));
                        imageFile = Glide.with(ctx.getApplicationContext()).asBitmap()
                                .apply(myOptions)
                                .load(url).error(def).into(DateUtil.dip2px(18), DateUtil.dip2px(18)).get();
                        Log.e("loadDownload", "" + imageFile + "url=" + url);
                        //  if(IConfig.DEBUG) Log.d("XXX", "found the file:" + imageFile.getAbsolutePath());

                    } catch (Exception e) {
                        imageFile = null;
                        Log.e("loadDownload", "" + imageFile + "url=" + url + "e.printStackTrace()=" + e.toString());

                    } finally {
                        if (imageFile == null) {
                            Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.friend_icon_default_photo);
                            imageFile = getCirleBitmap(ctx, bitmap);
                            Bitmap background = BitmapFactory.decodeResource(ctx.getResources(), resId);
                            Bitmap newBitmap = combineBitmap(alpha, background, imageFile, jishu);
                            UserBitmapManager.bitmapLoactionHashMap.put(userId, newBitmap);
                        } else {
                            Bitmap background = BitmapFactory.decodeResource(ctx.getResources(), resId);
                            Bitmap newBitmap = combineBitmap(alpha, background, imageFile, jishu);
                            UserBitmapManager.bitmapHashMap.put(userId, newBitmap);
                        }

                    }

                } catch (Exception e) {

                }
            }
        });
    }

    public void loadDownload(final boolean alpha, final Context ctx, final int url, final String userId, final int resId, final int jishu, final int def) {

        Log.e("loadDownload", "url=" + url + "userId=" + userId);
        ThreadPoolUtils.getInstance().addTask(new Runnable() {
            @Override
            public void run() {
                if (ctx == null) {
                    return;
                }
                try {
                    Bitmap imageFile = null;

                    try {
                        RequestOptions myOptions = new RequestOptions().priorityOf(Priority.HIGH).onlyRetrieveFromCache(true).transform(new GlideCerterTransformation(ctx, DateUtil.dip2px(20)));
                        imageFile = Glide.with(ctx.getApplicationContext()).asBitmap()
                                .apply(myOptions)
                                .load(url).error(def).into(DateUtil.dip2px(18), DateUtil.dip2px(18)).get();
                        Log.e("loadDownload", "" + imageFile + "url=" + url);
                        //  if(IConfig.DEBUG) Log.d("XXX", "found the file:" + imageFile.getAbsolutePath());

                    } catch (Exception e) {
                        imageFile = null;
                        Log.e("loadDownload", "" + imageFile + "url=" + url + "e.printStackTrace()=" + e.toString());

                    } finally {
                        if (imageFile != null) {
                            Bitmap background = BitmapFactory.decodeResource(ctx.getResources(), resId);
                            Bitmap newBitmap = combineBitmap(alpha, background, imageFile, jishu);
                            UserBitmapManager.bitmapHashMap.put(userId, newBitmap);
                        }

                    }

                } catch (Exception e) {

                }
            }
        });
    }

    public void load(Context ctx, int url, final ImageView iv) {
        if (ctx == null) {
            return;
        }
        try {
            RequestOptions options = new RequestOptions();
            options.centerInside().dontAnimate();
            Glide.with(ctx.getApplicationContext()).load(url).apply(options).into(iv);
           /* Glide.with(ctx).load(url)
                    .centerCrop()
                    .placeholder(R.drawable.icon_defs)
                    .error(R.drawable.icon_defs)
                    .dontAnimate()
                    *//*   .skipMemoryCache(false)
                       .priority(Priority.LOW)
                       .diskCacheStrategy(DiskCacheStrategy.ALL)*//*
                    .into(iv);
               *//* .into(new SimpleTarget<GlideDrawable>() { // 加上这段代码 可以解决
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        iv.setImageDrawable(resource); //显示图片
                    }
                });*/
        } catch (Exception e) {

        }

    }

   /* public void loadCover(Context ctx, String url, final ImageView iv) {
        if (ctx == null) {
            return;
        }
        try {
            Glide.with(ctx).asBitmap().load(url).addListener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    int imageWidth = resource.getWidth();
                    int imageHeight = resource.getHeight();

                    Logger.e("loadCover", "imageWidth=" + imageWidth + ",imageHeight=" + imageHeight);

                    // int height = ScreenUtils.getScreenWidth() * imageHeight / imageWidth;
                    ViewGroup.LayoutParams para = iv.getLayoutParams();
                    if (para != null) {
                        if (imageWidth >= imageHeight) {
                            RequestOptions options = new RequestOptions();
                            options.centerInside().dontAnimate();
                            Glide.with(BaseApp.getApp()).load(url).apply(options).into(iv);
                            *//*para.height = imageHeight;
                            para.width = ViewGroup.LayoutParams.MATCH_PARENT;
                            iv.setLayoutParams(para);*//*
                        } else {
                            RequestOptions options = new RequestOptions();
                            options.centerCrop().dontAnimate();
                            Glide.with(BaseApp.getApp()).load(url).apply(options).into(iv);
                         *//*   para.height = ViewGroup.LayoutParams.MATCH_PARENT;
                            para.width = ViewGroup.LayoutParams.MATCH_PARENT;
                            iv.setLayoutParams(para);*//*
                        }

                    }

                }
            });
           *//* Glide.with(ctx).load(url)
                    .centerCrop()
                    .placeholder(R.drawable.icon_defs)
                    .error(R.drawable.icon_defs)
                    .dontAnimate()
                    *//**//*   .skipMemoryCache(false)
                       .priority(Priority.LOW)
                       .diskCacheStrategy(DiskCacheStrategy.ALL)*//**//*
                    .into(iv);
               *//**//* .into(new SimpleTarget<GlideDrawable>() { // 加上这段代码 可以解决
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        iv.setImageDrawable(resource); //显示图片
                    }
                });*//*
        } catch (Exception e) {

        }

    }*/

    public void loadCover(Context ctx, String url, final ImageView iv) {
        if (ctx == null) {
            return;
        }
        try {
            Glide.with(ctx.getApplicationContext()).asBitmap().load(url).addListener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    int imageWidth = resource.getWidth();
                    int imageHeight = resource.getHeight();


                    // int height = ScreenUtils.getScreenWidth() * imageHeight / imageWidth;
                    ViewGroup.LayoutParams para = iv.getLayoutParams();
                    if (para != null) {
                        if (imageWidth >= imageHeight) {
                            RequestOptions options = new RequestOptions();
                            options.centerInside().dontAnimate();
                            // Glide.with(BaseApp.getApp()).load(url).apply(options).into(iv);
                            /*para.height = imageHeight;
                            para.width = ViewGroup.LayoutParams.MATCH_PARENT;
                            iv.setLayoutParams(para);*/
                        } else {
                            RequestOptions options = new RequestOptions();
                            options.centerCrop().dontAnimate();
                            // Glide.with(BaseApp.getApp()).load(url).apply(options).into(iv);
                         /*   para.height = ViewGroup.LayoutParams.MATCH_PARENT;
                            para.width = ViewGroup.LayoutParams.MATCH_PARENT;
                            iv.setLayoutParams(para);*/
                        }

                    }

                }
            });
           /* Glide.with(ctx).load(url)
                    .centerCrop()
                    .placeholder(R.drawable.icon_defs)
                    .error(R.drawable.icon_defs)
                    .dontAnimate()
                    *//*   .skipMemoryCache(false)
                       .priority(Priority.LOW)
                       .diskCacheStrategy(DiskCacheStrategy.ALL)*//*
                    .into(iv);
               *//* .into(new SimpleTarget<GlideDrawable>() { // 加上这段代码 可以解决
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        iv.setImageDrawable(resource); //显示图片
                    }
                });*/
        } catch (Exception e) {

        }

    }

    public void loadPlayer(Context ctx, String url, final ImageView iv, final int defIcon) {
        if (ctx == null) {
            return;
        }
        try {
            RequestOptions options = new RequestOptions();
            options.centerCrop().placeholder(defIcon).error(defIcon).dontAnimate();
            Glide.with(ctx.getApplicationContext()).load(url).apply(options).into(iv);
           /* Glide.with(ctx).load(url)
                    .centerCrop()
                    .placeholder(R.drawable.bg_no_play)
                    .error(R.drawable.bg_no_play)
                    .dontAnimate()
                    *//*   .skipMemoryCache(false)
                       .priority(Priority.LOW)
                       .diskCacheStrategy(DiskCacheStrategy.ALL)*//*
                    .into(iv);
               *//* .into(new SimpleTarget<GlideDrawable>() { // 加上这段代码 可以解决
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        iv.setImageDrawable(resource); //显示图片
                    }
                });*/
        } catch (Exception e) {

        }
    }

    public void loadCover(Context ctx, String url, final ImageView iv, int height, int with) {
        if (ctx == null) {
            return;
        }
        try {

            //MultiTransformation mation4 = new MultiTransformation(new BlurTransformation(90));
           /* RequestOptions options3 = new RequestOptions();
            options3.centerCrop().bi;*/

            /*RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .bitmapTransform(mation4);*/

           /* Glide.with(ctx)
                    .load(url)
                    .apply(RequestOptions.bitmapTransform(mation4).override(with, height))
                    // .apply(RequestOptions.bitmapTransform(mation4))
                    .into(iv);
*/
            Glide.with(ctx)
                    .load(url).apply(new RequestOptions()
                    .transform(new GlideBlurTransformation(ctx))).into(iv);


            /*RequestOptions options = new RequestOptions();
            options.centerCrop().dontAnimate();
            Glide.with(ctx).load(url).apply(options).into(iv);*/

           /* Glide.with(this).load(R.drawable.demo)
                    .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
                    .into(imageView)*/


           /* Glide.with(ctx).load(url)
                    .centerCrop()
                    .dontAnimate()
                    *//*   .skipMemoryCache(false)
                       .priority(Priority.LOW)
                       .diskCacheStrategy(DiskCacheStrategy.ALL)*//*
                    .into(iv);
               *//* .into(new SimpleTarget<GlideDrawable>() { // 加上这段代码 可以解决
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        iv.setImageDrawable(resource); //显示图片
                    }
                });*/
        } catch (Exception ex) {

        }
    }

    public void load(Context ctx, int url, final ImageView iv, int roundCirc) {
        if (ctx == null) {
            return;
        }
        try {


            /**
             * 圆角图片 new RoundedCornersTransformation 参数为 ：半径 , 外边距 , 圆角方向(ALL,BOTTOM,TOP,RIGHT,LEFT,BOTTOM_LEFT等等)
             */
            MultiTransformation mation5 = new MultiTransformation(
                    new RoundedCornersTransformation(roundCirc, 0, RoundedCornersTransformation.CornerType.ALL));
            RequestOptions myOptions = new RequestOptions().transform(new GlideCerterTransformation(ctx, roundCirc));

            Glide.with(ctx.getApplicationContext())
                    .load(url)
                    .apply(myOptions
                            // 圆角图片
                    ).into(iv);
            // .apply(RequestOptions.bitmapTransform(mation5).centerCrop())


           /* Glide.with(ctx).load(url)
                    .centerCrop()
                    .placeholder(R.drawable.icon_defs)
                    .transform(new CenterCrop(ctx), new GlideRoundTransform(ctx, roundCirc))
                    .error(R.drawable.icon_defs)
                    .dontAnimate()
                    *//*   .skipMemoryCache(false)
                       .priority(Priority.LOW)
                       .diskCacheStrategy(DiskCacheStrategy.ALL)*//*
                    .into(iv);
               *//* .into(new SimpleTarget<GlideDrawable>() { // 加上这段代码 可以解决
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        iv.setImageDrawable(resource); //显示图片
                    }
                });*/
        } catch (Exception e) {

        }
    }


    public static boolean isDestroy(Activity mActivity) {
        if (mActivity == null || mActivity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * @Description: 加载本地文件方式
     */
    public static void displayImagePath(Context activity, String path, final ImageView imageView) {
        if (isDestroy((Activity) activity)) {
            return;
        }
        try {
            RequestOptions options = new RequestOptions();
            options.centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE);
            Glide.with(activity).
                    load("file://" + path).apply(options)
                    .into(imageView);
        } catch (Exception e) {

        }
    }


    public void load(Context ctx, String url, ImageView iv, int placeholderResId, int errorResId) {
        if (isDestroy((Activity) ctx)) {
            return;
        }
        try {
            RequestOptions options = new RequestOptions();
            options.centerCrop()
                    .placeholder(errorResId)
                    .error(errorResId)
                    .dontAnimate();
            Glide.with(ctx)
                    .load(url)
                    .apply(options)
                    .into(iv);
        } catch (Exception e) {

        }
    }


    public void loadGif(Context ctx, String url, final ImageView imageView, final int defbg) {
        if (isDestroy((Activity) ctx)) {
            return;
        }
        try {

            RequestOptions options = new RequestOptions();
            options.centerCrop()
                    .placeholder(defbg)
                    .error(defbg)
            ;
            Glide.with(ctx)
                    .load(url)
                    .apply(options)
                    .into(imageView);

            //  Glide.with(ctx).load(url).placeholder(R.drawable.bg_no_play).error(R.drawable.bg_no_play).into(new GlideDrawableImageViewTarget(imageView, 100)); //加载一次
        } catch (Exception e) {

        }

    }

    public void loadGif(Context ctx, int url, final ImageView imageView) {
        if (ctx == null) {
            return;
        }


        if (isDestroy((Activity) ctx)) {
            return;
        }
        try {

            RequestOptions myOptions = new RequestOptions().transform(new GlideCerterTransformation(ctx, DateUtil.dip2px(85)));
            Glide.with(ctx)
                    .load(url)
                    .apply(myOptions)
                    .into(imageView);

            //  Glide.with(ctx).load(url).placeholder(R.drawable.bg_no_play).error(R.drawable.bg_no_play).into(new GlideDrawableImageViewTarget(imageView, 100)); //加载一次
        } catch (Exception e) {

        }

    }


}