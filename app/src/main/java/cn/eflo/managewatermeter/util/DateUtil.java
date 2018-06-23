package cn.eflo.managewatermeter.util;

import android.content.Context;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by billyyoyo on 16-1-27.
 */
public class DateUtil {
    //判断当前时间戳是不是当前日期
    public static boolean isCreatToday(long creatTime) {
        return isThisTime(creatTime, "yyyy-MM-dd");
    }

    private static boolean isThisTime(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);//参数时间
        String now = sdf.format(new Date());//当前时间
        if (param.equals(now)) {
            return true;
        }
        return false;
    }

    public static long getTimeZoneMillis(String date, String... pattern) {
        if (date == null || "".equals(date)) return 0l;
        java.text.DateFormat df = null;
        if (pattern != null && pattern.length > 0) df = new SimpleDateFormat(pattern[0]);
        else df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("PST"));
        try {
            long time = df.parse(date).getTime();
            return time;
        } catch (ParseException e) {
            return 0l;
        }
    }

    public static String getDurationTime(int time) {
        int hour = time / 3600;
        int minute = time % 3600 / 60;
        int second = time % 60;

        String hour_fmt = String.format("%02d", hour);
        String minute_fmt = String.format("%02d", minute);
        String second_fmt = String.format("%02d", second);

        if (hour != 0)
            return hour_fmt + "'" + minute_fmt + "'" + second_fmt;
        else
            return minute_fmt + "'" + second_fmt;

    }

    public static String formatTime(long time, String format) {
        return DateFormat.format(format, time).toString();
    }

    public static String getToday() {
        long time = System.currentTimeMillis();
        return DateFormat.format("yyyy-MM-dd", System.currentTimeMillis()).toString();
    }

    public static int durationYear(String year) {
        if (Util.isInteger(year)) {
            int y1 = Integer.parseInt(year);
            int y2 = Integer.parseInt(DateFormat.format("yyyy", System.currentTimeMillis()).toString());
            return y2 - y1;
        } else {
            return -1;
        }
    }

    public static String getNow() {
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()).toString();
    }

    public static Long getTodayMillis() {
        Calendar cl = Calendar.getInstance();
        cl.set(Calendar.HOUR_OF_DAY, 0);
        cl.set(Calendar.MINUTE, 0);
        cl.set(Calendar.SECOND, 1);
        Date currentDate = cl.getTime();
        return currentDate.getTime();
    }

    public static String getDateTimeAgo(Context context, long nDateTime) {
        Calendar cl = Calendar.getInstance();
        Date currentDate = cl.getTime();     //real time
        Date lastDate = new Date(nDateTime); //target time
        TimeSpan timeSpan = TimeSpan.subtract(currentDate, lastDate);
        int nDay = (int) timeSpan.getDays();
        int nHour = (int) timeSpan.getHours() % 24;
        int nMiniute = (int) timeSpan.getMinutes() % 60;
        int nSeconds = (int) timeSpan.getSeconds() % 60;
        return getDateTimeAgoZh(nDay, nHour, nMiniute, nSeconds);
    }

    public static String getDateTimeAgoZh(int day, int hr, int min, int sec) {
        if (day > 365) {
            return (day / 365) + "年";
        } else if (day > 30) {
            return (day / 30) + "月";
        } else if (day > 0) {
            return day + "日" + hr + "时";
        } else if (hr > 0) { // more than 1 hour
            return hr + "小时" + min + "分钟";
        } else if (min > 0) { //more than 1 min
            return min + "分钟" + sec + "秒";
        } else { // more than 1 sec
            return sec + "秒";
        }
    }

    //Added by Qiao Ding -- Aug,2,2016
    public static String getDateTimeAgoEn(int day, int hr, int min, int sec) {
        if (day > 365) {
            return (day / 365) + "years ";
        } else if (day > 30) {
            return (day / 30) + "months ";
        } else if (day > 0) {
            return day + "d " + hr + "h ";
        } else if (hr > 0) { // more than 1 hour
            return hr + "h " + min + "m ";
        } else if (min > 0) { //more than 1 min
            return min + "m " + sec + "s ";
        } else { // more than 1 sec
            return sec + "s ";
        }
    }
    //-----

    public static String getDateTimeAgo12(Context context, long nDateTime) {
        Calendar cl = Calendar.getInstance();
        cl.set(Calendar.HOUR_OF_DAY, 0);
        cl.set(Calendar.MINUTE, 0);
        cl.set(Calendar.SECOND, 1);
        Date currentDate = cl.getTime();     //当天00:00
        Date lastDate = new Date(nDateTime); //目标时间
        Locale locale = Locale.CHINESE;
        TimeSpan timeSpan = TimeSpan.subtract(currentDate, lastDate);
        int nDay = (int) timeSpan.getDays();
        if (nDateTime >= currentDate.getTime()) {//. Less than one day and display yyyy-mm-dd hh:mm:ss
            return getDateTimeByFormat(lastDate, "aaa h:mm", locale);
        } else if (nDay < 2) {
            return "昨天 " + getDateTimeByFormat(lastDate, "aaa h:mm", locale);
        } else if (nDay > 7) {//. Less than one day and display yyyy-mm-dd hh:mm:ss
            return getDateTimeByFormat(lastDate, "MMM d aaa h:mm", locale);
        }

//        long nLastTime = cl.getTimeInMillis() - nDay * 86400000L;
//        lastDate = new Date(nLastTime);
        return getDateTimeByFormat(lastDate, "EEE aaa h:mm", locale);
    }

    public static String getDateTimeAgo24(Context context, long nDateTime) {
        Calendar cl = Calendar.getInstance();
        cl.set(Calendar.HOUR_OF_DAY, 0);
        cl.set(Calendar.MINUTE, 0);
        cl.set(Calendar.SECOND, 1);
        Date currentDate = cl.getTime();     //当天00:00
        Date lastDate = new Date(nDateTime); //目标时间
        TimeSpan timeSpan = TimeSpan.subtract(currentDate, lastDate);
        int nDay = (int) timeSpan.getDays();
        Locale locale = Locale.CHINESE;
        if (nDateTime >= currentDate.getTime()) {//. Less than one day and display yyyy-mm-dd hh:mm:ss
            return getDateTimeByFormat(lastDate, "HH:mm", locale);
        } else if (nDay < 2) {
            return getDateTimeByFormat(lastDate, "HH:mm", locale) + "昨天";
        } else if (nDay > 7) {//. Less than one day and display yyyy-mm-dd hh:mm:ss
            return getDateTimeByFormat(lastDate, "MMM d HH:mm", locale);
        }

//        long nLastTime = cl.getTimeInMillis() - nDay * 86400000L;
//        lastDate = new Date(nLastTime);
        return getDateTimeByFormat(lastDate, "EEE HH:mm", locale);
    }


    public static String getDateTimeByFormat(Date d, String strFormat, Locale locale) {
    /*
     Symbol   Meaning                 Presentation        Example
     ------   -------                 ------------        -------
     G        era designator          (Text)              AD
     y        year                    (Number)            1996
     M        month in year           (Text & Number)     July & 07
     d        day in month            (Number)            10
     h        hour in am/pm (1~12)    (Number)            12
     H        hour in day (0~23)      (Number)            0
     m        minute in hour          (Number)            30
     s        second in minute        (Number)            55
     S        millisecond             (Number)            978
     E        day in week             (Text)              Tuesday
     D        day in year             (Number)            189
     F        day of week in month    (Number)            2 (2nd Wed in July)
     w        week in year            (Number)            27
     W        week in month           (Number)            2
     a        am/pm marker            (Text)              PM
     k        hour in day (1~24)      (Number)            24
     K        hour in am/pm (0~11)    (Number)            0
     z        time zone               (Text)              Pacific Standard Time
    '        escape for text         (Delimiter)
     ''       single quote            (Literal)           '

     Format Pattern                         Result
     --------------                         -------
     "yyyy.MM.dd G 'at' hh:mm:ss z"    ->>  1996.07.10 AD at 15:08:56 PDT
     "EEE, MMMM d, ''yy"                ->>  Wed, July 10, '96
     "h:mm a"                          ->>  12:08 PM
     "hh 'o''clock' a, zzzz"           ->>  12 o'clock PM, Pacific Daylight Time
     "K:mm a, z"                       ->>  0:00 PM, PST
     "yyyyy.MMMMM.dd GGG hh:mm aaa"    ->>  1996.July.10 AD 12:08 PM
     "M/d/yyyy H:m:s                   ->>   7/17/2001 19:22:20
     "EEEE, MMMM d, yyyy, hh:mm aaa"       ->>  Thursday, January 05, 2012 6:45 PM

    */
        SimpleDateFormat formatter = new SimpleDateFormat(strFormat, locale);//("yyyy.MM.dd G 'at' hh:mm:ss a zzz");
        return formatter.format(d);
    }

    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String d = df.format(new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(d + " " + dateString + ":00");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static long getTomorrowDate(String dateString, String pattern) {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String tomorrowDateString = formatter.format(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            date = dateFormat.parse(tomorrowDateString + " " + dateString + ":00");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static long[] getTimeDifference(long timeLong) {
        long s = timeLong / 1000 % 60;
        long m = timeLong / 1000 / 60 % 60;
        long h = timeLong / 1000 / 60 / 60 % 24;
        long[] time = {0, h, m, s};
        return time;
    }

    public static int getCurrentHour() {
        int hour = 0;
        Calendar c = Calendar.getInstance();
        long h = c.get(Calendar.HOUR_OF_DAY);
        hour = (int) h;
        return hour;
    }

    public static long setTime(long min) {
        long h;
        String time = null;
        String mins = null;
        Calendar c = Calendar.getInstance();
        long m = c.get(Calendar.MINUTE);
//        if (m >= min) {
//            h = c.get(Calendar.HOUR_OF_DAY) + 1;
//        } else {
//            h = c.get(Calendar.HOUR_OF_DAY);
//        }
        h = c.get(Calendar.HOUR_OF_DAY);

        if (min >= 10) {
            mins = (min - 1) + "";
        } else {
            mins = "0" + (min - 1);
        }
        if (h > 9) {
            time = h + ":" + mins;
        } else {
            time = "0" + h + ":" + mins;
        }
        return getStringToDate(time, "yyyy-MM-dd HH:mm:ss");
    }

}
