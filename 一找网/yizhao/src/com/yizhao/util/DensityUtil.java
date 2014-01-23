package com.yizhao.util;

import com.yizhao.core.Const;

import android.util.DisplayMetrics;  
import android.util.Log;
  
/** 
 * 计算公式 pixels = dips * (density / 160)
 */  
public class DensityUtil {  
	
    // 当前屏幕的densityDpi  
    private static float dmDensityDpi = 0.0f;  
    private static DisplayMetrics dm;  
    private static float scale = 0.0f;  
  
    /** 
     *  
     * 根据构造函数获得当前手机的屏幕系数 
     *  
     * */  
    public DensityUtil(DisplayMetrics _dm) {  
        // 获取当前屏幕  
        dm = _dm;
        // 设置DensityDpi  
        dmDensityDpi = dm.densityDpi;  
        // 密度因子  
        scale = dmDensityDpi / 160;
    }
  
    /** 
     * 密度转换像素 
     * */  
    public int dip2px(float dipValue) {
    	int px = (int) (dipValue * scale + 0.5f);
        Log.d(Const.TAG, "dip "+dipValue+" convert px = " + px);  
        return px;
    }  
  
    /** 
     * 像素转换密度 
     * */  
    public int px2dip(float pxValue) {  
        return (int) (pxValue / scale + 0.5f);  
    }  
  
}  
