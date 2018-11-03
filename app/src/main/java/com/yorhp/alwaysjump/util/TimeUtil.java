package com.yorhp.alwaysjump.util;

import log.LogUtils;

/**
 * 作者：Tyhj on 2018/10/24 01:07
 * 邮箱：tyhj5@qq.com
 * github：github.com/tyhjh
 * description：
 */

public class TimeUtil {
    Long currentTime = 0L;

    public void setTime() {
        currentTime = System.currentTimeMillis();
    }

    public void spendTime(String tag) {
        LogUtils.e(tag + "，花费时间为：" + (System.currentTimeMillis() - currentTime));
        setTime();
    }

}
