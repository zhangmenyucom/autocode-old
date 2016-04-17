package com.taylor.api.common.util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @notes:创建excl
 * @author henry
 *
 * 2016年1月6日		上午10:05:45
 */

public class ExcelExportUtil {

   /**
    * @notes:创建2007版Excel文件
    * @throws FileNotFoundException
    * @throws IOException: void:
    * @author: henry
    *
    * 2016年1月6日		上午10:05:26
    */
    @SuppressWarnings("rawtypes")
    public static void creat2007Excel(List<Map<String,Object>> dataMapList,String contextPath,HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException,IOException {
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet();// 创建一个工作薄对象
        sheet.setColumnWidth(1, 10000);// 设置第二列的宽度为
        XSSFCellStyle style = workBook.createCellStyle();// 创建样式对象
        XSSFFont font = workBook.createFont();// 创建字体对象
        font.setFontHeightInPoints((short) 15);// 设置字体大小
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);// 设置标准
        font.setFontName("黑体");// 设置为黑体字
        style.setFont(font);// 将字体加入到样式对象
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 水平居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);// 顶部边框粗线
        style.setTopBorderColor(HSSFColor.RED.index);// 设置为红色
        style.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);// 底部边框双线
        style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
        style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
        // 格式化日期
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        
        /**
         * 创建单元格
         */
        //获取集合中的第一个map的key用作标题行
        Map<String,Object> dataTitleMap = dataMapList.get(0);
        XSSFRow row = sheet.createRow(0);// 创建一个行对象
        row.setHeightInPoints(23);// 设置行高23像素
        Iterator iterator = dataTitleMap.entrySet().iterator();
        //从第一个单元格开始赋值
        int index = 0;
        while (iterator.hasNext()) {
            Entry entry = (java.util.Map.Entry)iterator.next();
            XSSFCell cell = row.createCell(index);// 创建单元格
            cell.setCellStyle(style);// 应用样式对象
            cell.setCellValue(HorizonObjTools.obj2String(entry.getKey()));
            index++;
        }
        /**
         * 从第二行开始打印数据
         */
        for(int i=0;i<dataMapList.size();i++){
            index = 0;
            row = sheet.createRow(i+1);// 创建一个行对象
            Map<String,Object> dataDataMap = dataMapList.get(i);
            iterator = dataDataMap.entrySet().iterator();
            while (iterator.hasNext()) {
                row.setHeightInPoints(23);// 设置行高23像素
                Entry entry = (java.util.Map.Entry)iterator.next();
                XSSFCell cell = row.createCell(index);// 创建单元格
                cell.setCellStyle(style);// 应用样式对象
                /**
                 * 如果值为空则打印空字符串
                 */
                String value = HorizonObjTools.obj2String(entry.getValue());
                value = StringUtils.isBlank(value) ? "" : value;
                cell.setCellValue(value);
                index++;
            }
        }
        /**
         * 创建本地临时文件夹
         */
        File tempFile = new File(contextPath);
        if(!tempFile.exists()){
            tempFile.mkdir();
        }
        /**
         * 输出本地临时文件
         */
        String realFileName = System.currentTimeMillis()+".xlsx";
        FileOutputStream os = new FileOutputStream((contextPath + realFileName).toString());
        workBook.write(os);// 将文档对象写入文件输出流
        downLoadFile(realFileName,contextPath,request,response);
    }
   
   /**
    * @notes:创建2003版本的Excel文件
    * @throws FileNotFoundException
    * @throws IOException: void:
    * @author: henry
    *
    * 2016年1月6日		上午10:05:35
    */
    @SuppressWarnings("rawtypes")
    public static void creat2003Excel(List<Map<String,Object>> dataMapList,String contextPath,HttpServletRequest request, HttpServletResponse response)throws FileNotFoundException,   IOException {
        HSSFWorkbook workBook = new HSSFWorkbook();// 创建 一个excel文档对象
        HSSFSheet sheet = workBook.createSheet();// 创建一个工作薄对象
        sheet.setColumnWidth(1, 10000);// 设置第二列的宽度为
        HSSFCellStyle style = workBook.createCellStyle();// 创建样式对象
        HSSFFont font = workBook.createFont();// 创建字体对象
        font.setFontHeightInPoints((short) 15);// 设置字体大小
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体
        font.setFontName("黑体");// 设置为黑体字
        style.setFont(font);// 将字体加入到样式对象
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 水平居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);// 顶部边框粗线
        style.setTopBorderColor(HSSFColor.RED.index);// 设置为红色
        style.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);// 底部边框双线
        style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
        style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        /**
         * 创建单元格
         */
        //获取集合中的第一个map的key用作标题行
        Map<String,Object> dataTitleMap = dataMapList.get(0);
        HSSFRow row = sheet.createRow(0);// 创建一个行对象
        row.setHeightInPoints(23);// 设置行高23像素
        Iterator<Entry<String, Object>> iterator = dataTitleMap.entrySet().iterator();
        //从第一个单元格开始赋值
        int index = 0;
        while (iterator.hasNext()) {
            Entry entry = (java.util.Map.Entry)iterator.next();
            HSSFCell cell = row.createCell(index);// 创建单元格
            cell.setCellStyle(style);// 应用样式对象
            cell.setCellValue(HorizonObjTools.obj2String(entry.getKey()));
            index++;
        }
        /**
         * 从第二行开始打印数据
         */
        for(int i=0;i<dataMapList.size();i++){
            index = 0;
            row = sheet.createRow(i+1);// 创建一个行对象
            Map<String,Object> dataDataMap = dataMapList.get(i);
            iterator = dataDataMap.entrySet().iterator();
            while (iterator.hasNext()) {
                row.setHeightInPoints(23);// 设置行高23像素
                Entry entry = (java.util.Map.Entry)iterator.next();
                HSSFCell cell = row.createCell(index);// 创建单元格
                cell.setCellStyle(style);// 应用样式对象
                /**
                 * 如果值为空则打印空字符串
                 */
                String value = HorizonObjTools.obj2String(entry.getValue());
                value = StringUtils.isBlank(value) ? "" : value;
                cell.setCellValue(value);
                index++;
            }
        }
        /**
         * 创建本地临时文件夹
         */
        File tempFile = new File(contextPath);
        if(!tempFile.exists()){
            tempFile.mkdir();
        }
        /**
         * 输出本地临时文件
         */
        String realFileName = System.currentTimeMillis()+".xls";
        FileOutputStream os = new FileOutputStream((contextPath + realFileName).toString());
        workBook.write(os);// 将文档对象写入文件输出流
        downLoadFile(realFileName,contextPath,request,response);
    }
    
    /**
     * @notes:下载文件到客户端
     * @param realFileName
     * @param contextPath
     * @param request
     * @param response
     * @return: boolean:
     * @author: henry
     *
     * 2016年1月11日       下午2:05:45
     */
    public static boolean downLoadFile(String realFileName,String contextPath,HttpServletRequest request, HttpServletResponse response){
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
            return false;
        }
    }
}
