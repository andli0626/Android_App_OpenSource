package com.yizhao.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import com.yizhao.core.Const;

public class DataFormalUtil {
	
	/**
	 * 通过时间戳格式化时间
	 * @param tims
	 * @return
	 */
	public static String convertTime(long tims){
		String res_time = "[";
		if(tims > 0){
			SimpleDateFormat sdf = new SimpleDateFormat(Const.TIME_FORMAT);
			res_time+=sdf.format(tims);
		}
		res_time += "]";
		return res_time;
	}
	
	/**
	 * 格式化价格，精确到分
	 * @param price
	 * @return
	 */
	public static String convertPrice(int price){
		
		String res;
		
		BigDecimal bd1 = new BigDecimal(price / 100);
		BigDecimal bd2 = bd1.setScale(2, BigDecimal.ROUND_HALF_EVEN);

		int i_price = price / 100;
		double d_price = bd2.doubleValue();
		
		if(i_price < d_price){
			res = String.valueOf(d_price);
		}else{
			res = String.valueOf(i_price);
		}
		
		return res;
	}

}
