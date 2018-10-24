package com.yorhp.alwaysjump.util;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.yorhp.alwaysjump.util.color.HsvColorLike;
import com.yorhp.alwaysjump.util.color.LabColorLike;
import com.yorhp.alwaysjump.util.color.LikeColor;
import com.yorhp.alwaysjump.util.color.RgbColorLike;

import log.LogUtils;

import static com.yorhp.alwaysjump.util.color.HsvColorLike.hsvAberration;
import static com.yorhp.alwaysjump.util.color.LabColorLike.labAberration;

/**
 * 作者：Tyhj on 2018/10/21 21:05
 * 邮箱：tyhj5@qq.com
 * github：github.com/tyhjh
 * description：
 */

public class ColorUtil {

    HsvColorLike hsvColorLike = new HsvColorLike();
    LabColorLike labColorLike = new LabColorLike();
    RgbColorLike rgbColorLike = new RgbColorLike();


    //棋子的颜色
    public static int chessColor = Color.parseColor("#2e2d41");
    //棋子的可允许色差
    public static int ABERRATION_CHESS_RGB = 10;
    public static int ABERRATION_CHESS_LAB= 20;
    //背景颜色的色容差
    public static int ABERRATION_BG_RGB = 5;
    //杂色板块的色差
    public static int ABERRATION_MOTLEY_HSV = 12;
    public static int ABERRATION_MOTLEY_LAB = 160;

    //略过棋子的色容差
    public static int ABERRATION_CHESS_HSV = 4;
    public static int ABERRATION_CHESS_LAB2= 30;


    /**
     * 对比颜色
     *
     * @param color1
     * @param color2
     * @param aberration
     * @return
     */
    public static boolean colorLike(int color1, int color2, int aberration, LikeColor labLike) {
        return labLike.isLike(color1, color2, aberration);
    }


}
