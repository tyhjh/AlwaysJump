package com.yorhp.alwaysjump.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.yorhp.alwaysjump.app.MyApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;

    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/CrashTest/log/";

    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".trace";

    private static CrashHandler sCrashHandler = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;


    private CrashHandler(){}

    // 单例模式
    public static CrashHandler getInstance(){
        return sCrashHandler;
    }

    public void init(Context context){
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);   //这两句代码不是很明白，有清楚的希望指教下
        mContext = context.getApplicationContext();
    }

    /**
     * 这是最关键的函数，当程序中有未捕获的异常，系统将会自动调用此方法
     * @param thread  为出现未捕获异常的线程
     * @param exception 未捕获的异常，有了此异常，我们就能得到异常信息
     */
    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        try {
            //保存异常信息到sd卡
            saveExceptionToSDCard(exception);
            //上传异常信息到服务器
            uploadExceptionToServer(exception);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 如果系统提供了默认的异常处理器，就交给系统自己处理，否则就自己结束掉自己
        if (mDefaultCrashHandler!=null){
            mDefaultCrashHandler.uncaughtException(thread,exception);
        }else {

            //System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    // 将异常信息保存到SDCard
    private void saveExceptionToSDCard(Throwable ex) throws IOException {
        // 如果SD卡不存在或无法使用，则无法写入异常信息，给与提示
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            if (DEBUG){
                Log.w(TAG,"sdcard unmounted , skip save exception"); //sd卡未安装好，跳出存储异常
            }
            return;
        }
        // 文件存储路径
        File dir = new File(PATH);
        if (!dir.exists()){
            dir.mkdirs();   // 这里开始自己犯了一个低级错误，写成了dir.mkdir()，需要注意；
        }
        // 获取当前时间
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
        // 创建存储异常信息的文件
        File file = new File(MyApplication.crashDir +FILE_NAME+time+FILE_NAME_SUFFIX);
        if (!file.exists()){
            file.createNewFile();
        }

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            savePhoneInfo(pw);
            pw.println();
            ex.printStackTrace(pw);   //输出异常信息
            pw.close();
        }catch (PackageManager.NameNotFoundException e){
            Log.e(TAG,"save crash info failed");
        }

    }

    // 保存手机的信息
    private void savePhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),PackageManager.GET_ACTIVITIES);

        // APP的版本信息
        pw.print("APP Version:");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);

        // Android 手机版本号
        pw.print("OS Version:");
        pw.print(Build.VERSION.RELEASE);
        pw.print('_');
        pw.println(Build.VERSION.SDK_INT);

        // 手机制造商
        pw.print("Vendor:");
        pw.println(Build.MANUFACTURER);

        // 手机型号
        pw.print("Model:");
        pw.println(Build.MODEL);

        // CPU架构
        pw.print("CUP ABI:");
        pw.println(Build.CPU_ABI);
    }

    // 将异常信息上传到服务器
    private void uploadExceptionToServer(Throwable ex){
        //Error error = new Error(ex.getMessage());
        // 上传服务器的操作还不清楚，希望有会的指教下      
    }
}