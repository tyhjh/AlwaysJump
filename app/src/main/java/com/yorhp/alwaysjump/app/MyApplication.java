package com.yorhp.alwaysjump.app;

import android.app.Application;
import android.os.Environment;

import java.io.File;

/**
 * 作者：Tyhj on 2018/10/23 23:56
 * 邮箱：tyhj5@qq.com
 * github：github.com/tyhjh
 * description：
 */

public class MyApplication extends Application {

    public static String rootDir,savePointDir,saveChessDir;

    @Override
    public void onCreate() {
        super.onCreate();
        initDir();
    }


    //文件夹初始化
    public void initDir() {
        rootDir = Environment.getExternalStorageDirectory() + "/AlwaysJump/";
        File f1 = new File(rootDir);
        if (!f1.exists()) {
            f1.mkdirs();
        }

        saveChessDir=rootDir+ "chess/";
        File f5 = new File(saveChessDir);
        if (!f5.exists()) {
            f5.mkdirs();
        }

        savePointDir=rootDir + "check/";
        File f6 = new File(savePointDir);
        if (!f6.exists()) {
            f6.mkdirs();
        }


        File f9 = new File(rootDir + "grade/");
        if (!f9.exists()) {
            f9.mkdirs();
        }

        File f7 = new File(rootDir + "opencv_me/");
        if (!f7.exists()) {
            f7.mkdirs();
        }

    }


    //打印初始化
    public static void log(String key, String value) {

    }

}
