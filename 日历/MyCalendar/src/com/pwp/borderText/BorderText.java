package com.pwp.borderText;

import android.content.Context;
import android.graphics.Canvas;

import android.graphics.Paint;

import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 
 * @author lilin
 * @date 2013-2-6 上午9:37:11
 * @annotation 实现带边框的TextView
 */
public class BorderText extends TextView {

	public BorderText(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint paint = new Paint();// 实例化一支画笔
		paint.setColor(android.graphics.Color.RED);// 设置画笔的颜色，代表边框的颜色
		// 绘制上边框
		// canvas.drawLine(0, 0, this.getWidth() - 1, 0, paint);
		// 绘制左边框
		// canvas.drawLine(0, 0, 0, this.getHeight() - 1, paint);
		// 绘制右边框
		// canvas.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1,
		// this.getHeight() - 1, paint);
		// 绘制下边框
		canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1,
				this.getHeight() - 1, paint);

	}

}
