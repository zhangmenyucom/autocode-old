package com.taylor.api.common.util;

import org.apache.commons.lang.StringUtils;

/**
 * @notes:数字工具类
 *
 * @author taylor
 *
 * 2014-7-18	下午3:43:03
 */
public class HorizonObjTools {
	
	private HorizonObjTools(){
		
	}
	
	/**
	 * @notes:判断数据对象是否为空
	 *
	 * @param obj
	 * @author	taylor
	 * 2014-7-18	下午3:45:27
	 */
	public static boolean isNull(Object obj) {
		if (null == obj || StringUtils.isBlank(obj.toString())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @notes:判断数据对象是否不为空
	 *
	 * @param obj
	 * @author	taylor
	 * 2014-7-18	下午3:50:06
	 */
	public static boolean isNotNull(Object obj) {
		return !isNull(obj);
	}
	
	/**
	 * @notes:对像转Integer类型
	 *
	 * @param obj
	 * @author	taylor
	 * 2014-7-18	下午3:53:18
	 */
	public static Integer obj2Integer(Object obj) {
		if (isNotNull(obj)) {
			return Integer.valueOf(obj.toString());
		}
		return null;
	}
	
	/**
	 * @notes:对像转Double类型
	 *
	 * @param obj
	 * @author	taylor
	 * 2014-7-18	下午3:54:24
	 */
	public static Double obj2Double(Object obj) {
		if (isNotNull(obj)) {
			return Double.valueOf(obj.toString());
		}
		return null;
	}
	
	/**
	 * @notes:对像转Float类型
	 *
	 * @param obj
	 * @author	taylor
	 * 2014-7-18	下午3:54:24
	 */
	public static Float obj2Float(Object obj) {
		if (isNotNull(obj)) {
			return Float.valueOf(obj.toString());
		}
		return null;
	}
	
	/**
	 * @notes:对像转Float类型
	 *
	 * @param obj
	 * @author	taylor
	 * 2014-7-18	下午3:54:24
	 */
	public static String obj2String(Object obj) {
		if (isNotNull(obj)) {
			return obj.toString();
		}
		return null;
	}

}
