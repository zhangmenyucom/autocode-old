package com.taylor.api.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

/**
 * @notes:HttpClient工具类
 *
 * @author taylor
 *
 * 2014-6-18	下午4:34:19
 */
public class HttpClientUtil {
	
	private static final Logger LOG = Logger.getLogger(HttpClientUtil.class);

	/**
	 * 最大超时时间
	 */
	private static final Integer CONNECTION_TIME_OUT = new Integer(10000);
	
	private HttpClientUtil(){
		
	}
	
	/**
	 * @notes:HttpClient Get Method
	 *
	 * @param url			请求URL
	 * @param paramMap		请求参数
	 * @author	taylor
	 * 2014-6-18	下午5:06:12
	 */
	public static String get(String url, Map<String, Object> paramMap) {
		
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIME_OUT);
		GetMethod httpGet = null;
		InputStream responseIn = null;
		BufferedReader inBR = null;
		try {
			if (null != paramMap && !paramMap.isEmpty()) {
				Iterator<Entry<String, Object>> itr = paramMap.entrySet().iterator();
				while (itr.hasNext()) {
					Entry<String, Object> entry = itr.next();
					String key = entry.getKey();
					String value = entry.getValue().toString();
					if (url.endsWith("?")) {
						url += "";
					} else if(url.indexOf("?") > 0) {
						url += "&";
					} else {
						url += "?";
					}
					url += key + "=" + URLEncoder.encode(value, "UTF-8");
				}
			}
	    	
	    	/**
	    	 * HttpGet发送搜索请求
	    	 */
			LOG.info("HttpClient.get URL=" + url);
	    	httpGet = new GetMethod(url);
	    	
			client.executeMethod(httpGet);
			responseIn = httpGet.getResponseBodyAsStream();
			inBR = new BufferedReader(new InputStreamReader(responseIn,"UTF-8"));
			StringBuilder buffer = new StringBuilder(0);
			String line = "";
			while ((line = inBR.readLine()) != null){
				buffer.append(line);
			}
			
			LOG.info(buffer.toString());
			
			return buffer.toString();
		} catch (HttpException e) {
			LOG.error(e);
		} catch (IOException e) {
			LOG.error(e);
		} finally {
			try {
				if (null != responseIn){
					responseIn.close();
				}
				if (null != inBR) {
					inBR.close();
				}
			} catch (IOException e) {
				LOG.error(e);
			}
			if (null != httpGet){
				httpGet.releaseConnection();
			}
		}
		
		return null;
	}
	
	/**
	 * @notes:HttpClient POST Method
	 *
	 * @param url			请求URL
	 * @param paramMap		请求参数
	 * @author	taylor
	 * 2014-6-18	下午5:25:54
	 */
	public static String post(String url, Map<String, Object> paramMap) {
		
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIME_OUT);
		PostMethod httpPost = null;
		InputStream responseIn = null;
		BufferedReader inBR = null;
		try {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>(0);
			if (null != paramMap && !paramMap.isEmpty()) {
				Iterator<Entry<String, Object>> itr = paramMap.entrySet().iterator();
				while (itr.hasNext()) {
					Entry<String, Object> entry = itr.next();
					String key = entry.getKey();
					String value = entry.getValue().toString();
					NameValuePair valuePair = new NameValuePair(key, value);
					nvps.add(valuePair);
				}
			}
	    	
	    	/**
	    	 * PostMethod发送搜索请求
	    	 */
			httpPost = new PostMethod(url);
			httpPost.setRequestBody(nvps.toArray(new NameValuePair[nvps.size()]));
	    	
			int statusCode = client.executeMethod(httpPost);
			if (HttpStatus.SC_OK != statusCode) {
				return null;
			}
			responseIn = httpPost.getResponseBodyAsStream();
			inBR = new BufferedReader(new InputStreamReader(responseIn));
			StringBuilder buffer = new StringBuilder(0);
			String line = "";
			while ((line = inBR.readLine()) != null){
				buffer.append(line);
			}
			
			LOG.info(buffer.toString());
			
			return buffer.toString();
		} catch (HttpException e) {
			LOG.error(e);
		} catch (IOException e) {
			LOG.error(e);
		} finally {
			try {
				if (null != responseIn) {
					responseIn.close();
				}
				if (null != inBR) {
					inBR.close();
				}
			} catch (IOException e) {
				LOG.error(e);
			}
			if (null != httpPost){
				httpPost.releaseConnection();
			}
		}
		
		return null;
	}
}
