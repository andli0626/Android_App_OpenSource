package com.teleca.jamendo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teleca.jamendo.R;

/**
 * 失败滚动条
 * 
 * @author lilin
 * @date 2011-12-26 下午09:55:59
 * @ClassName: FailureBar
 */
public class FailureBar extends LinearLayout {

	protected TextView mTextView;
	protected Button mRetryButton;

	public FailureBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FailureBar(Context context) {
		super(context);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.failure_bar, this);
		mTextView = (TextView) findViewById(R.id.ProgressTextView);
		mRetryButton = (Button) findViewById(R.id.RetryButton);
	}

	public void setText(String resid) {
		mTextView.setText(resid);
	}

	public void setOnRetryListener(OnClickListener l) {
		mRetryButton.setOnClickListener(l);
	}

}
