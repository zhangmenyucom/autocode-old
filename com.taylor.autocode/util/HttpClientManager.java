package com.taylor.api.common.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.log4j.Logger;

public class HttpClientManager {
	
	private static final Logger LOG = Logger.getLogger(HttpClientUtil.class);

	private static HttpClientManager instance;
	private HttpClient client;
	
	private HttpClientManager(){
		this.client = new HttpClient();
	}
	
	public static HttpClientManager getInstance(){
		if(instance==null){
			instance=new HttpClientManager();
		}
		return instance;
	}
	
	/**
	 * 用法： HttpRequestProxy hrp = new HttpRequestProxy();
	 * hrp.doRequest("http://www.163.com",null,null,"gbk");
	 * 
	 * @param url 请求的ＵＲＬ
	 * @param postData POST请求时form表单封装的数据 没有时传null
	 * @param header request请求时附带的头信息(header) 没有时传null
	 * @param encoding response返回的信息编码格式 没有时传null
	 * @return response返回的文本数据
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String doRequest(String url, Map postData, Map header,String encoding){
		String responseString = null;
		// 头部请求信息
		Header[] headers = null;
		if (header != null) {
			Set entrySet = header.entrySet();
			int dataLength = entrySet.size();
			headers = new Header[dataLength];
			int i = 0;
			for (Iterator itor = entrySet.iterator(); itor.hasNext();) {
				Map.Entry entry = (Map.Entry) itor.next();
				headers[i++] = new Header(entry.getKey().toString(), entry.getValue().toString());
			}
		}
		// post方式
		if (postData != null) {
			PostMethod postRequest = new PostMethod(url.trim());
			if (headers != null) {
				for (int i = 0; i < headers.length; i++) {
					postRequest.setRequestHeader(headers[i]);
				}
			}
			Set entrySet = postData.entrySet();
			int dataLength = entrySet.size();
			NameValuePair[] params = new NameValuePair[dataLength];
			int i = 0;
			for (Iterator itor = entrySet.iterator(); itor.hasNext();) {
				Map.Entry entry = (Map.Entry) itor.next();
				params[i++] = new NameValuePair(entry.getKey().toString(),entry.getValue().toString());
			}
			postRequest.setRequestBody(params);
			try {
				responseString = executeMethod(postRequest, encoding);
			} catch (Exception e) {
				LOG.error("《《《《《《《《《调用HttpClientManager中的doRequest的方法异常》》》》》》》》", e);
			} finally {
				postRequest.releaseConnection();
			}
		}
		// get方式
		if (postData == null) {
			GetMethod getRequest = new GetMethod(url.trim());
			if (headers != null) {
				for (int i = 0; i < headers.length; i++) {
					getRequest.setRequestHeader(headers[i]);
				}
			}
			try {
				responseString = this.executeMethod(getRequest, encoding);
			} catch (Exception e) {
				LOG.error("《《《《《《《《《调用HttpClientManager中的doRequest的方法异常》》》》》》》》", e);
			} finally {
				if(getRequest!=null){
					getRequest.releaseConnection();
				}
			}
		}
		return responseString;
	}
	
	/**
	 * 
	 * @param url 请求url
	 * @return  返回状态码
	 * @throws Exception
	 */
	public int doRequest(String url){
		//默认请求超时状态
		int status = 0;
		PostMethod postRequest = new PostMethod(url.trim());
		//设置请求超时时间30秒
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30*1000); 
		client.getHttpConnectionManager().getParams().setSoTimeout(30*1000); 
		try {
			status = client.executeMethod(postRequest);
		} catch (Exception e) {
			LOG.error("《《《《《《《《《调用HttpClientManager中的doRequest的方法异常》》》》》》》》", e);
		}finally{
			if(postRequest!=null){
				postRequest.releaseConnection();
			}
		}
		return status;
	}
	
	/**
	 * 执行httpmethod
	 * @param request
	 * @param encoding
	 * @throws Exception
	 */
	private String executeMethod(HttpMethod request, String encoding){
		String responseContent = null;
		InputStream responseStream = null;
		BufferedReader rd = null;
		try {
			client.executeMethod(request);
			if (encoding != null) {
				responseStream = request.getResponseBodyAsStream();
				rd = new BufferedReader(new InputStreamReader(responseStream,encoding));
				String tempLine = rd.readLine();
				StringBuffer tempStr = new StringBuffer();
				String crlf = System.getProperty("line.separator");
				while (tempLine != null) {
					tempStr.append(tempLine);
					tempStr.append(crlf);
					tempLine = rd.readLine();
				}
				responseContent = tempStr.toString();
			} else
				responseContent = request.getResponseBodyAsString();

			Header locationHeader = request.getResponseHeader("location");
			// 返回代码为302,301时，表示页面己经重定向，则重新请求location的url，这在
			// 一些登录授权取cookie时很重要
			if (locationHeader != null) {
				String redirectUrl = locationHeader.getValue();
				doRequest(redirectUrl, null, null, null);
			}
		} catch (Exception e) {
			LOG.error("《《《《《《《《《调用HttpClientManager中的executeMethod的方法异常》》》》》》》》", e);
		} finally {
			if (rd != null)
				try {
					rd.close();
				} catch (Exception e) {
					LOG.error("《《《《《《《《《调用HttpClientManager中的executeMethod的方法异常》》》》》》》》", e);
				}
			if (responseStream != null)
				try {
					responseStream.close();
				} catch (Exception e) {
					LOG.error("《《《《《《《《《调用HttpClientManager中的executeMethod的方法异常》》》》》》》》", e);
				}
		}
		return responseContent;
	}

	public HttpClient getClient() {
		return client;
	}

	public void setClient(HttpClient client) {
		this.client = client;
	}
	
}
