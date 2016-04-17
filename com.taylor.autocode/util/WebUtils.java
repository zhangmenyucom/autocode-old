package com.taylor.api.common.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.apache.log4j.Logger;

/**
 * 
 * @notes: web层工具类 
 * @date: 2015-11-17 下午3:13:40
 * @author Tony.z
 */
public class WebUtils {
	private static final Logger LOGGER = Logger.getLogger(WebUtils.class);
	
	
	static{
		//只需注册一次
		registerConverters();
	}
	
	/**
	 * 注册一些转换器规则
	 * @notes   registerConverters(这里用一句话描述这个方法的作用) 
	 * @author Tony.z
	 * @date  2015-11-17 下午3:19:14
	 */
	public static void registerConverters(){
		ConvertUtils.register(new DateLocaleConverter(), Date.class);//特殊类时间类型的转换
		ConvertUtils.register(new LongConverter(null), Long.class); //作用默认值为null 而不是0 
	    ConvertUtils.register(new ShortConverter(null), Short.class);   //作用默认值为null 而不是0
	    ConvertUtils.register(new IntegerConverter(null), Integer.class); //作用默认值为null 而不是0
	    ConvertUtils.register(new DoubleConverter(null), Double.class);   //作用默认值为null 而不是0
	    ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);//作用默认值为null 而不是0
	}
	
	/**
	 * 
	 * @notes 将request里面的数据转换为实体 参数名一样
	 * @param clazz 想要转换为数据的类型
	 * @param request
	 * @author Tony.z
	 * @date  2015-11-17 下午3:19:11
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T request2Bean(Class<T> clazz,HttpServletRequest request){
			try{
				T t = clazz.newInstance();
				Enumeration e = request.getParameterNames();
				while(e.hasMoreElements()){
					String name = (String) e.nextElement();
					String value = request.getParameter(name);
					BeanUtils.setProperty(t, name, value);
				}
				return t;
			}catch (Exception e) {
				LOGGER.error("WebUtils request2Bean error", e);
				throw new RuntimeException(e);
			}
	}
	
	/**
	 * 
	 * @notes 复制对象属性
	 * @param dest 目的对象
	 * @param orig 原对象
	 * @author Tony.z
	 * @date  2015-11-17 下午3:24:00
	 */
	public static void copyProperties(Object dest, Object orig){
		try {
			BeanUtils.copyProperties(dest, orig);
		} catch (Exception e) {
			LOGGER.error("WebUtils copyProperties error", e);
			throw new RuntimeException(e);
		}
	}
	
}
