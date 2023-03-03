package com.xugaoxiang.vlcdemo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.math.BigDecimal;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private LibVLC libvlc = null;
    private MediaPlayer mediaPlayer = null;
    private IVLCVout ivlcVout;
    private Media media;


    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        textView = (TextView) findViewById(R.id.text);



        initPlayer();
    }

    private float rate = 1.0f;

    public void onClick(View v) {
        Log.e(TAG, "rate = " + mediaPlayer.getRate());
        BigDecimal b1 = new BigDecimal(Float.toString(rate));
        BigDecimal b2 = new BigDecimal(Float.toString(0.05f));
        if (v.getId() == R.id.min) {
            rate = b1.subtract(b2).floatValue();

            if (rate < 0) {
                rate = 0.05f;
            }
        } else if (v.getId() == R.id.max) {
            rate = b1.add(b2).floatValue();
            if (rate > 4.0) {
                rate = 4.0f;
            }
        }
        textView.setText("" + Float.toString(rate));
        mediaPlayer.setRate(rate);
    }

    private void initPlayer() {
        ArrayList<String> options = new ArrayList<>();
//        options.add("--aout=opensles");
        //options.add("--audio-time-stretch");
        options.add("-vvv");
        options.add("--no-audio"); //关掉声音
        options.add("--file-caching=1500");
        options.add("--sout-mux-caching=1500");//输出缓存
//        options.add(":codec=mediacodec,iomx,all");

//        options.add("--key-faster=2");
//        options.add("--no-drop-late-frames");
        options.add("--skip-frames");

        libvlc = new LibVLC(MainActivity.this, options);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        mediaPlayer = new MediaPlayer(libvlc);

        //media = new Media(libvlc, Uri.parse("http://live.hkstv.hk.lxdns.com/live/hks/playlist.m3u8"));

        // take live555 as RTSP server
//        media = new Media(libvlc, Uri.parse("http://ivi.bupt.edu.cn/hls/cctv3hd.m3u8"));
        media = new Media(libvlc, "/sdcard/bike/videodir/LuSaiEn_5min_xiao四.mp4");
        mediaPlayer.setMedia(media);

        ivlcVout = mediaPlayer.getVLCVout();
        ivlcVout.setVideoView(surfaceView);
        ivlcVout.attachViews();
        ivlcVout.addCallback(new IVLCVout.Callback() {
            @Override
            public void onSurfacesCreated(IVLCVout vlcVout) {
                int sw = getWindow().getDecorView().getWidth();
                int sh = getWindow().getDecorView().getHeight();

                if (sw * sh == 0) {
                    Log.e(TAG, "Invalid surface size");
                    return;
                }

               /* mediaPlayer.getVLCVout().setWindowSize(sw, sh);
                mediaPlayer.setAspectRatio("16:9");
                mediaPlayer.setScale(0);*/
//                mediaPlayer.setRate(2.0f);

//                libvlc.runVLCCommand(MainActivity.this,"--key-faster==2");
            }

            @Override
            public void onSurfacesDestroyed(IVLCVout vlcVout) {

            }
        });

        mediaPlayer.setEventListener(new MediaPlayer.EventListener() {
            @Override
            public void onEvent(MediaPlayer.Event event) {
                Log.i(TAG, "onEvent: type = " + event.type);
                switch (event.type) {
                    case MediaPlayer.Event.Buffering:
                        if (event.getBuffering() >= 100.0f) {
                            Log.i(TAG, "onEvent: buffer success...");
                            mediaPlayer.play();
                        } else {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.pause();
                            }
                            Log.e(TAG, "缓冲: event.getBuffering() = " + event.getBuffering());
                        }
                        break;
                    case MediaPlayer.Event.Playing:
                        Log.i(TAG, "onEvent: playing...");
                        break;
                    case MediaPlayer.Event.Stopped:
                        Log.i(TAG, "onEvent: stop...");
                        Toast.makeText(MainActivity.this, "播放停止！", Toast.LENGTH_LONG).show();
                        break;
                    case MediaPlayer.Event.EncounteredError:
                        Log.i(TAG, "onEvent: error...");
                        mediaPlayer.stop();
                        Toast.makeText(MainActivity.this, "播放出错！", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
        mediaPlayer.play();
    }
}