package com.auto.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;


public class TaylorOgnl {
	
	private TaylorOgnl(){
		
	}
	
	public static boolean isNotEmpty(Object o) {
		if(o == null){
			return false;
		} else if(o instanceof String){
			String temp = (String)o;
			if("".equals(temp) || "null".equals(temp)){
				return false;
			}else{
				return true;
			}
		} else{
			return true;
		}
	}
	public static boolean isEmpty(Object o) {
		if(o == null){
			return true;
		} else if(o instanceof String){
			String value = o.toString();
			if (StringUtils.isBlank(value) || "null".equals(value)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isNotEmptyList(List<Object> list) {
		if(null == list){
			return false;
		} else if(list.isEmpty()){
			return false;
		} else{
			return true;
		}
	}
	
	public static boolean isNotEmpty4EqualNumber(Object o, Number n) {
		if(o == null || null == n){
			return false;
		}
		
		if(o instanceof String) {
			String temp = (String) o;
			if("".equals(temp) || "null".equals(temp) || StringUtils.isBlank(temp)) {
				return false;
			} else if(new Float(temp).equals(n.floatValue())) {
				return false;
			} else {
				return true;
			}
		}
		
		if(o instanceof Number) {
			Number temp = (Number) o;
			if (new Float(0).equals(temp) || new Float(0).equals(n)) {
				return false;
			} else if(temp.equals(n)) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
}
