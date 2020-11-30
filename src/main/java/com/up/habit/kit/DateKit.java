package com.up.habit.kit;

import java.util.Date;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/1/8 11:17
 */
public class DateKit extends com.jfinal.ext.kit.DateKit {

    /**
     * @return
     */
    public static String getDatePoor(Date firstDate, Date lastDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long diff = lastDate.getTime() - firstDate.getTime();
        long day = diff / nd;
        long hour = diff % nd / nh;
        long min = diff % nd % nh / nm;
        return day + "天" + hour + "小时" + min + "分钟";
    }
}
