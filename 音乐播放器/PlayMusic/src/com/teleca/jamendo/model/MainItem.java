

package com.teleca.jamendo.model;

import com.teleca.jamendo.api.IListViewItemClickListener;

public class MainItem {
	private Integer mDrawable;
	private String mText;
	private String mTextId;
	private IListViewItemClickListener mListener;

	public Integer getDrawable() {
		return mDrawable;
	}

	public void setDrawable(Integer drawable) {
		mDrawable = drawable;
	}

	public String getTextId() {
		return mTextId;
	}

	public void setTextId(String textId) {
		mTextId = textId;
	}

	public void setListener(IListViewItemClickListener listener) {
		this.mListener = listener;
	}

	public IListViewItemClickListener getListener() {
		return mListener;
	}

	public void setText(String mText) {
		this.mText = mText;
	}

	public String getText() {
		return mText;
	}

	public MainItem(Integer drawable, String textId) {
		mDrawable = drawable;
		mTextId = textId;
	}

	public MainItem(Integer drawable, String text, IListViewItemClickListener listener) {
		mDrawable = drawable;
		mTextId = text;
		mListener = listener;
	}
}
