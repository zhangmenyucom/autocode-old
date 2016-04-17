package com.taylor.api.common.util;

import java.math.BigDecimal;

public class BigDecimalUtil {
	
	private BigDecimalUtil(){
		
	}

	/**  
	 * 对double数据进行取精度.  
	 * <p>  
	 * For example: <br>  
	 * double value = 100.345678; <br>  
	 * double ret = round(value,4,BigDecimal.ROUND_HALF_UP); <br>  
	 * ret为100.3457 <br>  
	 *   
	 * @param value			double数据.  
	 * @param scale			精度位数(保留的小数位数).  
	 * @param roundingMode	精度取值方式.  
	 * @return 精度计算后的数据.  
	 */
	public static double roundNumber(double value, int scale, int roundingMode) {
		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(scale, roundingMode);
		double d = bd.floatValue();
		bd = null;
		return d;
	}

	/**  
	 * 对double数据进行取精度.  
	 * <p>  
	 * For example: <br>  
	 * double value = 100.345678; <br>  
	 * double ret = round(value,4,BigDecimal.ROUND_HALF_UP); <br>  
	 * ret为100.3457 <br>  
	 *   
	 * @param value			double数据.  
	 * @param scale			精度位数(保留的小数位数).  
	 * @param roundingMode	精度取值方式.  
	 * @return 精度计算后的数据.  
	 */
	public static String round(double value, int scale, int roundingMode) {
		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(scale, roundingMode);
		return bd.toString();
	}
	
	/**
	 * @notes:对double数据进行四舍五入取精度，返回字符串类型
	 *
	 * @param value		值
	 * @param scale		长度
	 * @author	taylor
	 * 2014-4-18	下午12:09:07
	 */
	public static String round(double value, int scale) {
		return round(value, scale, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * @notes:对double数据进行四舍五入取精度，返回字double类型
	 *
	 * @param value		值
	 * @param scale		长度
	 * @author	taylor
	 * 2014-4-18	下午12:10:32
	 */
	public static double roundNumber(double value, int scale) {
		return roundNumber(value, scale, BigDecimal.ROUND_HALF_UP);
	}

}
