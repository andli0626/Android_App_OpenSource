package com.teleca.jamendo.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teleca.jamendo.R;
import com.teleca.jamendo.model.MainItem;

/**
 * Simple ListView adapter presenting icon and text, used in HomeActivity
 * 
 * @author Lukasz Wisniewski
 */
public class ItemListViewAdp extends BaseAdp<MainItem> {

	public ItemListViewAdp(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		ViewHolder holder;

		if (row == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			row = inflater.inflate(R.layout.mainlistview_item, null);

			holder = new ViewHolder();
			holder.image = (ImageView) row.findViewById(R.id.PurpleImageView);
			holder.text = (TextView) row.findViewById(R.id.PurpleRowTextView);

			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		if (mList.get(position).getText() != null) {
			holder.text.setText(mList.get(position).getText());
		} else if (mList.get(position).getTextId() != null) {
			holder.text.setText(mList.get(position).getTextId());
		}
		if (mList.get(position).getDrawable() != null) {
			holder.image.setImageResource(mList.get(position).getDrawable());
		} else {
			holder.image.setVisibility(View.GONE);
		}

		return row;
	}

	/**
	 * Class implementing holder pattern, performance boost
	 * 
	 * @author Lukasz Wisniewski
	 */
	static class ViewHolder {
		ImageView image;
		TextView text;
	}

}
