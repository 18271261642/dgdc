package com.jkcq.util;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;

import androidx.core.view.ViewCompat;


public class AnimationUtil {

    private static final int SPEED = 1000;

    public static void rotateView(View view) {
        ObjectAnimator AnimatorRotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        AnimatorRotate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // TODO Auto-generated method stub
                Log.e("animatin", animation.getAnimatedValue() + "");
            }

        });
        AnimatorRotate.setDuration(500);
        AnimatorRotate.start();

    }

    public static void rotateYView(View view) {
        ObjectAnimator AnimatorRotate = ObjectAnimator.ofFloat(view, "rotationY", 0f, 180f);
        AnimatorRotate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // TODO Auto-generated method stub
                Log.e("animatin", animation.getAnimatedValue() + "");
            }

        });
        AnimatorRotate.setDuration(500);
        AnimatorRotate.start();

    }


    public static void showView(View view) {
       /* ViewCompat.animate(view)
                .alpha(1f)
                .setDuration(400)
                .setListener(null);*/
    }

    /**
     * 放大缩小
     */
    public static void ScaleUpView(View view) {
        ViewCompat.animate(view)
                .setDuration(200)
                .scaleX(1.2f)
                .scaleY(1.2f)
                .start();
    }

    public static void ScaleDownView(View view) {
        ViewCompat.animate(view)
                .setDuration(200)
                .scaleX(1f)
                .scaleY(1f)
                .start();
    }
}
