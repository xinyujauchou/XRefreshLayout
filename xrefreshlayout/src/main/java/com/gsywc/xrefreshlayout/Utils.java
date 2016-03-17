package com.gsywc.xrefreshlayout;

import android.content.res.Resources;

import java.util.Calendar;
import java.util.Date;

/**
 * 工具类
 * Created by chao.wang on 2016/3/16.
 */
public class Utils {

    public static String formatFreshTime(long lastFreshTime){
        String lastFreshStr;
        Calendar mCalendarNow = Calendar.getInstance();
        Calendar mCalendarLast = Calendar.getInstance();
        mCalendarLast.setTime(new Date(lastFreshTime));

        if(mCalendarNow.get(Calendar.MONTH) == mCalendarLast.get(Calendar.MONTH) //是否是今天
                && mCalendarNow.get(Calendar.DAY_OF_MONTH) == mCalendarLast.get(Calendar.DAY_OF_MONTH)){
            lastFreshStr = "今天 "+mCalendarLast.get(Calendar.HOUR_OF_DAY)+":"+ mCalendarLast.get(Calendar.MINUTE);
        }else if(mCalendarNow.get(Calendar.YEAR) == mCalendarLast.get(Calendar.YEAR)){ //是今年
            lastFreshStr = mCalendarLast.get(Calendar.MONTH)+"-"+ mCalendarLast.get(Calendar.DAY_OF_MONTH);
        }else{
            lastFreshStr = mCalendarLast.get(Calendar.YEAR)+"-"+mCalendarLast.get(Calendar.MONTH)+
                    "-"+ mCalendarLast.get(Calendar.DAY_OF_MONTH);
        }
        return lastFreshStr;
    }
}
