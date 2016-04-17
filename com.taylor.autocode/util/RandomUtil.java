package com.taylor.api.common.util;

import java.util.Random;

public class RandomUtil {
	
	private RandomUtil(){
		
	}
	
	private static final String NUMBER = "0123456789";//数字
	
	private static final String ALL_CASE = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";//大小写字母
	
	private static final String NUMBER_CASE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";//字母和数字

	private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";//小写字母
	
	private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";//大写字母
	
	
	/**
	 * @notes:随机产生几位数字：例：maxLength=3,则结果可能是 012
	 * @param maxLength		生成的长度
	 * @author	taylor
	 * 2014-4-16	上午10:13:18
	 */
	public static final int getNumber(int maxLength) {
		Random random = new Random();
		return random.nextInt(maxLength);
	}

	/**
	 * @notes:随机产生区间数字<例：minNumber=1,maxNumber=2,则结果可能是 1、2,包括首尾。>
	 * @param minNumber		首
	 * @param maxNumber		尾
	 * @author	taylor
	 * 2014-4-16	上午10:13:54
	 */
	public static int getRegionNumber(int minNumber, int maxNumber) {
		return minNumber + getNumber(maxNumber - minNumber);
	}

	/**
	 * @notes:随机产生几位字符串<例：maxLength=3,则结果可能是 aAz>
	 * @param maxLength			传入数必须是正数。
	 * @author	taylor
	 * 2014-4-16	上午10:14:35
	 */
	public static String getString(int maxLength) {
		return doProduce(maxLength, ALL_CASE);
	}

	/**
	 * @notes:随机产生随机数字+字母：例：maxLength=3,则结果可能是 1Az
	 * @param maxLength			传入数必须是正数。
	 * @author	taylor
	 * 2014-4-16	上午10:16:30
	 */
	public static String getStringAndNumber(int maxLength) {
		return doProduce(maxLength, NUMBER_CASE);
	}

	/**
	 * @notes:自定义随机产生结果
	 * @param source			源字符串
	 * @param maxLength			随机长度
	 * @author	taylor
	 * 2014-4-16	上午10:16:56
	 */
	public static String getResultByCustom(String source, int maxLength) {
		return doProduce(maxLength, source);
	}
	
	/**
	 * @notes:获取大写字母的随机len
	 * @param len			随机串长度
	 * @author	taylor
	 * 2014-4-16	上午10:22:50
	 */
	public static String getUppercaseString(int len) {
		return doProduce(len, UPPER_CASE);
	}
	
	/**
	 * @notes:获取小写字母的随机len
	 * @param len			随机串长度
	 * @author	taylor
	 * 2014-4-16	上午10:24:32
	 */
	public static String getLowercaseString(int len) {
		return doProduce(len, LOWER_CASE);
	}

	/**
	 * @notes:生产结果
	 * @param maxLength
	 * @param source
	 * @author	taylor
	 * 2014-4-16	上午10:18:25
	 */
	private static String doProduce(int maxLength, String source) {
		StringBuilder sb = new StringBuilder(100);
		for (int i = 0; i < maxLength; i++) {
			final int number = getNumber(source.length());
			sb.append(source.charAt(number));
		}
		return sb.toString();
	}
	
	/**
	 * @notes:获取指定（maxLength）长度的随机数字
	 *
	 * @param maxLength		随机字条符串长度
	 * @author	taylor
	 * 2014-5-12	上午10:30:40
	 */
	public static String getNumberByLength(int maxLength) {

		return doProduce(maxLength, NUMBER);
	}

	public static void main(String[] args) {
		System.out.println(getRegionNumber(5, 10));
	}

}
