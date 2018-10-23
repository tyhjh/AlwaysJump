package com.yorhp.alwaysjump.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.yorhp.alwaysjump.R;
import com.yorhp.alwaysjump.jump.Jump;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        createWindowView();
        showNotification(getApplicationContext(), 0, "AlwaysJump", "程序正在运行中");
    }

    WindowManager.LayoutParams params3;
    WindowManager windowManager;
    ImageView btnView3;
    public static final int FLAG_LAYOUT_INSET_DECOR = 0x00000200;

    private void createWindowView() {
        btnView3 = new ImageView(getApplicationContext());
        btnView3.setImageResource(R.drawable.ic_star);

        windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        params3 = new WindowManager.LayoutParams();

        // 设置Window Type
        params3.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        // 设置悬浮框不可触摸
        params3.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | FLAG_LAYOUT_INSET_DECOR;
        // 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应
        params3.format = PixelFormat.RGBA_8888;

        // 设置悬浮框的宽高
        params3.width = 200;
        params3.height = 200;
        params3.gravity = Gravity.TOP;
        params3.x = 300;
        params3.y = 200;


        btnView3.setOnTouchListener(new View.OnTouchListener() {

            //保存悬浮框最后位置的变量
            int lastX, lastY;
            int paramX, paramY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = params3.x;
                        paramY = params3.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        params3.x = paramX + dx;
                        params3.y = paramY + dy;
                        // 更新悬浮窗位置
                        windowManager.updateViewLayout(btnView3, params3);
                        break;
                }
                return false;
            }
        });

        btnView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //btnView3.setVisibility(View.INVISIBLE);

                jump.start();
            }
        });
        windowManager.addView(btnView3, params3);
    }

    Jump jump=new Jump();

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

}
