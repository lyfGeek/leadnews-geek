package com.geek.crawler.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String DATE_FORMAT = "yyyy-MM-dd";

    public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // 2019年07月09日
    public static String DATE_FORMAT_CHINESE = "yyyy年M月d日";
    // 2019年07月09日 14:00:32
    public static String DATE_TIME_FORMAT_CHINESE = "yyyy年M月d日 HH:mm:ss";

    /**
     * 获取当前日期。
     *
     * @return
     */
    public static String getCurrentDate() {
        String datestr = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.DATE_FORMAT);
        datestr = simpleDateFormat.format(new Date());
        return datestr;
    }

    /**
     * 获取当前日期时间。
     *
     * @return
     */
    public static String getCurrentDateTime() {
        String datestr = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT);
        datestr = simpleDateFormat.format(new Date());
        return datestr;
    }

    /**
     * 获取当前日期时间。
     *
     * @return
     */
    public static String getCurrentDateTime(String dateformat) {
        String datestr = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateformat);
        datestr = simpleDateFormat.format(new Date());
        return datestr;
    }

    public static String dateToDateTime(Date date) {
        String datestr = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT);
        datestr = simpleDateFormat.format(date);
        return datestr;
    }

    /**
     * 将字符串日期转换为日期格式。
     *
     * @param datestr
     * @return
     */
    public static Date stringToDate(String datestr) {

        if (datestr == null || datestr.equals("")) {
            return null;
        }
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.DATE_FORMAT);
        try {
            date = simpleDateFormat.parse(datestr);
        } catch (ParseException e) {
            date = DateUtils.stringToDate(datestr, "yyyyMMdd");
        }
        return date;
    }

    /**
     * 将字符串日期转换为日期格式。
     * 自定义格式。
     *
     * @param datestr
     * @return
     */
    public static Date stringToDate(String datestr, String dateFormat) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        try {
            date = simpleDateFormat.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将日期格式日期转换为字符串格式。
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        String datestr = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.DATE_FORMAT);
        datestr = simpleDateFormat.format(date);
        return datestr;
    }

    /**
     * 将日期格式日期转换为字符串格式。自定义格式。
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static String dateToString(Date date, String dateFormat) {
        String datestr = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        datestr = simpleDateFormat.format(date);
        return datestr;
    }

    /**
     * 获取日期的 DAY 值。
     *
     * @param date 输入日期。
     * @return
     */
    public static int getDayOfDate(Date date) {
        int d = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        d = calendar.get(Calendar.DAY_OF_MONTH);
        return d;
    }

    /**
     * 获取日期的 MONTH 值。
     *
     * @param date 输入日期。
     * @return
     */
    public static int getMonthOfDate(Date date) {
        int m = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        m = calendar.get(Calendar.MONTH) + 1;
        return m;
    }

    /**
     * 获取日期的 YEAR 值。
     *
     * @param date 输入日期。
     * @return
     */
    public static int getYearOfDate(Date date) {
        int y = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        y = calendar.get(Calendar.YEAR);
        return y;
    }

    /**
     * 获取星期几。
     *
     * @param date 输入日期。
     * @return
     */
    public static int getWeekOfDate(Date date) {
        int wd = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        wd = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return wd;
    }

    /**
     * 获取输入日期的当月第一天。
     *
     * @param date 输入日期。
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获得输入日期的当月最后一天。
     *
     * @param date
     */
    public static Date getLastDayOfMonth(Date date) {
        return DateUtils.addDay(DateUtils.getFirstDayOfMonth(DateUtils.addMonth(date, 1)), -1);
    }

    /**
     * 判断是否是闰年。
     *
     * @param date 输入日期
     * @return 是 ~ true。否 ~ false。
     */
    public static boolean isLeapYEAR(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);

        return year % 4 == 0 && year % 100 != 0 | year % 400 == 0;
    }

    /**
     * 根据整型数表示的年月日，生成日期类型格式。
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return
     */
    public static Date getDateByYMD(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.getTime();
    }

    /**
     * 获取年周期对应日。
     *
     * @param date  输入日期。
     * @param iyear 年数 ~ 负數表示之前。
     * @return
     */
    public static Date getYearCycleOfDate(Date date, int iyear) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.YEAR, iyear);

        return calendar.getTime();
    }

    /**
     * 获取月周期对应日。
     *
     * @param date 输入日期。
     * @param i
     * @return
     */
    public static Date getMonthCycleOfDate(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.MONTH, i);

        return calendar.getTime();
    }

    /**
     * 计算 fromDate 到 toDate 相差多少年。
     *
     * @param fromDate
     * @param toDate
     * @return 年数。
     */
    public static int getYearByMinusDate(Date fromDate, Date toDate) {
        Calendar simpleDateFormat = Calendar.getInstance();
        simpleDateFormat.setTime(fromDate);

        Calendar dt = Calendar.getInstance();
        dt.setTime(toDate);

        return dt.get(Calendar.YEAR) - simpleDateFormat.get(Calendar.YEAR);
    }

    /**
     * 计算 fromDate 到 toDate 相差多少个月。
     *
     * @param fromDate
     * @param toDate
     * @return 月数。
     */
    public static int getMonthByMinusDate(Date fromDate, Date toDate) {
        Calendar simpleDateFormat = Calendar.getInstance();
        simpleDateFormat.setTime(fromDate);

        Calendar dt = Calendar.getInstance();
        dt.setTime(toDate);

        return dt.get(Calendar.YEAR) * 12 + dt.get(Calendar.MONTH) - (simpleDateFormat.get(Calendar.YEAR) * 12 + simpleDateFormat.get(Calendar.MONTH));
    }

    /**
     * 计算 fromDate 到 toDate 相差多少天。
     *
     * @param fromDate
     * @param toDate
     * @return 天数
     */
    public static long getDayByMinusDate(Object fromDate, Object toDate) {
        Date f = DateUtils.chgObject(fromDate);

        Date t = DateUtils.chgObject(toDate);

        long fd = f.getTime();
        long td = t.getTime();

        return (td - fd) / (24L * 60L * 60L * 1000L);
    }

    /**
     * 计算年龄。
     *
     * @param birthday 生日日期。
     * @param calcDate 要计算的日期点。
     * @return
     */
    public static int calcAge(Date birthday, Date calcDate) {
        int cYear = DateUtils.getYearOfDate(calcDate);
        int cMonth = DateUtils.getMonthOfDate(calcDate);
        int cDay = DateUtils.getDayOfDate(calcDate);
        int bYear = DateUtils.getYearOfDate(birthday);
        int bMonth = DateUtils.getMonthOfDate(birthday);
        int bDay = DateUtils.getDayOfDate(birthday);

        if (cMonth > bMonth || (cMonth == bMonth && cDay > bDay)) {
            return cYear - bYear;
        } else {
            return cYear - 1 - bYear;
        }
    }

    /**
     * 从身份证中获取出生日期。
     *
     * @param idno 身份证号码。
     * @return
     */
    public static String getBirthDayFromIDCard(String idno) {
        Calendar calendar = Calendar.getInstance();
        if (idno.length() == 15) {
            calendar.set(Calendar.YEAR, Integer.valueOf("19" + idno.substring(6, 8))
                    .intValue());
            calendar.set(Calendar.MONTH, Integer.valueOf(idno.substring(8, 10))
                    .intValue() - 1);
            calendar.set(Calendar.DAY_OF_MONTH,
                    Integer.valueOf(idno.substring(10, 12)).intValue());
        } else if (idno.length() == 18) {
            calendar.set(Calendar.YEAR, Integer.valueOf(idno.substring(6, 10))
                    .intValue());
            calendar.set(Calendar.MONTH, Integer.valueOf(idno.substring(10, 12))
                    .intValue() - 1);
            calendar.set(Calendar.DAY_OF_MONTH,
                    Integer.valueOf(idno.substring(12, 14)).intValue());
        }
        return DateUtils.dateToString(calendar.getTime());
    }

    /**
     * 在输入日期上增加（+）或减去（-）天数。
     *
     * @param date 输入日期。
     * @param iday 要增加或减少的天数。
     */
    public static Date addDay(Date date, int iday) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.add(Calendar.DAY_OF_MONTH, iday);

        return calendar.getTime();
    }

    public static Date addHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.add(Calendar.HOUR_OF_DAY, hours);

        return calendar.getTime();
    }


    /**
     * 在输入日期上增加（+）或减去（-）月份。
     *
     * @param date   输入日期。
     * @param imonth 要增加或减少的月分数。
     */
    public static Date addMonth(Date date, int imonth) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.add(Calendar.MONTH, imonth);

        return calendar.getTime();
    }

    /**
     * 在输入日期上增加（+）或减去（-）年份。
     *
     * @param date  输入日期。
     * @param iyear 要增加或减少的年数。
     */
    public static Date addYear(Date date, int iyear) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.add(Calendar.YEAR, iyear);

        return calendar.getTime();
    }

    /**
     * 将 OBJECT 类型转换为 Date。
     *
     * @param date
     * @return
     */
    public static Date chgObject(Object date) {

        if (date != null && date instanceof Date) {
            return (Date) date;
        }

        if (date != null && date instanceof String) {
            return DateUtils.stringToDate((String) date);
        }

        return null;
    }

    public static long getAgeByBirthday(String date) {

        Date birthday = stringToDate(date, "yyyy-MM-dd");
        long sec = new Date().getTime() - birthday.getTime();

        long age = sec / (1000 * 60 * 60 * 24) / 365;

        return age;
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        //String temp = DateUtil.dateToString(getLastDayOfMonth(new Date()),
        ///   DateUtil.DATE_FORMAT_CHINESE);
        //String s=DateUtil.dateToString(DateUtil.addDay(DateUtil.addYear(new Date(),1),-1));

        Date date = DateUtils.addHours(new Date(), 1);
        System.err.println(DateUtils.dateToDateTime(date));
    }

}
