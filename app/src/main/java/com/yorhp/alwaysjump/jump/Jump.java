package com.yorhp.alwaysjump.jump;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;

import com.yorhp.alwaysjump.app.MyApplication;
import com.yorhp.alwaysjump.util.ColorUtil;
import com.yorhp.alwaysjump.util.FileUitl;
import com.yorhp.alwaysjump.util.TimeUtil;
import com.yorhp.alwaysjump.util.color.HsvColorLike;
import com.yorhp.alwaysjump.util.color.LabColorLike;
import com.yorhp.alwaysjump.util.color.RgbColorLike;
import com.yorhp.recordlibrary.ScreenRecordUtil;

import log.LogUtils;

import static com.yorhp.alwaysjump.util.ColorUtil.ABERRATION_CHESS_LAB;
import static com.yorhp.alwaysjump.util.ColorUtil.ABERRATION_CHESS_RGB;
import static com.yorhp.alwaysjump.util.ColorUtil.chessColor;
import static com.yorhp.alwaysjump.util.color.HsvColorLike.hsvAberration;
import static com.yorhp.alwaysjump.util.color.LabColorLike.labAberration;
import static com.yorhp.alwaysjump.util.color.RgbColorLike.rgbAberration;

/**
 * 作者：Tyhj on 2018/10/24 00:14
 * 邮箱：tyhj5@qq.com
 * github：github.com/tyhjh
 * description：
 */

public class Jump {

    //背景颜色
    public int bgColor;

    private static String ADB_COMMEND = "input touchscreen swipe 660 1600 660 1600 ";
    public static double chessHeight = 0.4;//截图比例
    public static double chessStart = 0.3;//开始截图的位置
    public static double jumpHeight = 0.35;
    public static double jumpStart = 0.25;
    public static int bitmapWidth = 1080;
    public static int bitmapHeight = 1920;

    HsvColorLike hsvColorLike;
    LabColorLike labColorLike;
    RgbColorLike rgbColorLike;

    public Jump() {
        hsvColorLike = new HsvColorLike();
        labColorLike = new LabColorLike();
        rgbColorLike = new RgbColorLike();
    }

    //一次跳过遍历的点
    public static int ignorePoint = 2;

    public void start() {
        Point startPoint, jumpPoint;
        TimeUtil.setTime();
        Bitmap bitmap = ScreenRecordUtil.getInstance().getScreenShot();
        Bitmap chessBitmap = FileUitl.cropBitmapY(bitmap, chessStart, chessHeight);
        TimeUtil.spendTime("截图");
        startPoint = findNowPoint(chessBitmap);
        TimeUtil.spendTime("startPoint");
        FileUitl.drawPoint(chessBitmap, startPoint.x, startPoint.y);
        FileUitl.bitmapToPath(chessBitmap, MyApplication.saveChessDir + System.currentTimeMillis() + ".png");
        Bitmap jumpBitmap = FileUitl.cropBitmapY(bitmap, jumpStart, jumpHeight);
        jumpPoint = findJumpPoint(jumpBitmap);
    }

    /**
     * 找到要去的点
     *
     * @param bitmap
     * @return
     */
    private Point findJumpPoint(Bitmap bitmap) {

        return new Point(0, 0);
    }

    /**
     * 找到当前位置
     *
     * @param bitmap
     * @return
     */
    public Point findNowPoint(Bitmap bitmap) {
        int width = bitmap.getWidth() + 2;
        int height = bitmap.getHeight() - 2;
        int ignorePoint = 5;
        Point firstPoint = null;
        //粗略找到左下角进入棋子的点
        for (int y = height; y > ignorePoint; y = y - ignorePoint) {
            for (int x = 0; x < width - ignorePoint - 2; x = x + ignorePoint) {
                if (ColorUtil.colorLike(bitmap.getPixel(x, y), chessColor, ABERRATION_CHESS_LAB, labColorLike)) {//开始密集查找
                    firstPoint = new Point(x, y);
                    x = width;
                    y = 0;

                }
            }
        }

        //是否位于棋子最左边
        for (int x = 0; x < firstPoint.x - 1; x++) {
            if (isLeftest(firstPoint.x - x, firstPoint.y, bitmap)) {
                int pointX = firstPoint.x - x;
                //找最左，下角的点
                for (int y = 0; y < 50; y++) {
                    if (ColorUtil.colorLike(bitmap.getPixel(pointX, firstPoint.y - y), chessColor, ABERRATION_CHESS_LAB, labColorLike)) {
                        return new Point(pointX + 35, firstPoint.y - y - 8);
                    }
                }
            }
        }
        return new Point(0, 0);
    }


    //这一列的左边颜色是否都与棋子不一样，则找x坐标
    public boolean isLeftest(int startX, int starty, Bitmap bitmap) {
        for (int y = 0; y < 50; y++) {
            if (ColorUtil.colorLike(bitmap.getPixel(startX - 1, starty - y), chessColor, ABERRATION_CHESS_LAB, labColorLike)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 计算距离和按键时间
     *
     * @param startPoint
     * @param jumpPoint
     * @return
     */
    private int getJumpTime(Point startPoint, Point jumpPoint) {
        int distence = (int) (Math.sqrt((startPoint.x - jumpPoint.x) * (startPoint.x - jumpPoint.x)
                + (startPoint.y + chessStart * bitmapHeight - jumpPoint.y - jumpStart * bitmapHeight)
                * (startPoint.y + chessStart * bitmapHeight - jumpPoint.y - jumpStart * bitmapHeight)));
        int time = 0;
        double k = (distence * (-0.00020) + 1.485);
        if (k > 1.416) {
            k = 1.416;
        }
        time = (int) (k * distence);
        LogUtils.e("系数设置为：" + k);
        return time;
    }


    public static void testColor() {


        Bitmap bitmap1 = BitmapFactory.decodeFile(MyApplication.saveChessDir + "test.png");
        TimeUtil.setTime();
        Point point = new Jump().findNowPoint(bitmap1);
        TimeUtil.spendTime("findNowPoint");
        Bitmap bitmap = bitmap1.copy(Bitmap.Config.ARGB_8888, true);
        FileUitl.drawPoint(bitmap, point.x, point.y);
        FileUitl.bitmapToPath(bitmap, MyApplication.saveChessDir + System.currentTimeMillis() + ".png");

        int color1 = Color.parseColor("#373838");
        int color2 = Color.parseColor("#2e2d41");
        LogUtils.e("HSV颜色空间计算颜色距离：" + hsvAberration(color1, color2));
        LogUtils.e("LAB颜色空间计算色差：" + labAberration(color1, color2));
        rgbAberration(color1, color2);
    }


    private void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

}
