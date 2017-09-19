package djs.com.happysharebuy.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 日期操作工具类
 *
 * @author hbb
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {


    private static final DateFormat FORMATER_DATE = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static final DateFormat FORMATER_MINUTE = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");

    private static final DateFormat FORMATER_DAY = new SimpleDateFormat(
            "yyyyMMdd");
    private static final DateFormat FORMATER_DAY2 = new SimpleDateFormat(
            "yyyy-MM-dd");


    /**
     * 获取当前时间
     */
    public static Date getCurruntDate() {
        Calendar ca = Calendar.getInstance();
        return ca.getTime();
    }

    /**
     * 返回当前时间的"yyyy-MM-dd hh:mm:ss"格式字符串
     */
    public static String currentTime() {
        return FORMATER_DATE.format(new Date());
    }

    /**
     * 返回当前时间的"yyyy-MM-dd hh:mm"格式字符串
     */
    public static String currentMINUTE() {
        return FORMATER_MINUTE.format(new Date());
    }

    /**
     * 返回当前时间的"yyyy-MM-dd hh:mm:ss"格式字符串
     */
    public static String currentTimeDay() {
        return FORMATER_DAY.format(new Date());
    }

    /**
     * 解析日期
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static Date parseTime(String time) throws ParseException {
        Date date = FORMATER_MINUTE.parse(time);
        return date;
    }

    /**
     * 解析日期
     *
     * @param time
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date parseTime(String time, String format)
            throws ParseException {
        DateFormat formater = new SimpleDateFormat(format);
        return formater.parse(time);
    }

    /**
     * 解析日期
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String date) throws ParseException {
        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        return formater.parse(date);
    }

    /**
     * 将Date转为字符串
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static String formatTime(Date date, String formatStr) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat format = new SimpleDateFormat(formatStr, Locale.US);
            String d = format.format(date);
            return d;
        }
    }

    /**
     * Date转为字符串，默认格式
     *
     * @param date
     * @return
     */
    public static String formatTime(Date date) {
        return FORMATER_DATE.format(date);
    }

    /**
     * 把一个日期字符串转换成指定日期格式
     *
     * @param timeStr 日期字符串
     * @param pattern 日期格式
     * @return String
     * @throws ParseException
     */
    public static String formatTime(String timeStr, String pattern)
            throws ParseException {
        return formatTime(parseTime(timeStr), pattern);
    }

    /**
     * 返回当前时间的"yyyy-MM-dd"格式字符串
     */
    public static String currentDate() {
        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        return formater.format(new Date());
    }

    /**
     * 比较两个日期
     */
    public static boolean compareTwoDate(Date date1, Date date2) {
        Calendar ca1 = Calendar.getInstance();
        Calendar ca2 = Calendar.getInstance();
        ca1.setTime(date1);
        ca2.setTime(date2);
        return ca1.after(ca2);
    }

    /**
     * 某个日期和当前时间做比较
     */
    public static boolean compareToCurrent(Date date) {
        Calendar ca1 = Calendar.getInstance();
        Calendar ca2 = Calendar.getInstance();
        ca1.setTime(date);
        return ca1.after(ca2);
    }

    /**
     * 两个时间是否相等
     */
    public static boolean equalTwoDate(Date date1, Date date2) {
        Calendar ca1 = Calendar.getInstance();
        Calendar ca2 = Calendar.getInstance();
        ca1.setTime(date1);
        ca2.setTime(date2);
        return ca1.equals(ca2);
    }

    /**
     * 获得下一星期的日期
     */
    public static Date getNextWeek() {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DAY_OF_YEAR, 7);
        return ca.getTime();
    }

    /**
     * 获得当前日期的移动间隔日期
     */
    public static Date getTheDay(int days) {
        Calendar ca = Calendar.getInstance();
        ca.set(2010, 1, 1);
        ca.add(Calendar.DAY_OF_YEAR, days);
        return ca.getTime();
    }

    public static Date getTheMonth(int months) {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.MONTH, months);
        return ca.getTime();
    }

    /**
     * 获取过去某一时间点与当期相隔的分钟数
     */
    public static long calendarDiffMinutes(String timeStr) {
        if (timeStr == null || timeStr.trim().equals("")) {
            return 0;
        }
        Date date;
        try {
            date = DateUtil.parseTime(timeStr);
            return calendarDiffMinutes(date);
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 计算两个任意时间中间的间隔时间：XX 天X X小时XX 分钟
     */
    public static List<Integer> getDaysBetween(Date date1, Date date2) {
        Calendar startday = Calendar.getInstance();
        Calendar endday = Calendar.getInstance();
        startday.setTime(date1);
        endday.setTime(date2);
        List<Integer> times = new ArrayList<Integer>();
        if (startday.after(endday)) {
            Calendar cal = startday;
            startday = endday;
            endday = cal;
        }
        long sl = startday.getTimeInMillis();
        long el = endday.getTimeInMillis();

        long ei = el - sl;
        int days = (int) (ei / (1000 * 60 * 60 * 24));
        int hours = (int) ((ei % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        int minutes = (int) ((ei % (1000 * 60 * 60)) / (1000 * 60));
        times.add(days);
        times.add(hours);
        times.add(minutes);
        return times;
    }

    /**
     * 获取过去某一时间点与当期相隔的分钟数
     */
    public static long calendarDiffMinutes(Date date) {
        Calendar ca1 = Calendar.getInstance();
        ca1.setTime(date);
        long time = ca1.getTimeInMillis();
        Calendar ca2 = Calendar.getInstance();
        long now = ca2.getTimeInMillis();
        return (now - time) / 60000;
    }

    /**
     * 得到几天前的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static String getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(now.getTime());
    }

    /**
     * 得到几分钟前的时间
     *
     * @param d
     * @param minutes
     * @return
     */
    public static String getMinutesBefore(Date d, int minutes) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) - minutes);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(now.getTime());
    }

    /**
     * 友好显示时间 几天前、几小时前等
     *
     * @param date
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String friendlyTime(String date) {
        if (date == null) {
            return null;
        }
        try {
            // 获取当前时间与date相差的毫秒数
            Date d = parseTime(date);
            Date now = new Date();

            int nowYear = dataFormatIntYear(now);
            int serYear = dataFormatIntYear(d);

            int countYear = nowYear - serYear;
            if (countYear > 0) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                return formatter.format(d);
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date today = cal.getTime();

            long diff = d.getTime() - today.getTime();
            if (diff > 0) {
                // 今天
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                return formatter.format(d);
            } else {
                // 今天以前的时间
                cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) - 1);
                Date yesterday = cal.getTime();
                diff = d.getTime() - yesterday.getTime();
                if (diff > 0) {
                    // 昨天
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    return "昨天 " + formatter.format(d);
                } else {
                    cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) - 1);
                    Date qian = cal.getTime();
                    diff = d.getTime() - qian.getTime();
                    if (diff > 0) {
                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                        return "前天 " + formatter.format(d);
                    } else {
                        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");
                        return formatter.format(d);
                    }
                }
            }
        } catch (Exception e) {
            return date;
        }
    }

    /**
     * 判断两个时间相差是否大于5分钟
     */
    public static boolean calendarFiveMinutes(String date1, String date2) {
        long fiveMinutes = 1000 * 60 * 5;
        try {
            Date d1 = DateUtil.parseTime(date1);
            Date d2 = DateUtil.parseTime(date2);
            return Math.abs(d1.getTime() - d2.getTime()) > fiveMinutes;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 用于判断传来的日期是否是今天以前的日期
     *
     * @param date
     * @return false为是以前的日期true为不是以前的日期
     */
    public static boolean selectDate(String date) {
        String a = "";
        String b = "";
        String c = "";
        if (date == null) {
            return false;
        } else {
            String time[] = date.split("-");
            a = time[0];
            String string1 = time[1];
            int parseInt = Integer.parseInt(string1);
            if (parseInt < 10) {
                b = "0" + parseInt;
            } else {
                b = "" + parseInt;
            }
            String string2 = time[2];
            int parseInt2 = Integer.parseInt(string2);
            if (parseInt2 < 10) {
                c = "0" + parseInt2;
            } else {
                c = "" + parseInt2;
            }
            date = a + "-" + b + "-" + c + " 00:00:00";
        }
        try {
            // 获取当前时间与date相差的毫秒数
            Date d = parseTime(date);
            Date now = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date today = cal.getTime();

            long diff = d.getTime() - today.getTime();
            if (diff >= 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 得到发送时间带时分秒
     *
     * @return
     */
    public static String getDate() {
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1).length() == 1 ? ("0" + String
                .valueOf(c.get(Calendar.MONTH) + 1)) : (String.valueOf(c
                .get(Calendar.MONTH) + 1));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH)).length() == 1 ? ("0" + String
                .valueOf(c.get(Calendar.DAY_OF_MONTH))) : (String.valueOf(c
                .get(Calendar.DAY_OF_MONTH)));
        ;
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY)).length() == 1 ? ("0" + String
                .valueOf(c.get(Calendar.HOUR_OF_DAY))) : (String.valueOf(c
                .get(Calendar.HOUR_OF_DAY)));
        ;
        String mins = String.valueOf(c.get(Calendar.MINUTE)).length() == 1 ? ("0" + String
                .valueOf(c.get(Calendar.MINUTE))) : (String.valueOf(c
                .get(Calendar.MINUTE)));
        String scoends = String.valueOf(c.get(Calendar.SECOND)).length() == 1 ? ("0" + String
                .valueOf(c.get(Calendar.MINUTE))) : (String.valueOf(c
                .get(Calendar.MINUTE)));
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
                + mins + ":" + scoends);
        return sbBuffer.toString();
    }

    /**
     * 得到日期
     *
     * @return
     */
    public static String getDate2() {
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1).length() == 1 ? ("0" + String
                .valueOf(c.get(Calendar.MONTH) + 1)) : (String.valueOf(c
                .get(Calendar.MONTH) + 1));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH)).length() == 1 ? ("0" + String
                .valueOf(c.get(Calendar.DAY_OF_MONTH))) : (String.valueOf(c
                .get(Calendar.DAY_OF_MONTH)));
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "." + month + "." + day);
        return sbBuffer.toString();
    }


    /**
     * 截取年数
     *
     * @param date
     * @return
     */
    public static int dataFormatIntYear(Date date) {
        DateFormat f = new SimpleDateFormat("yyyy");
        String dateStr = f.format(date);

        return Integer.valueOf(dateStr);
    }

    /**
     * 截取年数
     *
     * @param time
     * @return
     */
    public static int dataFormatIntYear(String time) {
        int year = 0;
        try {
            DateFormat formater = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            DateFormat f = new SimpleDateFormat("yyyy");
            String dateStr = f.format(formater.parse(time));

            year = Integer.valueOf(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return year;
    }

    /**
     * 返回当前时间的"yyyy-MM"格式字符串
     */
    public static String currentYearMonth() {
        DateFormat formater = new SimpleDateFormat("yyyy-MM");
        return formater.format(new Date());
    }

    /**
     * 友好显示时间 几天前、几小时前等
     *
     * @param date
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String friendlyTime2(String date) {

        if (date == null) {
            return null;
        }
        try {
            // 获取当前时间与date相差的毫秒数
            Date d = parseTime(date);
            Date now = new Date();

            int nowYear = dataFormatIntYear(now);
            int serYear = dataFormatIntYear(d);

            int countYear = nowYear - serYear;
            if (countYear > 0) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                return formatter.format(d);
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date today = cal.getTime();

            long diff = d.getTime() - today.getTime();
            if (diff > 0) {
                // 今天
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                return formatter.format(d);
            } else {
                // 今天以前的时间
                cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) - 1);
                Date yesterday = cal.getTime();
                diff = d.getTime() - yesterday.getTime();
                if (diff > 0) {
                    // 昨天
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    return "昨天 " + formatter.format(d);
                } else {
                    cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) - 1);
                    Date qian = cal.getTime();
                    diff = d.getTime() - qian.getTime();
                    //					if(diff>0){
                    //						SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    //						return "前天 " + formatter.format(d);
                    //					}else {
                    SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm");
                    return formatter.format(d);
                    //					}
                }
            }
        } catch (Exception e) {
            return date;
        }
    }

    /**
     * 处理返回时间
     *
     * @param date
     * @return
     */
    public static String disposeDate(String date) {
        if (TextUtils.isEmpty(date)) {
            return "";
        }
        if (date.contains("/Date(")) {
            date = date.replace("/Date(", "");
            if (date.contains(")/")) {
                date = date.replace(")/", "");
            }
        }
//        stampToDate(date);
        return stampToDate(date);
    }


    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        long lt = new Long(s);
        Date date = new Date(lt);
        res = FORMATER_DAY2.format(date);
        return res;
    }

    /**
     * 取得当月天数
     */
    public static int getCurrentMonthLastDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

}