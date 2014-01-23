package com.douban.android;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.douban.android.util.PreferencesUtil;
import com.google.gdata.client.douban.DoubanService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.TextContent;
import com.google.gdata.data.douban.MiniblogEntry;
import com.google.gdata.data.douban.MiniblogFeed;
import com.google.gdata.util.ServiceException;

/**
 * @author haiyang 显示我说的界面
 */
public class ActivityShowSaying extends AbstractActivity {
	ListView listView;
	OnClickListener listener = null;
	EditText editText = null;
	Button button = null;
	SayingListAdapter sayingListAdapter = null;

	private class Saying {
		public String id;
		public String nickname;
		public String content;
		public String time;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 显示列表
		setContentView(R.layout.show_saying);
		listView = (ListView) findViewById(R.id.saying_list);
		sayingListAdapter = new SayingListAdapter(this,
				getSayingArraryFromInternet());
		listView.setAdapter(sayingListAdapter);
		editText = (EditText) findViewById(R.id.content);
		listener = new OnClickListener() {
			public void onClick(View v) {
				setTitle("sending");
				DoubanService myService = getAuthDoubanService();
				try {
					sayingListAdapter.addSaying(myService
							.createSaying(new PlainTextConstruct((editText
									.getText()).toString())));
				} catch (IOException e) {
					setTitle("sending error");
					e.printStackTrace();
				} catch (ServiceException e) {
					setTitle("sending error");
					e.printStackTrace();
				}
			}
		};
		button = (Button) findViewById(R.id.send);
		button.setOnClickListener(listener);
	}

	/*
	 * 得到我说的内容
	 */
	private ArrayList<Saying> getSayingArraryFromInternet() {
		ArrayList<Saying> sayingList = new ArrayList<Saying>();
		SharedPreferences settings = getSharedPreferences(
				PreferencesUtil.preferencesDouban, 0);

		String userId = settings.getString(PreferencesUtil.userName,
				"haiyangjy");
		MiniblogFeed mf;
		try {
			DoubanService myService = getAuthDoubanService();
			mf = myService.getContactsMiniblogs(userId, 1, 20);
			for (MiniblogEntry me : mf.getEntries()) {
				Saying saying = new Saying();
				String nickname = me.getAuthors().get(0).getName();
				String content = ((TextContent) me.getContent()).getContent()
						.getPlainText();
				saying.content = content;
				saying.nickname = nickname;
				sayingList.add(saying);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return sayingList;
	}

	public class SayingListAdapter extends BaseAdapter {
		private Context mContext;
		private ArrayList<Saying> sayingList;

		public SayingListAdapter(Context context, ArrayList<Saying> sayingList) {
			mContext = context;
			SayingListAdapter.this.sayingList = sayingList;
		}

		@Override
		public int getCount() {

			return sayingList.size();
		}

		@Override
		public Object getItem(int position) {

			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SayingView sayingView;
			Saying saying = sayingList.get(position);
			if (convertView == null) {
				sayingView = new SayingView(mContext, saying);
			} else {
				sayingView = (SayingView) convertView;
				sayingView.setNickname(saying.nickname);
				sayingView.setContent(saying.content);
			}

			return sayingView;
		}

		/**
		 * 实时显示增加的我说
		 * 
		 * @param miniblogEntry
		 */
		public void addSaying(MiniblogEntry miniblogEntry) {
			Saying newSaying = new Saying();
			newSaying.content = ((TextContent) miniblogEntry.getContent())
					.getContent().getPlainText();
			newSaying.nickname = miniblogEntry.getAuthors().get(0).getName();
			sayingList.add(0, newSaying);
			notifyDataSetChanged();
		}

	}

	/**
	 * 我说列表的每一个条目
	 * 
	 * @author Administrator
	 * 
	 */
	public class SayingView extends LinearLayout {
		private TextView tNickname;
		private TextView tContent;
		private ImageView imageView;
		private Saying saying;
		private Context mContext;

		public SayingView(Context context, Saying saying) {
			super(context);
			mContext = context;
			SayingView.this.saying = saying;
			imageView = getImageViewFromLocal();

			addView(imageView);
			tNickname = new TextView(context);
			tNickname.setText(saying.nickname+": ");
			addView(tNickname, new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, 48));

			tContent = new TextView(context);
			tContent.setText(saying.content);
			addView(tContent, new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, 48));
		}

		/**
		 * 重新设置昵称
		 * 
		 * @param nickname
		 */
		public void setNickname(String nickname) {
			tNickname.setText(nickname+": ");
		}

		/**
		 * 重新设置我说的内容
		 * 
		 * @param content
		 */
		public void setContent(String content) {
			tContent.setText(content);
		}

		/*
		 * 得到本地的默认图片
		 */
		private ImageView getImageViewFromLocal() {
			ImageView imageView = new ImageView(mContext);
			imageView.setImageResource(R.drawable.default_head);
			imageView.setAdjustViewBounds(true);
			imageView.setLayoutParams(new AbsListView.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			return imageView;
		}

		
	}
}