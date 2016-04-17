package com.taylor.api.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * @notes:时间工具类
 * @author taylor
 * 
 *         2014-4-3 下午1:59:35
 */
public final class DateTools extends SimpleDateFormat {
	
	private static final Logger LOG = Logger.getLogger(DateTools.class);
	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = -2987750868895651661L;

	/**
	 * 时间格式为yyyy-MM-dd HH:mm:ss
	 */
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 时间格式为yyyy-MM-dd HH:mm:ss.sss
	 */
	public static final String YYYY_MM_DD_HH_MM_SS_S = "yyyy-MM-dd HH:mm:ss.SSS";

	/**
	 * 时间格式为yyyy-MM-dd
	 */
	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	/**
	 * 时间格式为yyyyMMddHHmmss
	 */
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	/**
	 * 时间格式为ddHHmmss
	 */
	public static final String DDHHMMSS = "ddHHmmss";

	/**
	 * 时间格式为HHmmss
	 */
	public static final String HHMMSS = "ddHHmmss";

	/**
	 * 时间格式为yyyy-MM-dd HH:mm
	 */
	public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

	/**
	 * 时间格式为yyyy-MM-dd HH
	 */
	public static final String YYYY_MM_DD_HH = "yyyy-MM-dd HH";

	/**
	 * DATETOOL
	 */
	private static DateTools dateTools = null;

	/**
	 * 日历类
	 */
	private final Calendar calendar = Calendar.getInstance();

