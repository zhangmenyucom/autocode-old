package com.taylor.api.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.OperationTimeoutException;

public class MemcachedManager {
	
	private static final  Logger LOG = LoggerFactory.getLogger(MemcachedManager.class);
	private static final String SEPARATOR = "-";
	
	private static MemcachedManager instance;
	private MemcachedClient client;
	private String channel;
	private String addresses;
	
	
	private MemcachedManager() {
		LOG.info("initClient memcached");
		Properties config = new Properties();
		InputStream input = null;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
        	input = cl.getResourceAsStream("config/memcached.properties");
        	Reader reader = new InputStreamReader(input, "UTF-8");
            config.load(reader);
        } catch (IOException e) {
        	LOG.error("memcache.properties load failed!", e);
        } finally {
        	if (null != input) {
        		try{
					input.close();
				} catch (Exception e) {
					LOG.error("memcache.properties load failed!", e);
				}
        	}
        }
        addresses = config.getProperty("memcahed.addr");
        channel = config.getProperty("channel");
		try {
			client = new MemcachedClient(AddrUtil.getAddresses(addresses));
		} catch (IOException e) {
			LOG.error("new MemcachedClient failed!", e);
		}
	}

	/**
	 * 单例
	 * spy MemcachedClient 是一种nio方式的连接，为避免原则性操作问题，整个应用只允许打开一个连接
	 */
	public static synchronized MemcachedManager getInstance() {
		if (null == instance) {
			instance = new MemcachedManager();
		}
		return instance;
	}
	

	@PreDestroy
	public void destroy() {
		client.shutdown();
		LOG.info("destroy memcached");
	}
	
	public Object get(String key){
		try {
			Assert.notNull(key, "访问memcache的key不可为空");
			String realKey = composeKey(key);
			Object value = client.get(realKey);
			LOG.debug("get value from memcache key:{}", realKey);
			if (value == null) {
				return null;
			}
			return value;
		} catch (OperationTimeoutException e) {
			LOG.error("《《《《《获取K失败》》》》》",e);
			//如果Memcached连接超时，且客户端实还存在，先销毁原客户端
			if (null != client) {
				destroy();
				//重新生成一次客户端实例
				try {
					client =  new MemcachedClient(AddrUtil.getAddresses(addresses));
				} catch (IOException e1) {
					LOG.error("《《《《《获取K失败》》》》》",e1);
				}
			}
			//重新回调该方法
			return get(key);
		}
	}


	public void set(String key, Object value) throws IOException {
		set(key, 0, value);
	}

	public void delete(String key) {
		Assert.notNull(key, "删除memcache的key不可为空");
		String realKey = composeKey(key);
		LOG.debug("begin to delete value from memcache key:{}", realKey);
		client.delete(realKey);
	}

	/**
	 * 
	 * @param key
	 * @param expre 单位 秒
	 * @param value
	 * @throws IOException
	 */
	public void set(String key, int expre, Object value) throws IOException {
		Assert.notNull(key, "设置memcache的key不可为空");
		Assert.isTrue(expre >= 0, "expre time must >= 0");
		String realKey = composeKey(key);
		LOG.debug("begin to set value to memcache key:{}", realKey);
		client.set(realKey, expre, value);
	}
	/**
	 * 一个key只允许包含一条记录
	 * 不等待结果集返回
	 * 内部串行实现
	 * @param key
	 * @param expre 秒
	 * @param value
	 * @throws IOException
	 */
	public void addNoWait(String key, int expre, Object value) throws IOException {
		Assert.notNull(key, "设置memcache的key不可为空");
		Assert.isTrue(expre >= 0, "expre time must >= 0");
		String realKey = composeKey(key);
		LOG.debug("begin to addNoWait value to memcache key:{}", realKey);
		client.add(realKey, expre, value);
	}
	/**
	 * 一个key只允许包含一条记录
	 * 等待结果集返回
	 * 内部串行实现
	 * @param key
	 * @param expre 秒
	 * @param value
	 * @throws IOException
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public boolean addWait(String key, int expre, Object value) throws IOException, InterruptedException, ExecutionException {
		Assert.notNull(key, "设置memcache的key不可为空");
		Assert.isTrue(expre >= 0, "expre time must >= 0");
		String realKey = composeKey(key);
		LOG.debug("begin to addWait value to memcache key:{}", realKey);
		Future<Boolean> b = client.add(realKey, expre, value);
		return b.get();
	}
	protected String composeKey(String key) {
		String result = this.channel + SEPARATOR + key;
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		
	}
}
