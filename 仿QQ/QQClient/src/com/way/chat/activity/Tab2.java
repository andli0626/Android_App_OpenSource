package com.way.chat.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.way.Constants;
import com.way.chat.activity.MyListView.OnRefreshListener;
import com.way.chat.common.bean.ChatMsgEntity;
import com.way.chat.common.bean.TextMessage;
import com.way.chat.common.bean.User;
import com.way.chat.common.tran.bean.TranObject;
import com.way.util.MessageDB;
import com.way.util.MyDate;
import com.way.util.SharePreferenceUtil;

public class Tab2 extends SuperView {
	private static final String GROUPNAME = "groupName";// 大组成员Map的key
	private static final String NAME = "name";// 小组成员Map的name
	private static final String ID = "id";// 小组成员Map的qq号
	private static final String IMG = "img";// 小组成员的头像
	private ExAdapter adapter;
	private List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();// 大组成员
	List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();// 小组成员
	private String[] groupName = { "我的好友", "我的同学", "我的家人" };// 大组成员名
	private int[] imgs = { R.drawable.icon, R.drawable.f1, R.drawable.f2,
			R.drawable.f3, R.drawable.f4, R.drawable.f5, R.drawable.f6,
			R.drawable.f7, R.drawable.f8, R.drawable.f9 };
	private List<User> data;
	private SharePreferenceUtil util;
	private MessageDB messageDB;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.tab2);
		messageDB = new MessageDB(this);
		util = new SharePreferenceUtil(this, Constants.SAVE_USER);
		TranObject msg = (TranObject) getIntent().getSerializableExtra(
				Constants.MSGKEY);
		data = (List<User>) msg.getObject();
		// 为大小组添加数据
		for (int i = 0; i < groupName.length; i++) {
			Map<String, String> curGroupMap = new HashMap<String, String>();
			groupData.add(curGroupMap);
			curGroupMap.put(GROUPNAME, groupName[i]);
			List<Map<String, String>> children = new ArrayList<Map<String, String>>();
			for (User u : data) {
				Map<String, String> curChildMap = new HashMap<String, String>();
				children.add(curChildMap);
				curChildMap.put(NAME, u.getName());
				curChildMap.put(ID, u.getId() + "");
				curChildMap.put(IMG, u.getImg() + "");
			}
			childData.add(children);
		}
		final MyListView listView = (MyListView) findViewById(R.id.tab2_listView);
		adapter = new ExAdapter(this);
		listView.setAdapter(adapter);
		listView.setGroupIndicator(null);// 不设置大组指示器图标，因为我们自定义设置了
		listView.setDivider(null);// 设置图片可拉伸的

		listView.setFocusable(true);// 聚焦才可以下拉刷新
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						// 可以做一从网上获取数据的事情
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						// data.addFirst("刷新后的内容" + count);
						// count++;
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						adapter.notifyDataSetChanged();
						listView.onRefreshComplete();
						Toast.makeText(Tab2.this, "刷新成功", 0).show();
					}

				}.execute(null);
			}
		});
	}

	// 关键代码是这个可扩展的listView适配器
	class ExAdapter extends BaseExpandableListAdapter {
		Context context;

		public ExAdapter(Context context) {
			super();
			this.context = context;
		}

		// 得到大组成员的view
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.member_listview, null);
			}

			TextView title = (TextView) view.findViewById(R.id.content_001);
			title.setText(getGroup(groupPosition).toString());// 设置大组成员名称

			ImageView image = (ImageView) view.findViewById(R.id.tubiao);// 是否展开大组的箭头图标
			if (isExpanded)// 大组展开时
				image.setBackgroundResource(R.drawable.group_unfold_arrow);
			else
				// 大组合并时
				image.setBackgroundResource(R.drawable.group_fold_arrow);

			return view;
		}

		// 得到大组成员的id
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		// 得到大组成员名称
		public Object getGroup(int groupPosition) {
			return groupData.get(groupPosition).get(GROUPNAME).toString();
		}

		// 得到大组成员总数
		public int getGroupCount() {
			return groupData.size();

		}

		// 得到小组成员的view
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				view = inflater.inflate(R.layout.item, null);
			}
			final TextView title = (TextView) view.findViewById(R.id.name_item);
			final TextView title2 = (TextView) view.findViewById(R.id.id_item);
			ImageView icon = (ImageView) view.findViewById(R.id.imageView_item);
			final String name = childData.get(groupPosition).get(childPosition)
					.get(NAME).toString();
			final String id = childData.get(groupPosition).get(childPosition)
					.get(ID).toString();
			final String img = childData.get(groupPosition).get(childPosition)
					.get(IMG).toString();
			title.setText(name);// 大标题
			title2.setText(id);// 小标题
			icon.setImageResource(imgs[Integer.parseInt(img)]);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					User u = new User();
					u.setName(name);
					u.setId(Integer.parseInt(id));
					u.setImg(Integer.parseInt(img));
					Intent intent = new Intent(Tab2.this, ChatActivity.class);
					intent.putExtra("user", u);
					startActivity(intent);
					// Toast.makeText(Tab2.this, "开始聊天", 0).show();
				}
			});
			return view;
		}

		// 得到小组成员id
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		// 得到小组成员的名称
		public Object getChild(int groupPosition, int childPosition) {
			return childData.get(groupPosition).get(childPosition);
		}

		// 得到小组成员的数量
		public int getChildrenCount(int groupPosition) {
			return childData.get(groupPosition).size();
		}

		/**
		 * Indicates whether the child and group IDs are stable across changes
		 * to the underlying data. 表明大組和小组id是否稳定的更改底层数据。
		 * 
		 * @return whether or not the same ID always refers to the same object
		 * @see Adapter#hasStableIds()
		 */
		public boolean hasStableIds() {
			return true;
		}

		// 得到小组成员是否被选择
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

	// 根据广播接收消息
	@Override
	public void getMessage(TranObject msg) {
		// TODO Auto-generated method stub
		switch (msg.getType()) {
		case MESSAGE:
			TextMessage tm = (TextMessage) msg.getObject();
			String message = tm.getMessage();
			ChatMsgEntity entity = new ChatMsgEntity("", MyDate.getDateEN(),
					message, -1, true);// 收到的消息
			messageDB.saveMsg(msg.getFromUser(), entity);
			Toast.makeText(Tab2.this,
					"您有新的消息来自：" + msg.getFromUser() + ":" + message, 0).show();// 提示，再保存到数据库
			break;
		case LOGIN:
			User loginUser = (User) msg.getObject();
			Toast.makeText(Tab2.this, loginUser.getId() + "上线了", 0).show();
			break;
		case LOGOUT:
			User logoutUser = (User) msg.getObject();
			Toast.makeText(Tab2.this, logoutUser.getId() + "下线了", 0).show();
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {// 捕获返回按键事件
		// TODO Auto-generated method stub
		// 发送广播，通知服务，已进入后台运行
		Intent i = new Intent();
		i.setAction(Constants.BACKKEY_ACTION);
		sendBroadcast(i);

		util.setIsStart(true);// 设置后台运行标志，正在运行
		finish();// 再结束自己
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		messageDB.close();
	}
}
