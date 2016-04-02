package com.huaren.logistics.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
  public final static String DATE_TIME_FORMATE = "yyyy-MM-dd HH:mm:ss";
  /**
   * 毫秒数转日期字符串
   *
   * @param time 毫秒数
   * @param format 字符串格式
   * @return 日期字符串
   */
  public static String parseDateToString(long time, String format) {
    if (time <= 0) return "";
    return new SimpleDateFormat(format).format(new Date(time));
  }

  /**
   * 毫秒数转日期字符串
   *
   * @param date 时间
   * @param format 字符串格式
   * @return 日期字符串
   */
  public static String parseDateToString(Date date, String format) {
    return new SimpleDateFormat(format).format(date);
  }

  /**
   * 当前时间转格式化字符串
   *
   * @param format
   *            字符串格式
   * @return 日期字符串
   */
  public static String parseCurrDateToString(String format) {
    return new SimpleDateFormat(format).format(new Date());
  }
}
