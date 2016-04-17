package com.taylor.api.common.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.taylor.api.common.xss.XssSecurityManager;

/**
 * @notes:文件上传下载工具类
 * 
 * @author： zzh
 * 
 *          2014-5-12 下午3:40:42
 */
public class FileUploadOrDowUtil {

	private FileUploadOrDowUtil() {

	}

	public static final Logger LOGGER = Logger.getLogger(FileUploadOrDowUtil.class);
	
	private static final int BUFFER = 1024;
	
	public static String uploadFile4Common(HttpServletRequest request,String chanelName,String saveToQiniu,String formFileName,boolean addStampFlag){
	    String rejson = "";
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String stampContent = "51offer";
        /**
         * 获取上传文件对象
         */
        CommonsMultipartFile uploadFile = (CommonsMultipartFile) multipartRequest.getFile(formFileName);
        String suffix = uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf("."), uploadFile.getOriginalFilename().length());
        String serverFileName = System.currentTimeMillis() + suffix;
        String contextPath = request.getSession().getServletContext().getRealPath("/") + "fileupload/";
        try {
            /**
             * 文件真实名字
             * 1、realFileName:获取文件的正式名字
             * 2、serverFileNmae:生成服务器上文件的名字
             */
            File localFileDir = new File(contextPath);
            if (!localFileDir.exists()) {
                localFileDir.mkdir();
            }
            File localServerFile = new File(contextPath +serverFileName);
            FileUtils.copyInputStreamToFile(uploadFile.getInputStream(), localServerFile);
            /**
             * 添加水印
             */
            if(addStampFlag){
                boolean flag = addStamp4Img(stampContent,contextPath + serverFileName);
                if(flag){
                    rejson = simpleFileUpload(uploadFile.getOriginalFilename(), serverFileName, contextPath, chanelName, saveToQiniu);
                }
            }else{
                rejson = simpleFileUpload(uploadFile.getOriginalFilename(), serverFileName, contextPath, chanelName, saveToQiniu);
            }
        } catch (Exception e) {
            LOGGER.error("《《《《《《《《《调用BaseAction中的uploadFile4Common方法失败", e);
            return rejson;
        }
        return rejson;
    }

	/**
	 * 
	 * @notes:单个文件上传
	 * @author：zzh
 	 * @param realFileName:文件的真实名称
	 * @param serverFileName：服务器上文件名称
	 * @param contextPath:本地服务器路径
	 * @param chanelName:频道号
	 * @param saveToQiniu：是否上传到七牛(可为空)
	 * @return jsonString
	 * 2015-4-14 下午5:29:06
	 */
	public static String simpleFileUpload(String realFileName,String serverFileName,String contextPath,String chanelName,String saveToQiniu){
	    
	    Map<String,Object> returnMap = new HashMap<String,Object>();
		/**
		 * realFileName:文件的真实名称
		 * serverFileName：服务器上文件名称
		 * contextPath:本地服务器路径
		 * chanelName:频道号
		 * saveToQiniu：是否上传到七牛
		 * downLoadFile2ServerUrl:服务器下载文件路径
		 */
		saveToQiniu = StringUtils.isBlank(HorizonObjTools.obj2String(saveToQiniu))?"":HorizonObjTools.obj2String(saveToQiniu);
		String downLoadFile2ServerUrl = "";
		/**
		 * 初始化上传工具
		 */
		HttpClient client = new HttpClient();
		PostMethod filePost = new PostMethod();
		
		File localFile = new File("");
		try {
			
			/**
			 * 加载配置文件
			 */
			Properties p = new Properties();
			InputStream ins = FileUploadOrDowUtil.class.getClassLoader().getResourceAsStream("/config/second.properties");
			p.load(ins);
			
			/**
			 * 验证非法文件名
			 */
			if (XssSecurityManager.matches(realFileName)) {
				LOGGER.error("<<<<<<<<<<<<<<<<<<< 非法文件名 realFileName:>>>>>>>>>>>>>>>>" + realFileName);
				returnMap.put("fileName", realFileName);
				returnMap.put("downLoadFile2ServerUrl", null);
				returnMap.put("uploadFlag", false);
				return JsonTools.object2json(returnMap);
			}
			
			/**
			 * 创建路径文件（本地路径+服务器上文件名称）
			 */
			localFile = new File(contextPath + serverFileName);
			
			/**
		    * 开始上传
		    */
			filePost = new PostMethod(p.getProperty("file_upload_address"));
			if(StringUtils.isNotBlank(saveToQiniu)){
				Part[] part = {new StringPart("chanelName", chanelName),new FilePart(localFile.getName(), localFile), new StringPart("saveToQiniu",saveToQiniu)};
				filePost.setRequestEntity(new MultipartRequestEntity(part, filePost.getParams()));
			}else{
				Part[] part = {new StringPart("chanelName", chanelName),new FilePart(localFile.getName(), localFile)};
				filePost.setRequestEntity(new MultipartRequestEntity(part, filePost.getParams()));
			}
			client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
			LOGGER.error("《《《《《《《《《《《《《《《《《开始处理上传》》》》》》》》》》》》》》》》》》》");
			
			/**
			 * 验证上传状态
			 */
			
			int status = client.executeMethod(filePost);
			
			/**
			 * 验证服务是否存在附件
			 * retureValue：服务器返回路径
			 * downLoadFile2ServerUrl：从服务下载文件路径
			 */
			if(StringUtils.isNotBlank(saveToQiniu)){
				downLoadFile2ServerUrl = filePost.getResponseBodyAsString().replaceAll("\"", "");
			}else{
				String downLoadFile2Server = p.getProperty("file_download_address");
				String retureValue = filePost.getResponseBodyAsString().replaceAll("\"", "");
				retureValue = retureValue.substring(1,retureValue.length());
				downLoadFile2ServerUrl = downLoadFile2Server + retureValue;
			}
			/**
			 * 验证服务器是否存在
			 */
			GetMethod httpGet = new GetMethod(downLoadFile2ServerUrl);
			int downLoadFlag = client.executeMethod(httpGet);
			
			/**
			 * 上传成功后删除本地文件
			 */
			if (status == HttpStatus.SC_OK && downLoadFlag == HttpStatus.SC_OK) {
				LOGGER.error("《《《《《《《《《《《《《《《《《上传成功验证通过》》》》》》》》》》》》》》》》》》》");
				returnMap.put("fileName", realFileName);
				returnMap.put("downLoadFile2ServerUrl", downLoadFile2ServerUrl);
				returnMap.put("uploadFlag", true);
			}
			return JsonTools.object2json(returnMap);
		} catch (Exception e) {
			LOGGER.error("《《《《《《《《《《《《《《《《《上传异常：realFileName：》》》》》》》》》》》》》》》》》》》" + realFileName, e);
			returnMap.put("fileName", realFileName);
			returnMap.put("downLoadFile2ServerUrl", downLoadFile2ServerUrl);
			returnMap.put("uploadFlag", false);
			return JsonTools.object2json(returnMap);
		}finally{
			/**
			 * 如果本地文件存在则删除本地服务器文件
			 */
			if (localFile.exists()) {
				localFile.delete();
			}
			/**
			 * 关闭打开的Http
			 */
			LOGGER.error("《《《《《《《《《《《《《《《《《finally》》》》》》》》》》》》》》》》》》》");
			client.getHttpConnectionManager().closeIdleConnections(0);
			filePost.releaseConnection();
		}
	}
	
	/**
	 * 
	 * @notes:多文件上传
	 *
	 * @author：zzh
	 *
	 * @param:paramMap{ 
				 	 * realFileName:文件的真实名称
					 * serverFileName：服务器上文件名称
					 * contextPath:本地服务器路径
					 * chanelName:频道号
					 * saveToQiniu：是否上传到七牛
					 * }
	 * 2015-4-14 下午5:29:06
	 * @throws IOException 
	 */
	public static Map<String,Object> multipleFileUpload(List<Map<String,Object>> paramListMap){
		/**
		 * resultListMap:返回的集合List
		 * resultMap:返回的单个Map，最終會放入resultListMap
		 * returnMap：最终返回给请求方的Map
		 * paramMap:传入的参数Map
		 * localFileList:聲明的本地文件集合，用戶出異常或者上傳完成后刪除本地穩健 
		 */
		List<Map<String,Object>> resultListMap = new ArrayList<Map<String,Object>>(0);
		Map<String,Object> returnMap = new HashMap<String,Object>(0);
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		List<File> localFileList = new ArrayList<File>();
		/**
		 * 初始化上传工具
		 */
		HttpClient client = new HttpClient();
		PostMethod filePost = new PostMethod();
		
		String downLoadFile2ServerUrl = "";
		
		try {
			for(int i = 0 ;i<paramListMap.size();i++){
				resultMap = paramListMap.get(i);
				Map<String,Object> paramMap = new HashMap<String, Object>(0);
				/**
				 * realFileName:文件的真实名称
				 * serverFileName：服务器上文件名称
				 * contextPath:本地服务器路径
				 * chanelName:频道号
				 * saveToQiniu：是否上传到七牛
				 * downLoadFile2ServerUrl:服务器下载文件路径
				 */
				String realFileName = HorizonObjTools.obj2String(paramMap.get("realFileName"));
				String serverFileName = HorizonObjTools.obj2String(paramMap.get("serverFileName"));
				String contextPath = HorizonObjTools.obj2String(paramMap.get("contextPath"));
				String chanelName = HorizonObjTools.obj2String(paramMap.get("chanelName"));
				String saveToQiniu = HorizonObjTools.obj2String(paramMap.get("saveToQiniu"));
				
				File localFile = new File("");
				
				/**
				 * 加载配置文件
				 */
				Properties p = new Properties();
				InputStream ins = FileUploadOrDowUtil.class.getClassLoader().getResourceAsStream("/config/second.properties");
				p.load(ins);
				
				/**
				 * 验证非法文件名
				 */
				if (XssSecurityManager.matches(realFileName)) {
					LOGGER.error("<<<<<<<<<<<<<<<<<<< 非法文件名 >>>>>>>>>>>>>>>>");
					returnMap.put("uploadFlag", false);
					return returnMap;
				}
				
				/**
				 * 创建路径文件（本地路径+服务器上文件名称）
				 */
				localFile = new File(contextPath + serverFileName);
				localFileList.add(localFile);
				
				/**
			    * 开始上传
			    */
				filePost = new PostMethod(p.getProperty("file_upload_address"));
				Part[] part = {new StringPart("chanelName", chanelName),new FilePart(localFile.getName(), localFile), new StringPart("saveToQiniu",saveToQiniu)};
				filePost.setRequestEntity(new MultipartRequestEntity(part, filePost.getParams()));
				client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
				LOGGER.error("《《《《《《《《《《《《《《《《《开始处理上传》》》》》》》》》》》》》》》》》》》");
				
				/**
				 * 验证上传状态
				 */
				
				int status = client.executeMethod(filePost);
				
				/**
				 * 验证服务是否存在附件
				 * retureValue：服务器返回路径
				 * downLoadFile2ServerUrl：从服务下载文件路径
				 */
				String downLoadFile2Server = p.getProperty("file_download_address");
				String retureValue = filePost.getResponseBodyAsString().replaceAll("\"", "");
				retureValue = retureValue.substring(1,retureValue.length());
				downLoadFile2ServerUrl = downLoadFile2Server + retureValue;
				
				/**
				 * 验证服务器是否存在
				 */
				GetMethod httpGet = new GetMethod(downLoadFile2ServerUrl);
				int downLoadFlag = client.executeMethod(httpGet);
				LOGGER.error("《《《《《《《《《《《《《《《《《上传成功验证通过》》》》》》》》》》》》》》》》》》》");
				
				/**
				 * 上传成功后删除本地文件
				 */
				if (status == HttpStatus.SC_OK && downLoadFlag == HttpStatus.SC_OK) {
					resultMap.put("fileName", realFileName);
					resultMap.put("downLoadFile2ServerUrl", downLoadFile2ServerUrl);
					resultListMap.add(resultMap);
				}
			}
			returnMap.put("uploadFlag", true);
			returnMap.put("resultListMap", resultListMap);
			return returnMap;
		}catch (IOException e) {
			LOGGER.error("《《《《《《多文件上传失败》》》》》》》》", e);
			returnMap.put("uploadFlag", false);
			return returnMap;
		}finally{
			/**
			 * 如果本地文件存在则删除本地服务器文件
			 */
			if(!localFileList.isEmpty()){
				for(int i=0;i<localFileList.size();i++){
					if (localFileList.get(i).exists()) {
						localFileList.get(i).delete();
					}
				}
			}
			/**
			 * 关闭打开的Http
			 */
			LOGGER.error("《《《《《《《《《《《《《《《《《finally》》》》》》》》》》》》》》》》》》》");
			client.getHttpConnectionManager().closeIdleConnections(0);
			filePost.releaseConnection();
		}
	}
	
	/**
	 * 
	 * @notes 单个文件下载至中转站
	 * @author zzh
     * @param realFileName：文件这是名字
     * @param contextPath:本地服务器路径
     * @param serverFilePath:服务器文件路径
	 * 2015-1-23 上午10:03:56
	 */
	public static boolean downLoadFile2Temp(String realFileName,String contextPath , String serverFilePath){
		
		/**
		 * 本地中转站路径
		 */
		File fileDir = new File(contextPath);
		if(!fileDir.exists()){
			fileDir.mkdir();
		}
		HttpClient client = new HttpClient();
		GetMethod httpGet = new GetMethod(serverFilePath);
		/**
		 * 开始下载至本地中转站
		 */
		try {
			/**
			 * 读取
			 */
			File downloadRealFile = new File(contextPath + "/" + realFileName);
			client.executeMethod(httpGet);
			InputStream in = httpGet.getResponseBodyAsStream();
			/**
			 * 输出至本地
			 */
			FileOutputStream out = new FileOutputStream(downloadRealFile);
			byte[] b = new byte[BUFFER];
			int len = 0;
			while ((len = in.read(b)) != -1) {
				out.write(b, 0, len);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			LOGGER.error("<------------------------------------文件下载至中转站失败------------------------------------------->", e);
			return false;
		} finally {
			httpGet.releaseConnection();
			client.getHttpConnectionManager().closeIdleConnections(0);
		}
		return true;
	}
	
	/**
	 * 
	 * @notes:多个文件下载至中转站
	 *
	 * @author：zzh
	 *
	 * @param:paramMap:文件Map
	 *
	 * 2015-1-23 上午10:03:56
	 */
	public static List<Object> multipleDownLoadFile2Temp(List<Map<String,Object>> paramListMap){
		List<Object> reList = new ArrayList<Object>(0);
		HttpClient client = new HttpClient();
		GetMethod httpGet = new GetMethod();
		try{
			for(int i=0;i<paramListMap.size();i++){
				Map<String,Object> paramMap = paramListMap.get(i);
				/**
				 * realFileName：文件这是名字
				 * contextPath:本地服务器路径
				 * serverFilePath:服务器文件路径
				 */
				String realFileName = HorizonObjTools.obj2String(paramMap.get("realFileName"));
				String contextPath = HorizonObjTools.obj2String(paramMap.get("contextPath"));
				String serverFilePath = HorizonObjTools.obj2String(paramMap.get("serverFilePath"));
				/**
				 * 本地中转站路径
				 */
				File fileDir = new File(contextPath);
				if(!fileDir.exists()){
					fileDir.mkdir();
				}
				File downloadRealFile = new File(contextPath + "/" + realFileName);
				httpGet = new GetMethod(serverFilePath);
				/**
				 * 读取
				 */
				client.executeMethod(httpGet);
				InputStream in = httpGet.getResponseBodyAsStream();
				/**
				 * 输出至本地
				 */
				FileOutputStream out = new FileOutputStream(downloadRealFile);
				byte[] b = new byte[BUFFER];
				int len = 0;
				while ((len = in.read(b)) != -1) {
					out.write(b, 0, len);
				}
				in.close();
				out.close();
				reList.add(downloadRealFile);
			}
		}catch (Exception e) {
			LOGGER.error("<------------------------------------文件下载至中转站失败------------------------------------------->", e);
			return reList;
		} finally {
			httpGet.releaseConnection();
			client.getHttpConnectionManager().closeIdleConnections(0);
		}
		return reList;
	}
	
	/**
     * 
     * @notes 文件下载
     * @author herny
     * @param realFileName:文件的真实名称
     * @param contextPath:本地服务器路径
     * 2014-11-19 下午4:17:10
     */
    public static boolean downLoadFile(Map<String,Object> paramMap,HttpServletRequest request, HttpServletResponse response){
        
        /**
         * realFileName:文件的真实名称
         * contextPath:本地服务器路径
         */
        String realFileName = HorizonObjTools.obj2String(paramMap.get("realFileName"));
        String contextPath = HorizonObjTools.obj2String(paramMap.get("contextPath"));
        /**
         * 验证本地文件是否存在
         */
        try {
            File downloadRealFile = new File(contextPath + realFileName);
            /**
             * 开始下载
             */
            InputStream fis = new BufferedInputStream(new FileInputStream(downloadRealFile));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            /**
             * 设置下载出来的文件名字
             */
            if (request.getHeader("User-Agent").indexOf("MSIE") != -1) {
                String fn;
                fn = URLEncoder.encode(realFileName, "utf-8");
                if (fn.length() > 150) {
                    fn = new String(realFileName.getBytes("GBK"), "ISO-8859-1");
                }
                response.addHeader("Content-Disposition", "attachment; filename=\"" + fn + "\"");
            } else {
                response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(realFileName.getBytes("utf-8"), "iso-8859-1") + "\"");
            }

            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream;charset=utf-8");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            if (downloadRealFile.exists()) {
                downloadRealFile.delete();
            }
            return true;
        }catch(Exception e){
            LOGGER.error("《《《《《《《调用BaseAction中的downLoadFile方法异常", e);
            return false;
        }
    }
	
	/**
	 * @notes:图片添加水印
	 * @param stampContent：水印内容
	 * @param targetImgPath：文件路径
	 * @return: boolean
	 * @author: henry
	 *
	 * 2015年10月15日		下午3:51:54
	 */
	public static boolean addStamp4Img(String stampContent, String targetImgPath) {
        try {
			File file = new File(targetImgPath);
			Image src = ImageIO.read(file);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            int fontSize = (int) (width * 0.2);
            BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics(); 
            g.drawImage(src, 0, 0, width, height, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,0.1f));  
            g.setColor(Color.BLACK);
            g.setFont(new Font("宋体", Font.PLAIN, fontSize));
            g.drawString(stampContent, width /2 -  width/(stampContent.length()/2), height/ 2 + (int)(height/(stampContent.length())*0.7));
            g.drawString(stampContent, width /2 -  width/(stampContent.length()/2), height/ 2 + (int)(height/(stampContent.length())*0.7));
            g.dispose();
            String formatName = targetImgPath.substring(targetImgPath.lastIndexOf(".") + 1);
            ImageIO.write(image, formatName , new File(targetImgPath) );  
            return true;
        } catch (Exception e) {
			LOGGER.error("《《《《《《addStamp4Img----》IOException》》》》》》》》", e);
            return false;
        }
    }
    /**   
     * 给图片添加水印   
     * @param iconPath 水印图片路径   
     * @param srcImgPath 源图片路径   
     * @param targerPath 目标图片路径   
     */    
    public static boolean markImageByIcon(String iconPath, String srcImgPath, String targerPath) {     
        return markImageByIcon(iconPath, srcImgPath, targerPath, 45) ;   
    }     
    /**   
     * 给图片添加水印、可设置水印图片旋转角度   
     * @param iconPath 水印图片路径   
     * @param srcImgPath 源图片路径   
     * @param targerPath 目标图片路径   
     * @param degree 水印图片旋转角度 
     */    
    public static boolean markImageByIcon(String iconPath, String srcImgPath,String targerPath, Integer degree) {     
        OutputStream os = null;     
        try {     
            Image srcImg = ImageIO.read(new File(srcImgPath));   
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);   
			// 得到画笔对象
            Graphics2D g = buffImg.createGraphics();     
            // 设置对线段的锯齿状边缘处理     
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);     
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);     
            if (null != degree) {     
                // 设置水印旋转     
                g.rotate(Math.toRadians(degree),(double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);     
            }     
            // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度    
            ImageIcon imgIcon = new ImageIcon(iconPath);     
            // 得到Image对象。     
            Image img = imgIcon.getImage();     
            float alpha = 0.2f; // 透明度     
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,alpha));     
            // 表示水印图片的位置     
            g.drawImage(img, (buffImg.getWidth(null) - img.getWidth(null)) / 2, (buffImg.getHeight(null)- img.getHeight(null))/ 2, null);     
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));     
            g.dispose();     
            os = new FileOutputStream(targerPath);     
            // 生成图片     
            ImageIO.write(buffImg, "JPG", os);  
            return true;
        } catch (Exception e) {     
			LOGGER.error("《《《《《《markImageByIcon----》Exception》》》》》》》》", e);
        	return false;
        } finally {     
            try {     
                if (null != os)     
                    os.close();     
            } catch (Exception e) {     
				LOGGER.error("《《《《《《markImageByIcon----》Exception》》》》》》》》", e);
            }     
        }
    }     
}
