package com.auto.util;

import org.apache.commons.lang.StringUtils;

/**
 * @notes:字符串处理工具类
 *
 * @author taylor
 *
 * 2015-7-14	下午5:35:43
 */
public class StringTool {
	
	/**
	 * @notes:处理包路径转文件路径		
	 * 如：com.taylor.api.mall.apartment.entity=>com\\taylor\\api\\mall\\apartment\\entity
	 *
	 * @param packageStr
	 * @return
	 * @author	taylor
	 * 2015-7-14	下午5:42:59
	 */
	public static String getPackage2FilePath(String packageStr) {
		if (StringUtils.isBlank(packageStr)) {
			return "";
		}
		
		String[] ps = packageStr.split("\\.");
		StringBuilder psb = new StringBuilder(0);
		for (int i = 0; i < ps.length; i++) {
			psb.append(ps[i]);
			if (i < ps.length - 1) {
				psb.append("\\");
			}
		}
		return psb.toString();
	}
	
	/**
	 * @notes:将列名变更成属性名
	 * 如：user_name	=> userName
	 * @param columnName	
	 * @return
	 * @author	taylor
	 * 2015-7-14	下午5:51:41
	 */
	public static String changeColumn2Property(String columnName) {
		if (StringUtils.isBlank(columnName)) {
			return "";
		}
		
		String[] names = columnName.split("[-|_]");
		StringBuilder nsb = new StringBuilder(0);
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			if (i != 0) {
				name = toUpperCaseIndex(name);
			}
			nsb.append(name);
		}
		return nsb.toString();
	}
	
	/**
	 * @notes:将字符串首字母变更成大写
	 * 如：abc	=>	Abc
	 *
	 * @param str
	 * @return
	 * @author	taylor
	 * 2015-7-14	下午5:55:41
	 */
	public static String toUpperCaseIndex(String str) {
		if (StringUtils.isBlank(str)) {
			return "";
		}
		
		String indexStr = (str.charAt(0) + "").toUpperCase();
		if (str.length() > 1) {
			str = indexStr + str.substring(1);
		}
		return str;
	}
	
	/**
	 * @notes:获取基础包
	 * 如：com.taylor.api.mall.apartment.entity => com.taylor.api.mall.apartment
	 *
	 * @param entityPackage
	 * @return
	 * @author	taylor
	 * 2015-7-14	下午6:07:33
	 */
	public static String getBasePackageByEntityPackage(String entityPackage) {
		String[] ps = entityPackage.split("\\.");
		StringBuilder ssb = new StringBuilder(0);
		int index = ps.length - 1;
		for (int i = 0; i < ps.length - 1; i++) {
			ssb.append(ps[i]);
			if (i < index - 1) {
				ssb.append(".");
			}
		}
		return ssb.toString();
	}
	
	/**
	 * @notes:字符串首字母小写
	 * 如：ApAdminUserDao => apAdminUserDao
	 * @param str	
	 * @return
	 * @author	taylor
	 * 2015-7-15	下午6:47:55
	 */
	public static String toLowerCaseIndex(String str) {

		if (StringUtils.isBlank(str)) {
			return "";
		}
		
		String indexStr = (str.charAt(0) + "").toLowerCase();
		if (str.length() > 1) {
			str = indexStr + str.substring(1);
		}
		return str;
	}

	/**
	 * @notes:
	 *
	 * @param args
	 * @author	taylor
	 * 2015-7-14	下午5:35:39
	 */
	public static void main(String[] args) {
		System.out.println(getBasePackageByEntityPackage("com.taylor.api.mall.apartment.entity"));
	}

}
