package com.auto.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import freemarker.template.Version;

/**
 * @notes:freemark	工具类
 *
 * @author taylor
 *
 * 2015-7-14	下午10:25:27
 */
public class FreeMarkUtil {
	
	private static Configuration cfg;
	
	/**
	 * @notes:构造方法，处理初始模板加载
	 * @param sourcePath
	 * @auther taylor
	 * 2015-7-14	下午10:24:32
	 */
	@SuppressWarnings("deprecation")
	public static void init(String sourcePath) {
        try {
        	cfg = new Configuration(new Version("2.3.22"));  
			cfg.setDirectoryForTemplateLoading(new File(System.getProperty("user.dir") +  sourcePath));
			cfg.setObjectWrapper(new DefaultObjectWrapper()); 
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	

	/**
	 * @notes:处理模板输出
	 *
	 * @param fltName			模板文件
	 * @param filePath			输出目录
	 * @param fileName			输出文件
	 * @param dataMap			输出数据
	 * @author	taylor
	 * 2015-7-14	下午10:24:28
	 */
	public static void autoFileByFtl(String fltName, String filePath, String fileName, Map<String, Object> dataMap) {
  
        /* Get or create a template */  
		try {
			Template temp = cfg.getTemplate(fltName);
			    
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			  
			/* Merge data-model with template */
			OutputStream outputStream = new FileOutputStream(new File(filePath + "//" + fileName));
			Writer out = new OutputStreamWriter(outputStream);  
			temp.process(dataMap, out);
			out.flush();
		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}  
	}
}
