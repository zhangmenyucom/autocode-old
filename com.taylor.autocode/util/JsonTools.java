package com.taylor.api.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

/**
 * @notes:JSON工具类
 *
 * @author taylor
 *
 * 2014-6-23	下午9:23:52
 */
public class JsonTools {
	
	private static final Logger LOG = Logger.getLogger(JsonTools.class);

	/**
	 * 将一个实体类对象转化成JSON数据格式,等效于object2json	@param obj 实体类对象		@return JSON数据格式字符串
	 */
	public static String pojo2json(Object obj) {
		return JSONObject.fromObject(obj).toString(1);
	}

	/**
	 * 将数组集合等对象转换成JSON字符串	@param list
	 */
	public static String object2json(Object list) {
		return JSONSerializer.toJSON(list).toString(1);
	}

	/**
	 * 将Map准换为JSON字符串,等效于object2json()	@param map map集合	@return JSON字符串
	 */
	public static String map2json(Map<?, ?> map) {
		JSONObject object = JSONObject.fromObject(map);
		return object.toString(1);
	}

	/**
	 * 将xml字符串转换为JSON字符串		@param xmlString xml字符串	@return JSON对象
	 */
	public static String xml2json(String xmlString) {
		XMLSerializer xmlSerializer = new XMLSerializer();
		JSON json = xmlSerializer.read(xmlString);
		return json.toString(1);
	}

	/**
	 * 读取XML文件准换为JSON字符串		@param xmlFile XML文件	@return JSON字符串
	 */
	public String xmlFile2json(String xmlFile) {
		InputStream is = this.getClass().getResourceAsStream(xmlFile);
		String xml;
		String json = null;
		try 
		{
			xml = IOUtils.toString(is);
			json = xml2json(xml);
		} catch (IOException e) {
			LOG.error("error", e);
		}
		return json;
	}

	/**
	 * 将Json格式的字符串转换成指定的对象返回	@param jsonStr 要转化的Json格式的字符串	@param javaBean 指定转化对象类型	@return 转化后的对象
	 */
	@SuppressWarnings("rawtypes")
	public static Object json2pojo(String jsonStr, Class javaBean) {
		JSONObject jsonObj = JSONObject.fromObject(jsonStr);
		Object obj = JSONObject.toBean(jsonObj, javaBean);
		return obj;
	}

	/**
	 * 将Json格式的字符串转换成Map对象	@param jsonString JSON数据格式字符串	@return map集合
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> json2Map(String jsonString) {
		return (Map<String, Object>) json2pojo(jsonString, Map.class);
	}

	/**
	 * 将Json格式的字符串转换成对象数组返回	@param jsonString JSON数据格式字符串	@return 对象数组
	 */
	public static Object[] json2ObjectArray(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return jsonArray.toArray();
	}

	/**
	 * 将Json格式的字符串转换成指定对象组成的List返回	@param jsonString	JSON数据格式字符串	@param pojoClass 指定转化对象类型	@return list集合
	 */
	@SuppressWarnings({ "rawtypes"})
	public static List jsonArray2List(String jsonString, Class pojoClass) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return (List) JSONArray.toCollection(jsonArray, pojoClass);
	}

	/**
	 * pojo，集合，数组等对象转换成XML字符串	@param obj
	 */
	public static String obj2xml(Object obj) {
		XMLSerializer xmlSerializer = new XMLSerializer();
		return xmlSerializer.write(JSONSerializer.toJSON(obj));
	}

	/**
	 * JSON(数组)字符串转换成XML字符串	@param jsonString JSON(数组)字符串
	 */
	public static String json2xml(String jsonString) {
		XMLSerializer xmlSerializer = new XMLSerializer();
		return xmlSerializer.write(JSONSerializer.toJSON(jsonString));
	}
}