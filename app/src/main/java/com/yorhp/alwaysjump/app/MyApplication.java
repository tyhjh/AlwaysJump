package com.yorhp.alwaysjump.app;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Environment;

import com.yorhp.alwaysjump.jump.Jump;
import com.yorhp.alwaysjump.util.FileUitl;
import com.yorhp.crashlibrary.CrashHander;
import com.yorhp.crashlibrary.saveErro.ISaveErro;
import com.yorhp.crashlibrary.saveErro.SaveErroToSDCard;
import com.yorhp.recordlibrary.ScreenRecordUtil;

import java.io.File;

import log.LogUtils;

/**
 * 作者：Tyhj on 2018/10/23 23:56
 * 邮箱：tyhj5@qq.com
 * github：github.com/tyhjh
 * description：
 */

public class MyApplication extends Application {

    public static String rootDir, savePointDir, saveChessDir, gradeDir, crashDir;

    public static boolean isDebug = true;

    @Override
    public void onCreate() {
        super.onCreate();
        initDir();
        LogUtils.init(isDebug, null);
        CrashHander.getInstance().init(this, new ISaveErro() {
            @Override
            public void saveErroMsg(Throwable throwable) {
                new SaveErroToSDCard(crashDir).saveErroMsg(throwable);
                Bitmap bitmap = ScreenRecordUtil.getInstance().getScreenShot();
                FileUitl.bitmapToPath(bitmap, MyApplication.savePointDir + "crash" + System.currentTimeMillis() + ".png");
                for (Bitmap bitmap1 : Jump.bitmapList) {
                    FileUitl.bitmapToPath(bitmap1, MyApplication.savePointDir + "crash_urgent_save" + System.currentTimeMillis() + ".png");
                }
            }
        });
    }


    //文件夹初始化
    public void initDir() {
        rootDir = Environment.getExternalStorageDirectory() + "/AlwaysJump/";
        File f1 = new File(rootDir);
        if (!f1.exists()) {
            f1.mkdirs();
        }

        saveChessDir = rootDir + "chess/";
        File f5 = new File(saveChessDir);
        if (!f5.exists()) {
            f5.mkdirs();
        }

        savePointDir = rootDir + "check/";
        File f6 = new File(savePointDir);
        if (!f6.exists()) {
            f6.mkdirs();
        }

        gradeDir = rootDir + "grade/";
        File f7 = new File(gradeDir);
        if (!f7.exists()) {
            f7.mkdirs();
        }

        crashDir = rootDir + "crash/";
        File f8 = new File(crashDir);
        if (!f8.exists()) {
            f8.mkdirs();
        }

    }

}
