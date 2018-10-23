package com.yorhp.alwaysjump.util;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * 作者：Tyhj on 2018/10/21 21:05
 * 邮箱：tyhj5@qq.com
 * github：github.com/tyhjh
 * description：
 */

public class ColorUtil {

    public static int SAME_COLOR_CLASSICAL = 5;

    public static int SAME_COLOR_CHESS = 15;

    //两种颜色相似
    public static boolean colorLike(int clr, int clr2) {

        int red2 = Color.red(clr2);
        int green2 = Color.green(clr2);
        int blue2 = Color.blue(clr2);

        int red = Color.red(clr); // 取高两位
        int green = Color.green(clr);// 取中两位
        int blue = Color.blue(clr);// 取低两位
        if (red == red2 && green == green2 && blue == blue2) {
            return true;
        }
        if (Math.abs(red - red2) < SAME_COLOR_CLASSICAL && Math.abs(green - green2) < SAME_COLOR_CLASSICAL && Math.abs(blue - blue2) < SAME_COLOR_CLASSICAL) {
            return true;
        }
        return false;
    }

    //两种颜色相似，针对找出棋子所在位置的时候
    public static boolean colorLikeChess(int clr, int clr2) {
        int red2 = Color.red(clr2);
        int green2 = Color.green(clr2);
        int blue2 = Color.blue(clr2);

        int red = Color.red(clr); // 取高两位
        int green = Color.green(clr);// 取中两位
        int blue = Color.blue(clr);// 取低两位

        if (Math.abs(red - red2) < SAME_COLOR_CHESS && Math.abs(green - green2) < SAME_COLOR_CHESS && Math.abs(blue - blue2) < SAME_COLOR_CHESS) {
            return true;
        }
        return false;
    }

    //x轴上的颜色一样
    public static boolean isSameColorOnX(Bitmap bitmap, int x, int y, int distance) {
        for (int i = 0; i < distance; i++) {
            if (!colorLike(bitmap.getPixel(x + i, y), bitmap.getPixel(x + i + 1, y))) {
                return false;
            }
        }
        return true;
    }

    //x轴上的颜色和指定颜色一样
    public static boolean isSameColorOnX(Bitmap bitmap, int color, int x, int y, int distance) {
        for (int i = 0; i < distance; i = i + 2) {
            if (!colorLike(color, bitmap.getPixel(x + i, y))) {
                return false;
            }
        }
        return true;
    }




}
