package com.jkcq.homebike.ali;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.jkcq.util.DateUtil;


/*
 * PopupWindow
 * @author mhj
 * Create at 2017/11/6 11:29
 */
public class PopupWindowUtils implements PopupWindow.OnDismissListener {

    private Context context;

    private PopupWindow popupWindow;

    public void initPopupWindow(Context context, View contentView) {
        this.context = context;
        if (null == popupWindow) {
            popupWindow = new PopupWindow(context);
            popupWindow.setContentView(contentView);
            popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setBackgroundDrawable(null);
            // popupWindow.setAnimationStyle(R.style.pop_anim_style);
            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(false);
            popupWindow.setTouchable(true);
            //设置监听
            popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        //如果焦点不在popupWindow上，且点击了外面，不再往下dispatch事件：
                        //不做任何响应,不 dismiss popupWindow
                        return true;
                    }
                    //否则default，往下dispatch事件:关掉popupWindow，
                    return false;
                }
            });
            popupWindow.update();
        }

    }

    public void setPopupBackground(int color) {
        if (null == popupWindow) {
            return;
        }
        popupWindow.setBackgroundDrawable(new ColorDrawable(color));
        popupWindow.update();
    }

    public void setPopupWindowWidth(int width) {
        if (null == popupWindow) {
            return;
        }
        popupWindow.setWidth(width);
        popupWindow.update();
    }

    public void setPopupWindowHeight(int height) {
        if (null == popupWindow) {
            return;
        }
        popupWindow.setHeight(height);
        popupWindow.update();
    }

    private int flags;

    public void setBackgroundAlpha(boolean isAlpha) {
        if (!(context instanceof Activity)) {
            return;
        }
//        Window window = ((Activity) context).getWindow();
//        WindowManager.LayoutParams layoutParams = window.getAttributes();
//        if (isAlpha) {
////            layoutParams.alpha = 0.5f; //0.0-1.0
//            flags = layoutParams.flags;
//            layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
////            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        } else {
////            layoutParams.alpha = 1f; //0.0-1.0
////            layoutParams.flags = flags;
//        }
//        window.setAttributes(layoutParams);
    }

    public void setAnimationStyle(int animationStyle) {
        if (null == popupWindow) {
            return;
        }
        popupWindow.setAnimationStyle(animationStyle);
        popupWindow.update();
    }

    public void showPopupWindow(View parent, int gravity, int dx, int dy) {
        if (popupWindow != null && !popupWindow.isShowing()) {
            popupWindow.showAtLocation(parent, gravity, dx, dy);
            popupWindow.setOnDismissListener(this);
        }
    }

    public void showAsDropDownPopupWindow(View parent, int gravity, int dx, int dy) {
        if (null == popupWindow || popupWindow.isShowing()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 24) {
            int[] location = new int[2];
            parent.getLocationOnScreen(location);
            // 7.1 版本处理
            if (Build.VERSION.SDK_INT == 25) {
                int screenHeight = DateUtil.getScreenWidth(context);
                popupWindow.setHeight(screenHeight - location[1] - parent.getHeight() - dy);
            }
            popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, location[0] - parent.getWidth() - dx, location[1] + parent.getHeight() + dy);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            popupWindow.showAsDropDown(parent, dx, dy, gravity);
        } else {
            popupWindow.showAsDropDown(parent, dx, dy);
        }
        popupWindow.setOnDismissListener(this);
    }

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    public void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    public void showAsDropDownPopupWindowRight(View parent, int gravity, int dx, int dy, int offset) {
        if (null == popupWindow || popupWindow.isShowing()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            int[] location = new int[2];
            parent.getLocationOnScreen(location);
            // 7.1 版本处理
            if (Build.VERSION.SDK_INT == 25) {
                int screenHeight = DateUtil.getScreenHeight(context);
                popupWindow.setHeight(screenHeight - location[1] - parent.getHeight() - dy);
            }
            popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, location[0] - offset, location[1] + parent.getHeight() + dy);
            //popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 0, location[1] + parent.getHeight() + dy);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            popupWindow.showAsDropDown(parent, dx, dy, gravity);
        } else {
            popupWindow.showAsDropDown(parent, dx, dy);
        }
        popupWindow.setOnDismissListener(this);
    }


    @Override
    public void onDismiss() {
        setBackgroundAlpha(false);
    }


}