package com.ihwdz.android.hwslimcore.LogUtil;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */
public class DisplayTextHelper {

    ///////Date Time String Converter//////

    // yyyyMMdd  20180803
    static public String getDateAsName() {
        return getDateAsName(new Date());
    }
    static public String getDateAsName(Date date) {
        return getDateTimeFormattedString(date, "yyyyMMdd", Locale.US);
    }

    // yyyyMMdd-hhmmss  20180803-033555
    static public String getDateTimeAsName() {
        return getDateTimeAsName(new Date());
    }
    static public String getDateTimeAsName(Date date) {
        return getDateTimeFormattedString(date, "yyyyMMdd-hhmmss", Locale.US);
    }

    // "yyyy-MM-dd' 'kk:mm"  2018-08-03 15:35
    static public String getDateTimeDisplayString() {
        return getDateTimeFormattedString(new Date(), "yyyy-MM-dd' 'kk:mm", Locale.US);
    }
    static public String getDateTimeFormattedString(Date date, String template, Locale locale) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(template, locale);
            String dateString = dateFormat.format(date);
            return dateString;
        }catch (Exception e)
        {
            return e.getMessage();
        }
    }

    // "yyyy/MM/dd"  2018/08/03
    static public String getDateTodayString() {
        return getDateTimeFormattedString(new Date(), "yyyy/MM/dd", Locale.CHINESE);
    }


    static public String getStringWithResourceName(Context context, String resourceName) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(resourceName,
                "string", packageName);
        return context.getString(resId);
    }

    public static String getDateFromSeconds(String seconds){
        if(seconds == null)
            return " ";
        else{
            Date date = new Date();
            try{
                date.setTime(Long.parseLong(seconds)*1000);
            }catch(NumberFormatException nfe){
            }
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        }
    }

    public static String getDateFromSeconds(long seconds){
        if(seconds <= 0)
            return " ";
        else{
            Date date = new Date();
            try{
                date.setTime(seconds*1000);
            }catch(NumberFormatException nfe){
            }
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        }
    }
}
