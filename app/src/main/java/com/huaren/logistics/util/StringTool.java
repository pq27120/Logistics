/**
 *
 * @author xiaohuan
 * 2015年6月13日
 */
package com.huaren.logistics.util;

import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaohuan
 * 
 * @date 2015年6月13日
 */
public class StringTool {
	/**
	 * 判断是否为非空字符串
	 */
	public static boolean isNotNull(String str) {
		if (str != null && !"".equals(str.trim()) && !"null".equalsIgnoreCase(str.trim()))
			return true;
		return false;
	}

	/**
	 * 获取非空字符串
	 */
	public static String getNotNullStr(String str) {
		if (str != null && !"null".equalsIgnoreCase(str.trim()))
			return str;
		return "";
	}

	/**
	 * 毫秒数转日期字符串
	 * 
	 * @param time
	 *            毫秒数
	 * @param format
	 *            字符串格式
	 * @return 日期字符串
	 */
	public static String parseDateToString(long time, String format) {
		if (time <= 0)
			return "";
		return new SimpleDateFormat(format).format(new Date(time));
	}

	/**
	 * 判断是否是电话号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobile(String mobiles) {
		if (!isNotNull(mobiles))
			return false;
		mobiles = mobiles.trim();
		Pattern p = Pattern.compile("^1[0-9]{10}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 判断是否是电话号码
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone) {
		if (!isNotNull(phone))
			return false;
		phone = phone.trim();
		Pattern p = Pattern.compile("^[0-9]+$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	/**
	 * 
	 通过name获取字符资源
	 */
	public static String getStringByName(Context context, String name) {
		if (!isNotNull(name))
			return "无";
		String str = "";
		try {

			str = context.getString(context.getResources().getIdentifier(name, "string", context.getPackageName()));
		} catch (Exception e) {
			return "";
		}
		return str;
	}

}
