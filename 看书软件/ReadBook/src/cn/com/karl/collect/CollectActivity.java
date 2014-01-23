package cn.com.karl.collect;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.karl.model.Book;
import cn.com.karl.pageturn.Turntest;
import cn.com.karl.reader.QueryList;
import cn.com.karl.reader.R;
import cn.com.karl.sqlite.CollectDAO;

public class CollectActivity extends Activity {
	public ListView listView;
	public List<Book> list;
	/* 设置长按菜单常量 */
	private static final int look = Menu.FIRST;// 查看
	private static final int add_local = Menu.FIRST + 1;// 添加到本地书架
	private static final int add_collect = Menu.FIRST + 2;// 添加到我的收藏
	private static final int delete = Menu.FIRST + 3;// 删除
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.collect);
		list = new ArrayList<Book>();
		listView = (ListView) findViewById(R.id.collect_list);
		super.registerForContextMenu(this.listView);
		CollectDAO collect = new CollectDAO(CollectActivity.this);
		list = collect.query();
		listView.setAdapter(new ListAdapter(list));
	}
	/* 长按列表上下文菜单 */

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("操作");
		menu.add(0, look, 0, "翻页阅读");
		menu.add(1, add_local, 1, "添加到本地书架");
//		menu.add(2, add_collect, 2, "添加到收藏列表");
		menu.add(3, delete, 3, "删除");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	/**
	 * 点击上下文的选择事件
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case look:
			Intent intent = new Intent(CollectActivity.this, Turntest.class);
//			intent.putExtra("filePath",filePath);
			startActivity(intent);
			break;
		case add_local:
//			addLocal();
			break;
		case add_collect:
//			addCollect();
			break;
		case delete:
//			deleteDialog(filePath);
			break;

		}
		return true;
	}
	private class ListAdapter extends BaseAdapter {
		List<Book> list;
		public ListAdapter(List<Book> list){
			this.list = list;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.collect_item, null);
				TextView tv = (TextView) convertView
						.findViewById(R.id.collect_list_text);
				Log.d("!!!!!!!!", "^^^^^^收藏的信息是" + list.get(position).getName());
				tv.setText(list.get(position).getName());
				
			}
			return convertView;
		}

	}
}
