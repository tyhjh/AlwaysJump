package com.yorhp.alwaysjump.jump;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;

import com.yorhp.alwaysjump.app.MyApplication;
import com.yorhp.alwaysjump.util.ColorUtil;
import com.yorhp.alwaysjump.util.FileUitl;
import com.yorhp.alwaysjump.util.TimeUtil;
import com.yorhp.alwaysjump.util.color.HsvColorLike;
import com.yorhp.alwaysjump.util.color.LabColorLike;
import com.yorhp.alwaysjump.util.color.RgbColorLike;
import com.yorhp.recordlibrary.ScreenRecordUtil;

import log.LogUtils;

import static com.yorhp.alwaysjump.util.ColorUtil.ABERRATION_BG_LAB;
import static com.yorhp.alwaysjump.util.ColorUtil.ABERRATION_BG_LAB2;
import static com.yorhp.alwaysjump.util.ColorUtil.ABERRATION_CHESS_LAB;
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

    //斜率
    double k = 0.5773;

    //背景颜色
    public int bgColor;

    private static String ADB_COMMEND = "input touchscreen swipe 660 1600 660 1600 ";
    public static double chessHeight = 0.25;//截图比例
    public static double chessStart = 0.4;//开始截图的位置
    public static double jumpHeight = 0.30;
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
    public static int ignorePoint = 5;


    Point startPoint, jumpPoint;

    public void start() {
        TimeUtil.setTime();
        Bitmap bitmap = ScreenRecordUtil.getInstance().getScreenShot();
        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();

        Bitmap chessBitmap = FileUitl.cropBitmapY(bitmap, chessStart, chessHeight);
        TimeUtil.spendTime("截图");

        startPoint = findNowPoint(chessBitmap);
        TimeUtil.spendTime("startPoint");

        setBgColor(chessBitmap.getPixel(0, 0));
        Rect rectChess = new Rect(startPoint.x - 40, (int) (startPoint.y + bitmapHeight * chessStart - 190), startPoint.x + 40, (int) (startPoint.y + bitmapHeight * chessStart + 30));
        FileUitl.drawRect(bitmap, rectChess, bgColor);


        FileUitl.drawPoint(chessBitmap, startPoint.x, startPoint.y);
        FileUitl.bitmapToPath(chessBitmap, MyApplication.saveChessDir + System.currentTimeMillis() + ".png");
        Bitmap jumpBitmap = FileUitl.cropBitmapY(bitmap, jumpStart, jumpHeight);

        TimeUtil.setTime();
        jumpPoint = findJumpPoint(jumpBitmap);
        TimeUtil.spendTime("jumpPoint");

    }

    /**
     * 找到要去的点
     *
     * @param bitmap
     * @return
     */
    private Point findJumpPoint(Bitmap bitmap) {

        Point jumpPoint = null;

        Point topPoint = getTopPoint(bitmap);

        Point leftPoint = getLeftPoint(bitmap, topPoint.x, topPoint.y+5);
        Point rightPoint = getRightPoint(bitmap, topPoint.x, topPoint.y+5);

        LogUtils.e("topPoint：" + topPoint.x + "，" + topPoint.y);
        LogUtils.e("leftPoint：" + leftPoint.x + "，" + leftPoint.y);
        LogUtils.e("rightPoint：" + rightPoint.x + "，" + rightPoint.y);


        double leathLeft =  Math.sqrt(Math.pow(leftPoint.x - topPoint.x, 2) + Math.pow(leftPoint.y - topPoint.y, 2));
        double leathRight = Math.sqrt(Math.pow(rightPoint.x - topPoint.x, 2) + Math.pow(rightPoint.y - topPoint.y, 2));

        if (leathLeft / leathRight > 1.3 || leathRight / leathLeft > 1.3) {
            if (leathLeft > leathRight) {
                jumpPoint = new Point(topPoint.x, leftPoint.y);
            } else {
                jumpPoint = new Point(topPoint.x, rightPoint.y);
            }

        } else {
            jumpPoint = new Point((leftPoint.x + rightPoint.x) / 2, (leftPoint.y + rightPoint.y) / 2);
        }

        Bitmap bitmap1 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        FileUitl.drawPoint(bitmap1, topPoint.x, topPoint.y);

        FileUitl.drawPoint(bitmap1, leftPoint.x, leftPoint.y);
        FileUitl.drawPoint(bitmap1, rightPoint.x, rightPoint.y);
        FileUitl.drawPoint(bitmap1, jumpPoint.x, jumpPoint.y);

        FileUitl.bitmapToPath(bitmap1, MyApplication.savePointDir + System.currentTimeMillis() + ".png");
        return jumpPoint;
    }


    /**
     * 获取左边的点
     *
     * @param bitmap
     * @param startX
     * @param startY
     * @return
     */
    private Point getLeftPoint(Bitmap bitmap, int startX, int startY) {

        int color = bitmap.getPixel(startX, startY);
        boolean isPure = isPure(bitmap, color, startX, startY);
        LogUtils.e("getLeftPoint，isPure：" + isPure);
        for (int x = startX; x > 0; x = x - 3) {
            int y = startY + (int) ((startX - x) * k);
            if (y >= bitmap.getHeight()) {
                return new Point(x + 3, startY - (int) ((startX - x) * k));
            }
            if (isPure) {
                if (!ColorUtil.colorLike(bitmap.getPixel(x, y), color, ABERRATION_BG_LAB, labColorLike)) {
                    return new Point(x, y);
                }
            } else {
                if (isOutLeft(bitmap, x, y)) {
                    return new Point(x, y);
                }
            }
        }
        return new Point(0, startY + (int) ((startX) * k));
    }

    /**
     * 是否从左边出去了
     *
     * @param bitmap
     * @param startX
     * @param startY
     * @return
     */
    private boolean isOutLeft(Bitmap bitmap, int startX, int startY) {
        for (int x = startX-5; x > startX - 10; x--) {
            if (x > 0 && !isLikeBg2(bitmap, x, startY)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 获取右边的点
     *
     * @param bitmap
     * @param startX
     * @param startY
     * @return
     */
    private Point getRightPoint(Bitmap bitmap, int startX, int startY) {
        int width = bitmap.getWidth();
        int color = bitmap.getPixel(startX, startY);
        boolean isPure = isPure(bitmap, color, startX, startY);
        LogUtils.e("getRightPoint，isPure：" + isPure);
        for (int x = startX; x < width; x = x + 3) {
            int y = startY + (int) ((x - startX) * k);
            if (y >= bitmap.getHeight()) {
                return new Point(x + 3, startY - (int) ((x - startX) * k));
            }
            if (isPure) {
                if (!ColorUtil.colorLike(bitmap.getPixel(x, y), color, ABERRATION_BG_LAB, labColorLike)) {
                    return new Point(x, y);
                }
            } else {
                if (isOutRight(bitmap, x, y)) {
                    return new Point(x, y);
                }
            }
        }
        return new Point(width, startY + (int) ((width - startX) * k));
    }

    /**
     * 是否从右边出去了
     *
     * @param bitmap
     * @param startX
     * @param startY
     * @return
     */
    private boolean isOutRight(Bitmap bitmap, int startX, int startY) {
        for (int x = startX+5; x < startX + 10; x++) {
            if (x < bitmap.getWidth() && !isLikeBg2(bitmap, x, startY)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 获取最上面的点
     *
     * @param bitmap
     * @return
     */
    public Point getTopPoint(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        setBgColor(bitmap.getPixel(3, 3));
        int ignorePoint = 40;
        Point firstPoint = null;
        for (int y = 0; y < height; y = y + ignorePoint) {
            for (int x = 0; x < width; x = x + 30) {
                if (isLikeBg(bitmap, x, y)) {
                    if (x == 60) {
                        setBgColor(bitmap.getPixel(x, y));
                    }
                } else {
                    firstPoint = new Point(0, y - ignorePoint);
                    x = width;
                    y = height;
                }

            }
        }


        ignorePoint = 5;
        for (int y = firstPoint.y; y < height; y = y + ignorePoint) {
            for (int x = 0; x < width; x = x + 5) {
                if (isLikeBg(bitmap, x, y)) {
                    if (x == 20) {
                        setBgColor(bitmap.getPixel(x, y));
                    }
                } else {
                    //如果被干扰、🎵、棋子遮挡
                    if (isDisturb(bitmap, x, y)) {
                        x = getOutX(bitmap, x, y);
                    }
                    //计算出去的坐标
                    int centerX = (x + getOutX(bitmap, x, y)) / 2;
                    firstPoint = new Point(centerX, getOutY(bitmap, centerX, y));
                    x = width;
                    y = height;
                }
            }
        }


        return firstPoint;
    }


    //是否是🎵干扰
    private boolean isDisturb(Bitmap bitmap, int startX, int startY) {

        for (int y = startY; y < startY + 20; y = y + ignorePoint) {
            if (isLikeBg(bitmap, startX, y)) {
                return true;
            }
        }

        for (int x = startX; x < startX + 20; x = x + ignorePoint) {
            if (isLikeBg(bitmap, startX, startY + 30)) {
                return true;
            }
        }


        return false;
    }


    /**
     * 找到Y轴再次回到背景颜色的位置
     *
     * @param bitmap
     * @param x
     * @param startY
     * @return
     */
    private int getOutY(Bitmap bitmap, int x, int startY) {
        for (int y = startY; y > 0; y = y - ignorePoint) {
            if (isLikeBg(bitmap, x, y)) {
                return y;
            }
        }
        return 0;
    }

    /**
     * X轴再次回到背景颜色的位置
     *
     * @param bitmap
     * @param startX
     * @param y
     * @return
     */
    private int getOutX(Bitmap bitmap, int startX, int y) {
        int distence = bitmap.getWidth();
        for (int x = startX; x < distence; x = x + ignorePoint) {
            if (isLikeBg(bitmap, x, y)) {
                return x;
            }
        }
        return 0;
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
                if (isLikeChess(bitmap, x, y)) {//开始密集查找
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
                    if (isLikeChess(bitmap, pointX, firstPoint.y - y)) {
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
            if (isLikeChess(bitmap, startX - 1, starty - y)) {
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
        int distence = (int) (Math.sqrt(Math.pow(startPoint.x - jumpPoint.x, 2))
                + Math.pow(startPoint.y + chessStart * bitmapHeight - jumpPoint.y - jumpStart * bitmapHeight, 2));
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

        int color1 = Color.parseColor("#4a4b54");
        int color2 = Color.parseColor("#484848");
        LogUtils.e("HSV颜色空间计算颜色距离：" + hsvAberration(color1, color2));
        LogUtils.e("LAB颜色空间计算色差：" + labAberration(color1, color2));
        rgbAberration(color1, color2);


        //new Jump().findJumpPoint(BitmapFactory.decodeFile(MyApplication.savePointDir + "test.png"));
    }


    private void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }


    private boolean isLikeBg(Bitmap bitmap, int x, int y) {
        return ColorUtil.colorLike(bitmap.getPixel(x, y), bgColor, ABERRATION_BG_LAB, labColorLike);
    }

    private boolean isLikeBg2(Bitmap bitmap, int x, int y) {
        return ColorUtil.colorLike(bitmap.getPixel(x, y), bgColor, ABERRATION_BG_LAB2, labColorLike);
    }

    private boolean isLikeChess(Bitmap bitmap, int x, int y) {
        return ColorUtil.colorLike(bitmap.getPixel(x, y), chessColor, ABERRATION_CHESS_LAB, labColorLike);
    }

    //判读是不是纯色
    private  boolean isPure(Bitmap bitmap, int clr, int x, int y) {
        int height = 8;
        int width = 6;
        for (int i = 1; i < width; i++) {
            if ((x - i > 0) && (ColorUtil.colorLike(bitmap.getPixel(x+i, y+height), clr, ABERRATION_BG_LAB, labColorLike)|| ColorUtil.colorLike(bitmap.getPixel(x-i, y+height), clr, ABERRATION_BG_LAB, labColorLike))) {
                return false;
            }
        }
        return true;
    }


}
