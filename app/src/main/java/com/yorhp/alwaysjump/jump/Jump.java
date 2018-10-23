package com.yorhp.alwaysjump.jump;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;

import com.yorhp.alwaysjump.app.MyApplication;
import com.yorhp.alwaysjump.util.ColorUtil;
import com.yorhp.alwaysjump.util.FileUitl;
import com.yorhp.alwaysjump.util.TimeUtil;
import com.yorhp.recordlibrary.ScreenRecordUtil;

import log.LogUtils;

/**
 * 作者：Tyhj on 2018/10/24 00:14
 * 邮箱：tyhj5@qq.com
 * github：github.com/tyhjh
 * description：
 */

public class Jump {

    private static String ADB_COMMEND = "input touchscreen swipe 660 1600 660 1600 ";
    public static double chessHeight = 0.4;//截图比例
    public static double chessStart = 0.3;//开始截图的位置
    public static double jumpHeight = 0.35;
    public static double jumpStart = 0.25;
    public static int chessColor = Color.argb(255, 46, 45, 65);
    public static int bitmapWidth = 1080;
    public static int bitmapHeight = 1920;

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
    private Point findNowPoint(Bitmap bitmap) {
        int width = bitmap.getWidth() + 2;
        int height = bitmap.getHeight() - 2;
        int ignorePoint = 10;
        for (int y = height; y > ignorePoint; y = y - ignorePoint) {
            for (int x = 0; x < width - ignorePoint - 2; x = x + ignorePoint) {
                if (ColorUtil.colorLikeChess(bitmap.getPixel(x, y), chessColor)) {//开始密集查找
                    ignorePoint = 1;
                }
                if (ColorUtil.colorLikeChess(bitmap.getPixel(x, y), chessColor) &&
                        ColorUtil.colorLikeChess(bitmap.getPixel(x, y - 1), chessColor) &&
                        ColorUtil.colorLikeChess(bitmap.getPixel(x, y - 2), chessColor) &&
                        ColorUtil.colorLikeChess(bitmap.getPixel(x, y - 3), chessColor) &&
                        ColorUtil.colorLikeChess(bitmap.getPixel(x, y - 4), chessColor) &&
                        ColorUtil.colorLikeChess(bitmap.getPixel(x, y - 5), chessColor) &&
                        ColorUtil.colorLikeChess(bitmap.getPixel(x, y - 6), chessColor) &&
                        ColorUtil.colorLikeChess(bitmap.getPixel(x, y - 7), chessColor) &&
                        ColorUtil.colorLikeChess(bitmap.getPixel(x, y - 8), bitmap.getPixel(x, y + 1)) &&
                        ColorUtil.colorLikeChess(bitmap.getPixel(x, y - 5), bitmap.getPixel(x, y - 5) - 1)) {

                    return new Point(x + 30, y - 8);
                }

            }
        }
        return new Point(0, 0);
    }


    /**
     * 计算距离和按键时间
     *
     * @param startPoint
     * @param jumpPoint
     * @return
     */
    private int getSpendTime(Point startPoint, Point jumpPoint) {
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


}
