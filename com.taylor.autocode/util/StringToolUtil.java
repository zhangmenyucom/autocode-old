package com.taylor.api.common.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

public class StringToolUtil {
	
	public final static Logger logger = Logger.getLogger(StringToolUtil.class);

	/**
	 * taylor	2010-10-30	上午11:43:00
	 * 
	 * @comment	过滤HTML的字符串截取方法
	 * 
	 * @param s				字符串对象
	 * @param lengt			长度
	 * @param isPoint		截取后是否带......
	 * @param isTrim		是否去除头尾空字符串
	 */
	public static String bSubstringRemoveHtml(String s, int length, boolean isPoint, boolean isTrim) {
		if (StringUtils.isBlank(s)) {
			return "";
		}
		s = removeHtml(s);
		s = s.replaceAll("^[　  ]+|[　  ]+$", "");

		return bSubstring(Jsoup.parse(s).text(), length, isPoint, isTrim);
	}

	public static String removeHtml(String s) {
		if (StringUtils.isBlank(s)) {
			return "";
		}
		return Jsoup.parse(s).text();
	}

	/**
	 * taylor	2010-10-30	上午11:41:26
	 * 
	 * @comment	字符截取方法
	 * 
	 * @param s				字符串对象
	 * @param length		截取长度<汉字长度>
	 * @param isPoint		是否带......
	 * @param isTrim		是否去除头尾空字符串
	 */
	public static String bSubstring(String s, int length, boolean isPoint, boolean isTrim) {
		try {
			if (StringUtils.isBlank(s)) {
				return "";
			}
			s = s.replaceAll("^[　  ]+|[　  ]+$", "");
			length = length * 2;
			byte[] bytes = s.getBytes("Unicode");
			if (bytes.length < length) {
				return s;
			}
			int n = 0; // 表示当前的字节数
			int i = 2; // 要截取的字节数，从第3个字节开始
			for (; i < bytes.length && n < length; i++) {
				// 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
				if (i % 2 == 1) {
					n++; // 在UCS2第二个字节时n加1
				} else {
					// 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
					if (bytes[i] != 0) {
						n++;
					}
				}
			}
			// 如果i为奇数时，处理成偶数
			if (i % 2 == 1) {
				if (bytes[i - 1] != 0) {
					// 该UCS2字符是汉字时，去掉这个截一半的汉字
					i = i - 1;
				} else {
					// 该UCS2字符是字母或数字，则保留该字符
					i = i + 1;
				}
			}
			StringBuilder sb = new StringBuilder(new String(bytes, 0, i, "Unicode"));
			s = sb.toString();
			if (isPoint) {
				s = s + "...";
			}
			return s;
		} catch (Exception e) {
			logger.error("《《《《《《《《《调用StringToolUtil中的bSubstring方法异常》》》》》》》", e);
		}

		return "";
	}

	public static String decodeUnicode(String dataStr) {
		if (StringUtils.isBlank(dataStr)) {
			return "";
		}
		int start = 0;
		int end = 0;
		final StringBuffer buffer = new StringBuffer();
		while (start > -1) {
			end = dataStr.indexOf("\\u", start + 2);
			String charStr = "";
			if (end == -1) {
				charStr = dataStr.substring(start + 2, dataStr.length());
			} else {
				charStr = dataStr.substring(start + 2, end);
			}
			char letter = (char) Integer.parseInt(charStr, 16);
			buffer.append(new Character(letter).toString());
			start = end;
		}
		return buffer.toString();
	}

	/**
	 * 获取字符串长度，以汉字为基数
	 * @param value
	 */
	public static int getStringLen(String value) {
		int n = 0; // 表示当前的字节数
		int i = 2; // 要截取的字节数，从第3个字节开始
		try {
			byte[] bytes = value.getBytes("Unicode");
			for (; i < bytes.length; i++) {
				// 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
				if (i % 2 == 1) {
					n++; // 在UCS2第二个字节时n加1
				} else {
					// 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
					if (bytes[i] != 0) {
						n++;
					}
				}
			}
			if (n % 2 == 1) {
				n++;
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("《《《《《《《《《调用StringToolUtil中的getStringLen方法异常》》》》》》》", e);
		}
		return n / 2;
	}
	
	/**
	 * @notes:字符串不够位补0
	 *
	 * @param str		字符串
	 * @param len		匹配长度
	 * @author	taylor
	 * 2014-6-3	下午4:19:32
	 */
	public static String pullZero(String str, int len) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		int strLen = str.length();
		if (strLen < len) {
			String zero = "";
			for (int i = 0; i < (len - strLen); i++) {
				zero += "0";
			}
			return (zero + str);
		}
		return str;
	}
//	/**
//	 * @notes:获取商品评分HTML
//	 * 
//	 * @param value
//	 *            商品评分值
//	 * @author taylor 2014-5-9 下午12:02:16
//	 */
//	public static String getRatingInfoHtml(Float value) {
//		StringBuilder html = new StringBuilder(0);
//
//		int haveStar = value.intValue();
//		for (int i = 0; i < value.intValue(); i++) {// 全星处理
//			if (value > 5)
//				break;
//			html.append("<i class=\"icon-starAll\"></i>");
//		}
//		if (value.intValue() < value) {// 半星处理
//			html.append("<i class=\"icon-starHalf\"></i>");
//			haveStar++;
//		}
//		for (int i = 0; i < 5 - haveStar; i++) {// 空星处理
//			html.append("<i class=\"icon-starNull\"></i>");
//		}
//		return html.toString();
//	}
	
	/**
	 * @notes:获取商品评分HTML4H5
	 * 
	 * @param value
	 *            商品评分值
	 * @author taylor 2014-5-9 下午12:02:16
	 */
	public static String getRatingInfoHtml4H5(Float value) {
		StringBuilder html = new StringBuilder(0);

		int haveStar = value.intValue();
		for (int i = 0; i < value.intValue(); i++) {// 全星处理
			if (value > 5)
				break;
			html.append("<i></i>");
		}
		if (value.intValue() < value) {// 半星处理
			html.append("<i class=\"half\"></i>");
			haveStar++;
		}
		for (int i = 0; i < 5 - haveStar; i++) {// 空星处理
			html.append("<i class=\"on\"></i>");
		}
		return html.toString();
	}

	/**
	 * 
	 * @desc   getRatingInfoHtml4PC(用于拼评星html代码)
	 * @param value
	 * @author taylor
	 * @date  2015-10-8 下午4:31:50
	 */
	public static String getRatingInfoHtml4PC(Float value) {
		StringBuilder html = new StringBuilder(0);

		int haveStar = value.intValue();
		for (int i = 0; i < value.intValue(); i++) {// 全星处理
			if (value > 5)
				break;
			html.append("<i class=\"on\"></i>");
		}
		if (value.intValue() < value) {// 半星处理
			html.append("<i class=\"half\"></i>");
			haveStar++;
		}
		for (int i = 0; i < 5 - haveStar; i++) {// 空星处理
			html.append("<i></i>");
		}
		return html.toString();
	}

	public static void main(String[] args) {
		String str = "adc张梦雨123";
		System.out.println(bSubstring(str, 3, false, false));
		// System.out.println(getRatingInfoHtml4H5(4.5F));
	}
}
