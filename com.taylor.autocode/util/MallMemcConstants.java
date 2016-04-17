package com.taylor.api.common.util;

import java.io.Serializable;

/**
 * @notes:平台组memcached key 管理类
 *
 * @author taylor
 *
 * 2015-12-10	下午7:29:50
 */
public class MallMemcConstants implements Serializable {

	private static final long serialVersionUID = -6744073366625047580L;
	
	/****** 平台memcached key 前缀 *******/
	public static final String PREFIX = "MALL_";
	
	/****** 文书商城memcached key 前缀 *******/
	public static final String CHANNEL_DOC = PREFIX + "DOC_";
	
	/****** 留学生公寓memcached key 前缀 *******/
	public static final String CHANNEL_APT = PREFIX + "APT_";
	
	/****** 留学保险memcached key 前缀 *******/
	public static final String CHANNEL_INS = PREFIX + "INS_";
	
	/****** 语言培训memcached key 前缀 *******/
	public static final String CHANNEL_COU = PREFIX + "COU_";
	
	/****** 游学memcached key 前缀 *******/
    public static final String CHANNEL_ST = PREFIX + "ST_";
	
	
	/*******
	 * ######################### 语培Key开始 ###########################
	 */
	/****** 语培用户身份验证标识，存于memcached *******/
	public static final String COUR_USER_VERIFY = CHANNEL_COU + "USER_VERIFY_";
	
	/*******
	 * ######################### 语培Key结束 ###########################
	 */
	
	/*******
     * ######################### 游学Key开始 ###########################
     */
    /****** 语培用户身份验证标识，存于memcached *******/
    public static final String ST_USER_VERIFY = CHANNEL_ST + "USER_VERIFY_";
    
    /*******
     * ######################### 语培Key结束 ###########################
     */

}