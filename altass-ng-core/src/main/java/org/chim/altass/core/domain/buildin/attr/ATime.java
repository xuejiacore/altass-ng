/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.domain.buildin.attr
 * Author: Xuejia
 * Date Time: 2017/1/14 1:42
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain.buildin.attr;

import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;

/**
 * Class Name: ATime
 * Create Date: 2017/1/14 1:42
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Elem(alias = "time", version = "1.0")
public class ATime {

    /**
     * 纯粹时间模式
     */
    public static final Integer MODE_PURE_TIME = 0x0;
    /**
     * 计时模式
     */
    public static final Integer MODE_RECKON_BY_TIME = 0x2;
    /**
     * 时间点模式
     */
    public static final Integer MODE_POINT_OF_TIME = 0x4;

    /**
     * 永久执行模式
     * 计时模式每次完成后，重新计时，重新执行任务
     * 时间点模式每次执行完成后，到达下一个时间点重新执行任务
     */
    public static final Integer CYCLE_MODE_FOREVER = -1;
    /**
     * 当此执行模式
     * 计时模式等待对应时长后执行一次
     * 时间点模式到达某个固定事件后执行任务
     */
    public static final Integer CYCLE_MODE_ONLY_ONE = 1;

    @Attr(alias = "timeMode")
    private Integer timerMode = MODE_PURE_TIME;                                 // 定时器的类型，默认纯粹时间
    @Attr(alias = "cycleMode")
    private Integer cycleMode = CYCLE_MODE_ONLY_ONE;                            // 定时器重复类型，默认一次

    @Attr(alias = "year")                         // 年
    private Integer year = 0;
    @Attr(alias = "month")                        // 月
    private Integer month = 0;
    @Attr(alias = "day")                          // 日
    private Integer day = 0;
    @Attr(alias = "hour")                         // 时
    private Integer hour = 0;
    @Attr(alias = "minutes")                      // 分
    private Integer minutes = 0;
    @Attr(alias = "seconds")                      // 秒
    private Integer seconds = 0;

    public ATime() {
    }

    public ATime(Integer year, Integer month, Integer day, Integer hour, Integer minutes, Integer seconds) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public ATime(Integer hour, Integer minutes, Integer seconds) {
        this.hour = hour;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public Integer getTimerMode() {
        return timerMode;
    }

    public void setTimerMode(Integer timerMode) {
        this.timerMode = timerMode;
    }

    public Integer getCycleMode() {
        return cycleMode;
    }

    public void setCycleMode(Integer cycleMode) {
        this.cycleMode = cycleMode;
    }

    @Override
    public String toString() {
        return "ATime{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                ", minutes=" + minutes +
                ", seconds=" + seconds +
                '}';
    }
}
