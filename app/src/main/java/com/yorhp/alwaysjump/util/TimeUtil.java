package com.yorhp.alwaysjump.util;

import log.LogUtils;

/**
 * 作者：Tyhj on 2018/10/24 01:07
 * 邮箱：tyhj5@qq.com
 * github：github.com/tyhjh
 * description：
 */

public class TimeUtil {
    static Long currentTime = 0L;

    public static void setTime() {
        currentTime = System.currentTimeMillis();
    }

    public static void spendTime(String tag) {
        LogUtils.e(tag + "，花费时间为：" + (System.currentTimeMillis() - currentTime));
        setTime();
    }

}
