package com.taylor.api.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
/*
 * MD5 算法
*/
public class MD5Util {
	
	private static final Logger LOG = Logger.getLogger(MD5Util.class);
    
    // 全局数组
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    private MD5Util() {
    	
    }

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuilder buffer = new StringBuilder(0);
        for (int i = 0; i < bByte.length; i++) {
        	buffer.append(byteToArrayString(bByte[i]));
        }
        return buffer.toString();
    }

    public static String getMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
			LOG.error("error", ex);
        }
        return resultString;
    }

	/**
	 * @notes:字符串MD5加密
	 *
	 * @param inStr
	 * @author	taylor
	 * 2014-6-6	下午2:34:53
	 */
	public static String string2MD5(String inStr) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			char[] charArray = inStr.toCharArray();
			byte[] byteArray = new byte[charArray.length];
	
			for (int i = 0; i < charArray.length; i++)
				byteArray[i] = (byte) charArray[i];
			byte[] md5Bytes = md5.digest(byteArray);
			StringBuilder hexValue = new StringBuilder(0);
			for (int i = 0; i < md5Bytes.length; i++) {
				int val = (md5Bytes[i]) & 0xff;
				if (val < 16)
					hexValue.append("0");
				hexValue.append(Integer.toHexString(val));
			}
			return hexValue.toString();
		} catch (Exception e) {
			LOG.error("error", e);
			return "";
		}

	}
	
}
