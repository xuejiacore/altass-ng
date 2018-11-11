/*
 * <p>Title:DateUtil.java </p>
 * <p>Company: Scho</p>
 * @author Maxwell Lee
 * @version 1.0
 * @time: 2015年6月15日
 */
package org.chim.altass.base.utils.type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Maxwell Lee
 */
@SuppressWarnings("all")
public class DateUtil {
    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);

    public static final String DEFAULT_PATTERN = "yyyyMMdd";

    public static final SimpleDateFormat SDFYYYYMMDDHHmmSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat SDFYYYYMMDD = new SimpleDateFormat("yyyyMMddHHmmss");

    public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat(DEFAULT_PATTERN);

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param dateStr 时间字符串 yyyy-MM-dd HH:mm:ss
     * @return 返回日期时间类型
     * @author Maxwell Lee
     * @time: 2015年8月24日
     */
    public static Date FromStr(String dateStr) {
        return FromStr(dateStr, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * add by wangtongtong
     *
     * @param dateStr 需要解析的时间字符串
     * @return 返回解析完成的日期时间
     * @time: 2016年4月13日
     */
    public static Date FromStr(String dateStr, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);

        if (dateStr == null) {
            return null;
        }

        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            log.error(String.format("解释日期字符串(%s)异常错误：", dateStr), e);
            return null;
        }
    }

    /**
     * 解析日期时间 yyyyMMddHHmmss 为Date类型
     *
     * @param dateStr 需要解析的日期时间，必须为 yyyyMMddHHmmss格式
     * @return 返回解析后的Date 日期时间类型
     * @author Maxwell Lee
     * @time: 2015年8月24日
     */
    public static Date FromStrCompat(String dateStr) {
        return FromStr(dateStr, "yyyyMMddHHmmss");
    }

    /**
     * 解析时间将日期格式化为YYYYMMDD的整型形式
     *
     * @param date 需要格式化的日期时间数据
     * @return 返回格式化为YYYYMMDD的整型形式
     */
    public static int getDateFormatYyyyMmDd(Date date) {
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat(DEFAULT_PATTERN);
        return Integer.parseInt(yyyyMMdd.format(date));
    }

    /**
     * 解析Long类型时间戳
     *
     * @param date 解析出来的日期时间类型
     * @return 返回解析完成的日期时间
     */
    public static Date parseDate(Long date) {
        return new Date(date);
    }

    /**
     * 获得格式或后的整型形式的日期
     *
     * @param date 需要解析的时间戳
     * @return 返回格式化后的整型形式的日期
     */
    public static int getDateFormatYyyyMmDd(Long date) {
        return getDateFormatYyyyMmDd(new Date(date));
    }

    /**
     * 跟据参数获得对应的日期Date
     *
     * @param year  年
     * @param month 月
     * @param date  日
     * @return 返回对应年/月/日的Date 实例
     */
    public static Date getDateByYearMonthDate(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);
        return calendar.getTime();
    }

    /**
     * @param type 跟据类型获得时间戳对应的时间字符串
     * @param time 时间戳
     * @return 返回格式化后的时间字符串
     */
    public static String getTime(String type, long time) {
        if (time == 0) {
            return "11-01 15:35";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(type);
        return formatter.format(new Date(time));
    }

    /**
     * 将日期类型转化为对应格式化模式后的字符串
     *
     * @param date    需要转化的日期
     * @param pattern 格式化字符串
     * @return 返回转化后的日期字符串
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat fastDateFormat = new SimpleDateFormat(pattern);
        return fastDateFormat.format(date);
    }

    public static String format(Date date, SimpleDateFormat dateFormat) {
        return dateFormat.format(date);
    }

    /**
     * 将日期字符串跟据格式化模式解析为日期实例
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date parseDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.length() == 0) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取给定日期若干天后的日期
     *
     * @param date
     * @param days
     * @return
     */
    public static Date getDateAfterDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static Date parseDateDefaultNow(String dateStr, String pattern) {
        if (dateStr == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 计算当月第一天0点的时间
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 计算当前月的最后一天，23:59:59
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 当月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        // 下个月第一天
        calendar.add(Calendar.MONTH, 1);

        // 减少一天
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 计算下个月第一天0点的时间
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);// 增加一个月
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static String formatDateFromIntToString(int dateInt) {
        Date date = parseDate(dateInt + "", "yyyyMMdd");
        if (date == null) {
            return "";
        }
        return format(date, "yyyy.M.d");
    }

    /**
     * 获得给定日期当月第一天0点的Date对象
     *
     * @return
     */
    public static Date getCurrentMonthStartDate(Date date) {
        return getFirstDayOfMonth(date);
    }

    /**
     * 获得给定日期当月最后一天23点59分59秒的Date对象
     *
     * @param date
     * @return
     */
    public static Date getCurrentMonthEndDate(Date date) {
        return getLastDayOfMonth(date);
    }

    /**
     * 获得给定日期上个月第一天0点的Date对象
     *
     * @param date
     * @return
     */
    public static Date getLastMonthStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 获得给定日期上个月最后一天23点59分59秒的Date对象
     *
     * @param date
     * @return
     */
    public static Date getLastMonthEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获得给定日期所在月的任意一天的日期
     *
     * @param date
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date getDayOfMonth(Date date, int day, int hour, int minute,
                                     int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTime();
    }

    public static Date getFirstDayOfWeekByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int offset = getFirstDayOfWeekOffset(calendar);
        calendar.add(Calendar.DATE, -offset);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    private static int getFirstDayOfWeekOffset(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        int offset = 0;
        if (dayOfWeek >= firstDayOfWeek) {
            offset = dayOfWeek - firstDayOfWeek;
        } else {
            offset = dayOfWeek - firstDayOfWeek + Calendar.SATURDAY;
        }
        return offset;
    }

    public static Date getLastDayOfWeekByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int offset = getFirstDayOfWeekOffset(calendar);
        calendar.add(Calendar.DATE, Calendar.SATURDAY - offset - 1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static String customMilliseconds(long milliseconds) {
        long temp = milliseconds;
        StringBuilder sb = new StringBuilder();
        // 计算毫秒数
        long millis = temp % 1000;
        // 去掉毫米数，转化为秒为单位
        temp = (temp - temp % 1000) / 1000;
        if (temp == 0) {
            sb.append("0.").append(millis).append("秒");
            return sb.toString();
        }

        // 计算秒数
        long seconds = temp % 60;
        // 去掉秒数，转化为分钟为单位
        temp = (temp - temp % 60) / 60;
        if (temp == 0) {
            sb.append(seconds).append(".").append(millis).append("秒");
            return sb.toString();
        }

        // 计算分钟数
        long minutes = temp % 60;
        // 去掉分钟数，转化为小时为单位
        temp = (temp - temp % 60) / 60;
        if (temp == 0) {
            sb.append(minutes).append("分").append(seconds).append(".")
                    .append(millis).append("秒");
            return sb.toString();
        }

        // 计算小时数
        long hours = temp % 24;
        // 去掉小时数，转化为天为单位
        temp = (temp - temp % 24) / 24;
        sb.append(hours).append("小时").append(minutes).append("分")
                .append(seconds).append(".").append(millis).append("秒");
        return sb.toString();
    }

    public static String getTimeForComment(String pattern, long time) {
        if (time == 0) {
            return "11-01 15:35";
        }
        return format(new Date(time), pattern);
    }

/*	public static long getNowDayTime() {
        Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, -12);// 12小时前？
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}*/

    public static Date getPreviousDayStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);// 计算前一天的日期
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getPreviousDayEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);// 计算前一天的日期
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getDayStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getDayEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.FromStr("2015-08-01 03:59:58"));
        System.out.println(DateUtil.FromStr("2015-08-01 23:59:59"));

        System.out.println("-----------------");

        System.out.println(getDayEnd(DateUtil.FromStr("2015-08-01 23:59:59")));
        System.out.println(getDayEnd(DateUtil.FromStr("2015-08-01 23:59:58")));
        System.out.println(getDayEnd(DateUtil.FromStr("2015-08-01 03:59:58")));
        System.out.println(getDayEnd(DateUtil.FromStr("2015-08-01 11:59:58")));
        System.out.println(getDayEnd(new Date()));

        System.out.println("-----------------");

        System.out.println(getDayStart(DateUtil.FromStr("2015-08-01 23:59:59")));
        System.out.println(getDayStart(DateUtil.FromStr("2015-08-01 23:59:58")));
        System.out.println(getDayStart(DateUtil.FromStr("2015-08-01 03:59:58")));
        System.out.println(getDayStart(DateUtil.FromStr("2015-08-01 11:59:58")));
        System.out.println(getDayStart(new Date()));

    }

    public static Date getNextDayStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);// 计算后一天的日期
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 根据开始时间和结束时间，生成排序的每天的日期字符串（包括一头一尾的这一天或两天）
     * 如：20150801, 20150802, 20150803, ..., 20150915等等
     *
     * @param fromDate
     * @param toDate
     * @return
     * @author Maxwell Lee
     * @time: 2015年8月24日
     */
    public static List<String> CreateDailyDateStr(Date fromDate, Date toDate) {
        Calendar cal;
        List<String> rst;
        SimpleDateFormat sdf;
        String curDate, endDate;

        if (fromDate == null || toDate == null) {
            return null;
        }
        if (fromDate.after(toDate)) {
            throw new IllegalArgumentException("fromDate参数不能比toDate参数大。");
        }

        cal = Calendar.getInstance();
        rst = new ArrayList<>();
        sdf = new SimpleDateFormat("yyyyMMdd");

        endDate = sdf.format(toDate);
        cal.setTime(fromDate);

        do {
            curDate = sdf.format(cal.getTime());
            rst.add(curDate);
            cal.add(Calendar.DAY_OF_YEAR, 1);
        } while (endDate.equals(curDate) == false);

        return rst;
    }

}
