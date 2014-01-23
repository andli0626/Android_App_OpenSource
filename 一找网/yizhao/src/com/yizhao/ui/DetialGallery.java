package com.yizhao.ui;

import android.content.Context;
import android.util.AttributeSet;
//import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class DetialGallery extends Gallery {

    public DetialGallery(Context context ,AttributeSet attrSet) {
     super(context,attrSet);
     // TODO Auto-generated constructor stub
    }    
    
//	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2)
//	{   
//		return e2.getX() > e1.getX(); 
//	}
	
	/*当初始化的按下动作事件和松开动作事件匹配时通知fling（译者注：快滑，用户按下触摸屏、快速移动后松开）事件。该动作的速度通过计算X和Y轴上每秒移动多少像素得来。

	　　参数

	　　e1     导致开始fling的按下动作事件。

	　　e2     触发当前onFling方法的移动动作事件

	　　velocityX 测量fling沿X轴上的速度，像素/每秒

	　　velocityY 测量fling沿Y轴上的速度，像素/每秒

	　　返回值

	　　如果该事件被消耗返回true，否则false
	 */
 	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
//		// TODO Auto-generated method stub
//		//  return super.onFling(e1, e2, 0, velocityY);//方法一：只去除翻页惯性
//		//  return false;//方法二：只去除翻页惯性  注：没有被注释掉的代码实现了开始说的2种效果。
//		int kEvent;  
//		if(isScrollingLeft(e1, e2)){
//			//Check if scrolling left     
//			kEvent = KeyEvent.KEYCODE_DPAD_LEFT;  
//		}else{ 
//		//Otherwise scrolling right    
//			kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;   
//		}  
//		onKeyDown(kEvent, null);  
		return false;  
	}
    
    
    
    
}
