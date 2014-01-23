package com.teleca.jamendo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teleca.jamendo.R;

/**
 * Widget notifying user of ongoing action
 * 
 * @author Lukasz Wisniewski
 */
public class ProgressBar extends LinearLayout {

	protected TextView mTextView;

	public ProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ProgressBar(Context context) {
		super(context);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.progress_bar, this);

		mTextView = (TextView) findViewById(R.id.ProgressTextView);
	}

	/**
	 * Sets informative text
	 * 
	 * @param resid
	 */
	public void setText(String resid) {
		mTextView.setText(resid);
	}

}
