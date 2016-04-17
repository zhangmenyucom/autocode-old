package com.taylor.api.common.util;

import java.util.Date;

import com.taylor.api.common.util.DateTools;
import com.taylor.api.common.util.HorizonObjTools;
import com.taylor.api.common.util.RandomUtil;

/**
 * @notes: 编号生成类
 * @author: kuta.li
 * @date: 2015-7-7-上午8:57:58
 */
public class CodeUtil {
    
    private CodeUtil(){
        
    }
    
    /**
     * @notes: 编号生成方法
     * @author: kuta.li
     * @date: 2015-7-7-上午8:58:10
     */
    public static String generate(String prefix){
        if(HorizonObjTools.isNull(prefix)){
            prefix = "";
        }
        String date = DateTools.formatDateTime(new Date(), "yyMMdd");
        String time = DateTools.formatDateTime(new Date(), "mmss");
        //规则：前缀+日期+5位随机数+时刻
        String code = prefix + date + RandomUtil.getNumberByLength(5) + time;
        return code;
    }
    
}