	/**
	 * <默认私有构造函数>
	 */
	private DateTools() {
		super(YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * <默认构造函数>
	 * 
	 * @param mode 模态
	 */
	private DateTools(String mode) {
		super(mode);
	}

	/**
	 * @notes:此类构造的日期工具类不可以格式化日期
	 * @author	taylor
	 * 2014-4-3	下午2:00:10
	 */
	public static DateTools getDateTools() {
		if (null == dateTools) {
			dateTools = new DateTools();
		}
		return dateTools;
	}

	/**
	 * @notes:此类构造的日期工具类可以格式化日期
	 * @param mode		模式 以哪种时间模式去创建或转换
	 * @return			DateTools
	 * @author	taylor
	 * 2014-4-3	下午2:00:20
	 */
	public static DateTools getDateTools(String mode) {
		if (null == dateTools) {
			dateTools = new DateTools(mode);
		}
		return dateTools;
	}

	/**
	 * @notes:得到会传入日期的月份
	 * @param date		日期
	 * @return	int month
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:00:40
	 */
	public int getMonth(Date date) throws ParseException {
		setCalendar(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * @notes:得到会传入日期的年份
	 * @param date		日期
	 * @return int year
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:00:58
	 */
	public int getYear(Date date) throws ParseException {
		setCalendar(date);
		return calendar.get(Calendar.YEAR);
	}
	
	/**
	 * @notes:得到会传入日期的是日期所在月的哪一天
	 * @param date		日期
	 * @return int day
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:01:42
	 */
	public int getDate(Date date) throws ParseException {
		setCalendar(date);
		return calendar.get(Calendar.DATE) + 1;
	}
	
	/**
	 * @notes:得到会传入日期的分钟
	 * @param date		日期
	 * @return int minute 	
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:02:33
	 */
	public int getMinute(Date date) throws ParseException {
		setCalendar(date);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * @notes:得到会传入日期的小时
	 * @param date			日期
	 * @param isStandard	是否是24小时制的,true:是
	 * @return int hour
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:03:17
	 */
	public int getHour(Date date, boolean isStandard) throws ParseException {
		setCalendar(date);
		if (isStandard) {
			return calendar.get(Calendar.HOUR_OF_DAY);
		} else {
			return calendar.get(Calendar.HOUR);
		}
	}

	/**
	 * @notes:得到会传入日期的小时
	 * @param date		日期
	 * @return int hour
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:04:07
	 */
	public int getSecond(Date date) throws ParseException {
		setCalendar(date);
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * @notes:得到会传入日期的月份
	 * @param strDate		字符串日期
	 * @return int month
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:04:39
	 */
	public int getMonth(String strDate) throws ParseException {
		setCalendar(strDate);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * @notes:得到会传入日期的年份
	 * @param strDate		字符串日期
	 * @return int year
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:05:01
	 */
	public int getYear(String strDate) throws ParseException {
		setCalendar(strDate);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * @notes:得到会传入日期的是日期所在月的哪一天
	 * @param strDate		字符串日期
	 * @return <int> day of month
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:05:20
	 */
	public int getDate(String strDate) throws ParseException {
		setCalendar(strDate);
		return calendar.get(Calendar.DATE) + 1;
	}

	/**
	 * @notes:得到会传入日期的分钟
	 * @param strDate		字符串日期
	 * @return	<int> minute of day
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:06:11
	 */
	public int getMinute(String strDate) throws ParseException {
		setCalendar(strDate);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * @notes:得到会传入日期的小时
	 * @param strDate		字符串日期
	 * @param isStandard	是否是24小时制的,true:是
	 * @return <int> hour of day
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:06:51
	 */
	public int getHour(String strDate, boolean isStandard) throws ParseException {
		setCalendar(strDate);
		if (isStandard) {
			return calendar.get(Calendar.HOUR_OF_DAY);
		} else {
			return calendar.get(Calendar.HOUR);
		}
	}

	/**
	 * @notes:得到会传入日期的小时
	 * @param strDate		字符串日期
	 * @return <int> second of day
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:07:32
	 */
	public int getSecond(String strDate) throws ParseException {
		setCalendar(strDate);
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * @notes:在原有的时间小时上加上或减去多少
	 * @param date			原有时间
	 * @param levevHour		要加减的小时数
	 * @param isStandard	是还是24小时制
	 * @return	Date
	 * @author	taylor
	 * 2014-4-3	下午2:08:02
	 */
	public Date operationHour(Date date, int levevHour, boolean isStandard) {
		setCalendar(date);
		if (isStandard) {
			calendar.add(Calendar.HOUR_OF_DAY, levevHour);
		} else {
			calendar.add(Calendar.HOUR, levevHour);
		}
		return calendar.getTime();
	}

	/**
	 * @notes:在原有的时间分钟上加上或减去多少
	 * @param date			原有时间
	 * @param levevHour		要加减的分钟数
	 * @param isStandard	是还是24小时制
	 * @return	Date
	 * @author	taylor
	 * 2014-4-3	下午2:08:49
	 */
	public Date operationMinute(Date date, int levevMinute) {
		setCalendar(date);
		calendar.add(Calendar.MINUTE, levevMinute);
		return calendar.getTime();
	}

	/**
	 * @notes:在原有的时间年份上加上或减去多少
	 * @param date			原有时间
	 * @param levevYear		要加减的年份数
	 * @return	Date
	 * @author	taylor
	 * 2014-4-3	下午2:09:27
	 */
	public Date operationYear(Date date, int levevYear) {
		setCalendar(date);
		calendar.add(Calendar.YEAR, levevYear);
		return calendar.getTime();
	}

	/**
	 * @notes:在原有的时间月份上加上或减去多少
	 * @param date			原有时间
	 * @param levevMonth	要加减的月份数
	 * @return	Date
	 * @author	taylor
	 * 2014-4-3	下午2:09:49
	 */
	public Date operationMonth(Date date, int levevMonth) {
		setCalendar(date);
		calendar.add(Calendar.MONTH, levevMonth);
		return calendar.getTime();
	}

	/**
	 * @notes:在原有的时间月份上加上或减去多少
	 * @param date			原有时间
	 * @param levevDate		要加减的月份数
	 * @return	Date
	 * @author	taylor
	 * 2014-4-3	下午2:10:12
	 */
	public Date operationDate(Date date, int levevDate) {
		setCalendar(date);
		calendar.add(Calendar.DATE, levevDate);
		return calendar.getTime();
	}

	/**
	 * @notes:在原有的时间秒上加上或减去多少
	 * @param date			原有时间
	 * @param levevSecond	要加减的秒数
	 * @return	Date
	 * @author	taylor
	 * 2014-4-3	下午2:11:19
	 */
	public Date operationSecond(Date date, int levevSecond) {
		setCalendar(date);
		calendar.add(Calendar.SECOND, levevSecond);
		return calendar.getTime();
	}

	/**
	 * @notes:在原有的时间小时上加上或减去多少(字符串时间)
	 * @param strDate		原有时间
	 * @param levevHour		要加减的小时数
	 * @param isStandard	是还是24小时制
	 * @return	String
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:11:37
	 */
	public String operationHour(String strDate, int levevHour, boolean isStandard) throws ParseException {
		setCalendar(strDate);
		if (isStandard) {
			calendar.add(Calendar.HOUR_OF_DAY, levevHour);
		} else {
			calendar.add(Calendar.HOUR, levevHour);
		}
		return format(calendar.getTime());
	}

	/**
	 * @notes:在原有的时间分钟上加上或减去多少
	 * @param strDate			原有时间
	 * @param levevMinute		要加减的分钟数
	 * @return	String
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:12:07
	 */
	public String operationMinute(String strDate, int levevMinute) throws ParseException {
		setCalendar(strDate);
		calendar.add(Calendar.MINUTE, levevMinute);
		return format(calendar.getTime());
	}

	/**
	 * @notes:在原有的时间年份上加上或减去多少
	 * @param strDate		原有时间
	 * @param levevYear		要加减的年份数
	 * @return	String
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:12:27
	 */
	public String operationYear(String strDate, int levevYear) throws ParseException {
		setCalendar(strDate);
		calendar.add(Calendar.YEAR, levevYear);
		return format(calendar.getTime());
	}

	/**
	 * @notes:在原有的时间月份上加上或减去多少
	 * @param strDate			原有时间
	 * @param levevMonth		要加减的月份数
	 * @return	String
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:12:47
	 */
	public String operationMonth(String strDate, int levevMonth) throws ParseException {
		setCalendar(strDate);
		calendar.add(Calendar.MONTH, levevMonth);
		return format(calendar.getTime());
	}

	/**
	 * @notes:在原有的时间月份上加上或减去多少
	 * @param strDate		原有时间
	 * @param levevDate		要加减的月份数
	 * @return	Date
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:13:14
	 */
	public String operationDate(String strDate, int levevDate) throws ParseException {
		setCalendar(strDate);
		calendar.add(Calendar.DATE, levevDate);
		return format(calendar.getTime());
	}

	/**
	 * @notes:在原有的时间秒上加上或减去多少
	 * @param strDate			原有时间
	 * @param levevSecond		要加减的秒数
	 * @return	Date
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:13:34
	 */
	public String operationSecond(String strDate, int levevSecond) throws ParseException {
		setCalendar(strDate);
		calendar.add(Calendar.SECOND, levevSecond);
		return format(calendar.getTime());
	}

	/**
	 * @notes:返回两个时间段之间的间隔(天)
	 * @param srcDate		时间点1
	 * @param destDate		时间点2
	 * @return	int
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:14:00
	 */
	public int getDaysOperationDate(Date srcDate, Date destDate) throws ParseException {
		return (int) StrictMath.abs((srcDate.getTime() - destDate.getTime()) / 30);
	}

	/**
	 * @notes:返回两个时间段之间的间隔(天)
	 * @param strSrcDate		 时间点1
	 * @param strDestDate		时间点2
	 * @return	int
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:14:15
	 */
	public int getDaysOperationDate(String strSrcDate, String strDestDate) throws ParseException {
		return (int) StrictMath.abs((parse(strSrcDate).getTime() - parse(strDestDate).getTime()) / 30);
	}

	/**
	 * @notes:判断用户输入的时间是否介于两个时间段内
	 * @param afterDate		结束时间
	 * @param beforeDate	起始时间
	 * @param currentDate	用户输入的时间
	 * @return	boolean true:是介于两个时间段之间
	 * @author	taylor
	 * 2014-4-3	下午2:14:39
	 */
	public boolean compareDate(Date afterDate, Date beforeDate, Date currentDate) {
		if (currentDate.after(beforeDate) && currentDate.before(afterDate)) {
			return true;
		}
		return false;
	}

	/**
	 * @notes:判断用户输入的时间是否介于两个时间段内(字符串时间)
	 * @param strAfterDate		结束时间
	 * @param strBeforeDate		起始时间
	 * @param strCurrentDate	用户输入的时间
	 * @return	boolean true:是介于两个时间段之间
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:15:06
	 */
	public boolean compareDate(String strAfterDate, String strBeforeDate, String strCurrentDate) throws ParseException {
		Date currentDate = parse(strCurrentDate);
		if (currentDate.after(parse(strBeforeDate)) && currentDate.before(parse(strAfterDate))) {
			return true;
		}
		return false;
	}

	/**
	 * 返回系统的当前时间,以字符串形式
	 * 
	 * @return String
	 */
	public String getSystemStrDate() {
		return format(new Date());
	}

	/**
	 * 设置日历的时间
	 */
	private void setCalendar(Date date) {
		calendar.setTime(date);
	}

	/**
	 * 设置日历的时间
	 */
	private void setCalendar(String strDate) throws ParseException {
		calendar.setTime(parse(strDate));
	}

	/**
	 * @notes:判断当前时间是否介于开始时间和结束时间
	 * @param startTime		开始时间
	 * @param endTime		结束时间
	 * @return	boolean
	 * @throws ParseException
	 * @author	taylor
	 * 2014-4-3	下午2:15:36
	 */
	public boolean compareDate(String startTime, String endTime) throws ParseException {
		Date currentDate = new Date();
		String strCurrentTime = format(currentDate);
		String time = strCurrentTime.substring(0, strCurrentTime.indexOf(" ") + 1);
		Date startDate = parse(time + startTime);
		Date endDate = parse(time + endTime);
		if (currentDate.before(startDate)) {
			if (currentDate.before(endDate)) {
				return true;
			}
		} else if (endDate.before(startDate)) {
			if (currentDate.after(startDate)) {
				return true;
			}
		} else {
			if (currentDate.after(startDate) && currentDate.before(endDate)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @notes:格式化日期为 pattern 形式
	 *
	 * @param myDate
	 * @param pattern
	 * @author	taylor
	 * 2014-4-21	下午9:25:08
	 */
	public static String formatDateTime(Date myDate, String pattern) {
		if (null == myDate) {
			return null;
		}
		SimpleDateFormat fd = new SimpleDateFormat(pattern);
		return fd.format(myDate);
	}
	
	/**
	 * @notes:格式话日期为yyyy-MM-dd HH:mm:ss形式
	 *
	 * @param myDate
	 * @author	taylor
	 * 2014-4-21	下午9:25:32
	 */
	public static String formatDateTime(Date myDate) {
		return formatDateTime(myDate, YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * @notes:将字符串转换成日期 yyyy-MM-dd HH:mm:ss
	 *
	 * @param rq
	 * @author	taylor
	 * 2014-4-21	下午9:25:55
	 */
	public static Date getDateString(String rq) {
		DateFormat df = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		Date d = null;
		try {
			d = df.parse(rq);
		} catch (Exception e) {
			LOG.error("error", e);
		}
		return d;
	}

	/**
	 * @notes:将字符串转换成日期分钟 YYYY_MM_DD_HH_MM
	 *
	 * @param str
	 * @param pattern
	 * @author	taylor
	 * 2014-4-21	下午9:27:56
	 */
	public static Date getDateByString(String str, String pattern) {
		SimpleDateFormat sdf = null;
		try {
			sdf = new SimpleDateFormat(pattern);
		} catch (Exception ex) {
			LOG.error("getDateByString SimpleDateFormat Exception", ex);
			sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM);
		}
		try {
			return sdf.parse(str);
		} catch (Exception e) {
			LOG.error("error", e);
		}
		return null;
	}

	/**
	 * @notes:将字符串转换成日期  YYYY_MM_DD
	 *
	 * @param param
	 * @author	taylor
	 * 2014-4-21	下午9:26:27
	 */
	public static Date parseDate(String param) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
		try {
			date = sdf.parse(param);

		} catch (Exception ex) {
			LOG.error("error", ex);
		}
		return date;
	}
	
	/**
	 * @notes: 计算加N个工作日后的工作日日期
	 * @param startDate	开始时间
	 * @param workDay	加的工作日数
	 * @author: kuta.li
	 * @date: 2015-8-3-下午1:58:54
	 */
	public static Date addWorkDays(Date startDate, Integer workDay){
		Calendar c1 = Calendar.getInstance();
	    c1.setTime(startDate);
	    for (int i = 0; i < workDay; i++) {
	        c1.set(Calendar.DATE, c1.get(Calendar.DATE) + 1);
	        if (Calendar.SATURDAY == c1.get(Calendar.DAY_OF_WEEK) || Calendar.SUNDAY == c1.get(Calendar.DAY_OF_WEEK)) {
	            workDay = workDay + 1;
	            continue;
	        }
	    }
	    return parseDate(formatDateTime(c1.getTime()));
	}
	
	@Override
	public boolean equals(Object obj) {
        if (!super.equals(obj)){
        	return false; // super does class check
        }
        DateTools that = (DateTools) obj;
        return calendar.equals(that.calendar);
    }
	
	@Override
	public int hashCode() {
		return this.calendar.hashCode();
	}
	
}