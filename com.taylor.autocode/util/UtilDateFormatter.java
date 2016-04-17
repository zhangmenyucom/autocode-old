package com.taylor.api.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.format.Formatter;

/**
 * @notes:spring mvc 日期转换类 将spring mvc 的字符串自动转换为日期格式
 *
 * @author arron
 *
 * 2015-4-27	下午7:11:50
 */
public class UtilDateFormatter implements Formatter<Date> {
	
	private static final Logger LOG = Logger.getLogger(UtilDateFormatter.class);
	
	private String format;

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}

	@Override
	public String print(Date object, Locale locale) {
		if (object == null)
			return null;

		DateFormat fmt = new SimpleDateFormat(getFormat());
		return fmt.format(object);
	}

	@Override
	public Date parse(String text, Locale locale) throws ParseException {
		if (StringUtils.isEmpty(text))
			return null;
		DateFormat fmt = new SimpleDateFormat(this.format);
		try {
			return new Date(fmt.parse(text).getTime());
		} catch (ParseException e) {
			LOG.error("print error", e);
			throw new ParseException("日期类型转换错误>>>",1);
		}
	}
}
