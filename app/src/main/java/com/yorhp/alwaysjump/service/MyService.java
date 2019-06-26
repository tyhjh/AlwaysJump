package com.yorhp.alwaysjump.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;

import com.yorhp.alwaysjump.R;
import com.yorhp.alwaysjump.app.Const;
import com.yorhp.alwaysjump.jump.Jump;
import com.yorhp.alwaysjump.jump.OnJump;

import toast.ToastUtil;

public class MyService extends AccessibilityService {
    public MyService() {
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        jump.setOnJump(new OnJump() {
            @Override
            public void jumpStart(int x, int y, int duration) {
                clickOnScreen(x, y, duration, null);
            }
        });
        createWindowView();
        showNotification(getApplicationContext(), 0, "AlwaysJump", "程序正在运行中");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


    WindowManager.LayoutParams params;
    WindowManager windowManager;
    ImageView btnView;
    public static final int FLAG_LAYOUT_INSET_DECOR = 0x00000200;

    private void createWindowView() {
        btnView = new ImageView(getApplicationContext());
        btnView.setImageResource(R.drawable.ic_star);
        windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        // 设置Window Type
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        // 设置悬浮框不可触摸
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | FLAG_LAYOUT_INSET_DECOR;
        // 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应
        params.format = PixelFormat.RGBA_8888;
        // 设置悬浮框的宽高
        params.width = 200;
        params.height = 200;
        params.gravity = Gravity.TOP;
        params.x = 300;
        params.y = 200;


        btnView.setOnTouchListener(new View.OnTouchListener() {

            //保存悬浮框最后位置的变量
            int lastX, lastY;
            int paramX, paramY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = params.x;
                        paramY = params.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        params.x = paramX + dx;
                        params.y = paramY + dy;
                        // 更新悬浮窗位置
                        windowManager.updateViewLayout(btnView, params);
                        break;
                }
                return false;
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //btnView3.setVisibility(View.INVISIBLE);
                ToastUtil.toast(MyService.this, "go go go!!!");
                if (Jump.start_model >= Const.RUN_MODEL_TEST_PIC) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            jump.start();
                        }
                    }).start();
                    return;
                }

                if (!Jump.start) {
                    Jump.start = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            jump.start();
                        }
                    }).start();
                } else {
                    ToastUtil.toast(MyService.this, "已暂停");
                    Jump.start = false;
                }
            }
        });
        windowManager.addView(btnView, params);
    }

    Jump jump = new Jump();


    private void showNotification(Context context, int id, String title, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_triangle);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setAutoCancel(false);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setVisibility(Notification.VISIBILITY_SECRET);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
        startForeground(id, notification);
    }

    private void clickOnScreen(float x, float y, int duration, AccessibilityService.GestureResultCallback callback) {
        Path path = new Path();
        path.moveTo(x, y);
        gestureOnScreen(path, 0, duration, callback);
    }

    private void gestureOnScreen(Path path, long startTime, long duration,
                                 AccessibilityService.GestureResultCallback callback) {
        GestureDescription.Builder builde = new GestureDescription.Builder();
        builde.addStroke(new GestureDescription.StrokeDescription(path, startTime, duration));
        GestureDescription gestureDescription = builde.build();
        dispatchGesture(gestureDescription, callback, null);
    }

}
