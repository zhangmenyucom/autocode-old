package com.taylor.api.common.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * cookie 相关操作类
 * @author wing.wang
 *
 */
public class CookieUtils{
	

	private static final Random random = new Random();

	private final static Logger LOG = LoggerFactory.getLogger("CookieUtils");
	
	
	private CookieUtils(){
		
	}

	/**
	 * 写cookie
	 * @param name 		cookie 名称
	 * @param value		明文值
	 * @param age		过期时间 秒
	 * @param path		路径
	 * @param response
	 */
	public static void setCookie(String name,String value,int age,String path,HttpServletResponse response){
		
		try{
			Cookie cookie = new Cookie(name,URLEncoder.encode(value,"utf-8"));
			cookie.setMaxAge(age);
			cookie.setPath(path);
			response.addCookie(cookie);
		}catch (Exception e){
			LOG.error("set cookie error {},{}",value,e);
		}
	}
	
	public static void setCookie(String name,String value,int age,String path,String domain,HttpServletResponse response){
		
		try{
			Cookie cookie = new Cookie(name,URLEncoder.encode(value,"utf-8"));
			cookie.setMaxAge(age);
			cookie.setPath(path);
			cookie.setDomain(domain);
			response.addCookie(cookie);
		}catch (Exception e){
			LOG.error("set cookie error {},{}",value,e);
		}
	}
	/**
	 * 通过cookie name 获得值
	 * @param name
	 */
	public static String getCookie(String name,HttpServletRequest request){
		try{
			if (StringUtils.isBlank(name)) {
				return null;
			}
			
			Cookie[] cookies = request.getCookies();
			if(null != cookies && cookies.length>0){
				for(Cookie c : cookies){
					if(name.equals(HorizonObjTools.obj2String(c.getName()))){
						return URLDecoder.decode(c.getValue(),"utf-8");
					}
				}
			}else{
				return null;
			}
		}catch(Exception e){
			LOG.error("get cookie error {},{}",name,e);
		}
		return null;
	}
	
	/**
	 * 清除cookie值
	 * @param name
	 */
	public static String clearCookies(HttpServletRequest request,HttpServletResponse response){
		try{
			Cookie[] cookies = request.getCookies();
			if(null != cookies && cookies.length>0){
				for(Cookie c : cookies){
					c.setMaxAge(0);
					c.setDomain("51offer.com");
					c.setPath("/");
					c.setValue("");
					response.addCookie(c);
				}
			}else{
				return null;
			}
		}catch(Exception e){
			LOG.error("error", e);
		}
		return null;
	}
	
	/**
	 * @notes:删除指定domain域、路径path下的所有cookies
	 *
	 * @param path			cookie路径
	 * @param domain		域
	 * @param request
	 * @param response
	 * @author	taylor
	 * 2014-5-19	下午4:01:32
	 */
	public static void clearCookies(String path, String domain, HttpServletRequest request, HttpServletResponse response){
		try{
			Cookie[] cookies = request.getCookies();
			if(null != cookies && cookies.length>0){
				for(Cookie c : cookies){
					c.setMaxAge(0);
					c.setDomain(domain);
					c.setPath(path);
					c.setValue("");
					response.addCookie(c);
				}
			}else{
				return;
			}
		}catch(Exception e){
			LOG.error("error", e);
		}
	}
	
	/**
	 * @notes:设置全站用户唯一标识
	 *
	 * @param request
	 * @param response
	 * @author	taylor
	 * 2014-8-20	上午10:29:58
	 */
	public static String setSysCookieId(HttpServletRequest request, HttpServletResponse response) {
		String cookieId = getCookie("cookieId", request);
		if (StringUtils.isBlank(cookieId)) {
			cookieId = String.valueOf(random.nextLong() * 100000);
			cookieId = MD5Util.getMD5Code(cookieId);
			setCookie("cookieId", cookieId, 365 * 24 * 3600, "/", "51offer.com", response);
		}
		return cookieId;
	}
}
