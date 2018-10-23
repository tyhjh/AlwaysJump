package com.yorhp.alwaysjump.util;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 作者：Tyhj on 2018/10/21 21:51
 * 邮箱：tyhj5@qq.com
 * github：github.com/tyhjh
 * description：
 */

public class AdbUtil {

    static Process process = null;
    static DataOutputStream os = null;
    static ProcessBuilder processBuilder=new ProcessBuilder();

    public static void execShellCmd(String cmd) {
        try {
            if (process == null) {
                process = Runtime.getRuntime().exec("su");
                os = new DataOutputStream(process.getOutputStream());
            }
            os.writeBytes(cmd+"\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void execShellCmd2(String[] cmd){

        try {
            processBuilder.command(cmd);
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void adbClick(int x, int y) {
        String msg = "input swipe " + x + " " + y+" "+x+1+" "+y+1+" "+10;
        /*String[] order = {
                "input",
                "tap",
                "" + x,
                "" + y
        };*/
        execShellCmd(msg);
    }

}
