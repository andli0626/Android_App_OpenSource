package cn.com.karl.reader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import cn.com.karl.pageturn.Turntest;
import cn.com.karl.sqlite.BookDAO;
import cn.com.karl.sqlite.CollectDAO;
import cn.com.karl.tabhost.TabHostActivity;

public class QueryList extends Activity {
	List<String> list;
	Button back;
	ListView listView;
	File file;
	/* 设置长按菜单常量 */
	private static final int look = Menu.FIRST;// 查看
	private static final int add_local = Menu.FIRST + 1;// 添加到本地书架
	private static final int add_collect = Menu.FIRST + 2;// 添加到我的收藏
	private static final int delete = Menu.FIRST + 3;// 删除
	static int temp;
	static String filePath;
	ArrayAdapter<String> adapter;
	static String bookName;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.query_book_list);
		back = (Button) findViewById(R.id.query_back_bt);
		listView = (ListView) findViewById(R.id.query_listView);
		back.setOnClickListener(onClickListener);

		list = FileToStr(BookShelfActivity.txtList);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);
		super.registerForContextMenu(this.listView);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				temp = position;
				filePath = BookShelfActivity.txtList.get(position).getPath();
				bookName = BookShelfActivity.txtList.get(position).getName();
				Log.d("###########", "!!!!!!!!!!" + bookName);
				return false;
			}
		});
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(QueryList.this, TabHostActivity.class);
			startActivity(intent);
		}

	};

	/**
	 * 把文件列表转换成字符串
	 * 
	 * @param
	 * @return
	 */
	public List<String> FileToStr(List<File> f) {
		ArrayList<String> listStr = new ArrayList<String>();
		for (int i = 0; i < f.size(); i++) {
			String nameString = f.get(i).getName();
			Log.d("#####", "$$$$$$$" + nameString);
			listStr.add(nameString);
		}
		return listStr;
	}

	/* 长按列表上下文菜单 */

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("操作");
		menu.add(0, look, 0, "翻页阅读");
		menu.add(1, add_local, 1, "添加到本地书架");
		menu.add(2, add_collect, 2, "添加到收藏列表");
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
			Intent intent = new Intent(QueryList.this, Turntest.class);
			intent.putExtra("filePath", filePath);
			startActivity(intent);
			break;
		case add_local:
			addLocal();
			break;
		case add_collect:
			addCollect();
			break;
		case delete:
			deleteDialog(filePath);
			break;

		}
		return true;
	}

	public boolean delete(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			Log.d("#########", "$$$$$$" + filePath + "^^^^^^^^^^^" + temp);

			file.delete();
			// adapter.notifyDataSetChanged();
			handler.sendEmptyMessage(0);

			return true;
		} else {
			return false;
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			list.remove(temp);
			Log.d("#######", "##########刷新adapter");
			adapter.notifyDataSetChanged();

		};
	};

	// 删除图书的对话框
	private void deleteDialog(final String filePath) {

		new AlertDialog.Builder(QueryList.this).setIcon(R.drawable.ic_launcher)
				.setTitle("系统提示：").setMessage("是否从本地删除该书籍？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						boolean boo = delete(filePath);
						if (boo == true) {
							Toast.makeText(getApplicationContext(), "删除成功!",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(),
									"删除失败或文件不存在", Toast.LENGTH_SHORT).show();
						}
					}

				}).setNeutralButton("取消", null).create().show();
	}

	// 添加到本地书架
	public void addLocal() {
		BookDAO bookdao = new BookDAO(QueryList.this);
		Log.d("!!!!!!!!!!!!", "$$$$要保存的信息" + bookName + "@@" + filePath);
		long l = bookdao.insert(bookName, filePath, 0);
		Log.d("@@@@@@@", "-------添加的返回值-----" + l);
		if (l == 0) {
			Toast.makeText(this, "书架中已经添加了本书！", Toast.LENGTH_SHORT).show();
		} else {

			Toast.makeText(this, "图书添加成功！", Toast.LENGTH_SHORT).show();
		}
	}

	// 添加到收藏列表
	public void addCollect() {
		CollectDAO collect = new CollectDAO(QueryList.this);
		long l = collect.insert(bookName, filePath);
		if (l == 0) {
			Toast.makeText(this, "收藏列表中已经添加了本书！", Toast.LENGTH_SHORT).show();
		} else {

			Toast.makeText(this, "图书收藏成功！", Toast.LENGTH_SHORT).show();
		}
	}
}
