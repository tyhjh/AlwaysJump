package com.yorhp.alwaysjump.app;

import android.app.Application;
import android.os.Environment;

import java.io.File;

import log.LogUtils;

/**
 * 作者：Tyhj on 2018/10/23 23:56
 * 邮箱：tyhj5@qq.com
 * github：github.com/tyhjh
 * description：
 */

public class MyApplication extends Application {

    public static String rootDir, savePointDir, saveChessDir;

    public static boolean isDebug=true;

    @Override
    public void onCreate() {
        super.onCreate();
        initDir();
        LogUtils.init(isDebug,null);
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



    }


    //打印初始化
    public static void log(String key, String value) {

    }

}
