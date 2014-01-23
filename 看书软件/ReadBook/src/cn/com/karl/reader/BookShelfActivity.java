package cn.com.karl.reader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.karl.model.Book;
import cn.com.karl.pageturn.Turntest;
import cn.com.karl.sqlite.BookDAO;
import cn.com.karl.sqlite.CollectDAO;

public class BookShelfActivity extends BaseActivity implements
		OnItemClickListener {
	public TextView tv_name, tv_path;
	private GridView bookShelf;
	static List<File> txtList;// 文件列表
	public List<Book> bookList;
	private GridView menu_grid;
	private SlidingDrawer sd;// 滑动抽屉
	private Button iv;
	private int[] menu_icon = { R.drawable.menu_search, R.drawable.menu_quit };

	private String[] menu_name = { "搜索SD卡", "退出" };

	private List<Map<String, Object>> menu_list;
	private SimpleAdapter simpleAdapter = null;
	public static String filePath;
	public static int temp;
	public static String bookName;
	public BookDAO bookDao;
	public boolean boo;
	/* 设置长按菜单常量 */
	private static final int delete_local = Menu.FIRST;// 从本地删除
	private static final int add_local = Menu.FIRST + 1;// 添加到本地书架
	private static final int add_collect = Menu.FIRST + 2;// 添加到我的收藏
	private static final int delete = Menu.FIRST + 3;// 删除

	ShlefAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		// 去掉标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置横屏
		// this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.main);
		bookDao = new BookDAO(BookShelfActivity.this);
		bookList = bookDao.query();
		txtList = new ArrayList<File>();
		menu_list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		for (int i = 0; i < menu_icon.length; i++) {
			map = new HashMap<String, Object>();
			map.put("menu_icon", menu_icon[i]);
			map.put("menu_name", menu_name[i]);
			menu_list.add(map);
		}
		bookShelf = (GridView) findViewById(R.id.bookShelf);
		if (bookList.size() <= 0) {
			Toast.makeText(getApplicationContext(), "你的书架还没有书籍，快去添加几本吧！",
					Toast.LENGTH_SHORT).show();
			// Intent intent = new Intent();
			new MyTask().execute(0);
		}
		adapter = new ShlefAdapter(bookList);
		bookShelf.setAdapter(adapter);
		bookShelf.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(BookShelfActivity.this,
						Turntest.class);
				intent.putExtra("filePath", bookList.get(position).getPath());
				startActivity(intent);
				Toast.makeText(getApplicationContext(), "" + position,
						Toast.LENGTH_SHORT).show();
			}
		});
		menu_grid = (GridView) findViewById(R.id.allApps);
		simpleAdapter = new SimpleAdapter(this, menu_list,
				R.layout.local_menu_griditems, new String[] { "menu_icon",
						"menu_name" }, new int[] { R.id.menu_icon,
						R.id.menu_name });
		menu_grid.setAdapter(simpleAdapter);
		menu_grid.setOnItemClickListener(this);
		sd = (SlidingDrawer) findViewById(R.id.sliding);
		iv = (Button) findViewById(R.id.imageViewIcon);
		sd.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener()// 开抽屉
		{
			@Override
			public void onDrawerOpened() {
				iv.setText("返回");
				iv.setBackgroundResource(R.drawable.btn_local);// 响应开抽屉事件
																// ，把图片设为向下的
			}
		});
		sd.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {
				iv.setText("菜单");
				iv.setBackgroundResource(R.drawable.btn_local);// 响应关抽屉事件
			}
		});
		super.registerForContextMenu(this.bookShelf);
		bookShelf.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				filePath = bookList.get(position).getPath();
				Log.d("!!!!!", "~~~~~~~~~~~本地书架的路径" + filePath);
				bookName = bookList.get(position).getName();
				temp = position;
				return false;
			}
		});
	}

	class ShlefAdapter extends BaseAdapter {
		private List<Book> booklist;

		public ShlefAdapter(List<Book> booklist) {
			this.booklist = booklist;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return booklist.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return booklist.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View contentView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if (contentView == null) {
				contentView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.item1, null);

				TextView view = (TextView) contentView
						.findViewById(R.id.book_name);

				view.setText(booklist.get(position).getName());
				Log.d("@@@@@@@", "!!!!!书的名称" + booklist.get(position).getName());
				view.setBackgroundResource(R.drawable.cover_txt);
			}
			return contentView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (position == 2) {
			exitDialog();
		} else if (position == 0) {
			new MyTask().execute(0);

		}
	}

	/**
	 * 获取文件列表
	 * 
	 * @param filePath
	 */
	public void GetFiles(File filePath) {

		File[] files = filePath.listFiles();

		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					GetFiles(files[i]);
				} else {
					if (files[i].getName().toLowerCase().endsWith(".txt")) {
						txtList.add(files[i]);
						Log.d("###", "$%%%%" + files[i].getPath());
					}
				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitDialog();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public void exitDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("你确定退出吗？").setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				})
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	// 异步任务类
	class MyTask extends AsyncTask<Integer, Integer, String[]> {
		private ProgressDialog dialog;

		protected void onPreExecute() {
			dialog = ProgressDialog.show(BookShelfActivity.this, "",
					"正在扫描SD卡,请稍候....");
			super.onPreExecute();
		}

		protected String[] doInBackground(Integer... params) {
			if (!android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {

			} else {
				GetFiles(Environment.getExternalStorageDirectory());
			}
			return null;
		}

		protected void onPostExecute(String[] result) {
			dialog.dismiss();
			Toast.makeText(BookShelfActivity.this, "扫描完毕", Toast.LENGTH_LONG)
					.show();
			Log.d("$$$$$$$$",
					"########" + Environment.getExternalStorageDirectory());
			if (Environment.getExternalStorageDirectory().equals("/mnt/sdcard")) {
				Log.d("$$$$$$$$", "########木有执行？？？？");
				Toast.makeText(BookShelfActivity.this, "sd卡中没有图书",
						Toast.LENGTH_LONG).show();
			} else {
				Intent in = new Intent();
				in.setClass(BookShelfActivity.this, QueryList.class);
				BookShelfActivity.this.startActivity(in);
			}

			super.onPostExecute(result);
		}
	}

	/* 长按列表上下文菜单 */

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("操作");
		menu.add(1, add_local, 1, "查看具体信息");
		menu.add(2, add_collect, 2, "添加到收藏列表");
		menu.add(3, delete, 3, "从书架删除");
		menu.add(4, delete_local, 4, "从本地彻底删除");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	/**
	 * 点击上下文的选择事件
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case delete_local:
			boo = true;
			deleteDialog(boo);
			break;
		case add_local:
			LayoutInflater inflater = LayoutInflater
					.from(BookShelfActivity.this);
			View view = inflater.inflate(R.layout.alert_info, null);
			tv_name = (TextView) view.findViewById(R.id.alert_name);
			tv_path = (TextView) view.findViewById(R.id.alert_path);
			tv_name.setText(bookName);
			tv_path.setText(filePath);
			AlertDialog dialog = new AlertDialog.Builder(BookShelfActivity.this)
					.setTitle("图书信息").setView(view)
					.setPositiveButton("确定", null).create();
			dialog.show();
			break;

		case add_collect:
			addCollect();
			break;
		case delete:
			boo = false;
			deleteDialog(boo);
			break;
		}
		return true;
	}

	// 删除书架图书的对话框
	private void deleteDialog(boolean boo) {
		Builder dialog = new AlertDialog.Builder(BookShelfActivity.this);
		if (boo == false) {
			dialog.setIcon(R.drawable.ic_launcher)
					.setTitle("系统提示：")
					.setMessage("是否从书架删除该书籍？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									boolean boo = bookDao.delete(temp);
									if (boo == true) {
										Toast.makeText(getApplicationContext(),
												"删除成功!", Toast.LENGTH_SHORT)
												.show();
									} else {
										Toast.makeText(getApplicationContext(),
												"删除失败或文件不存在",
												Toast.LENGTH_SHORT).show();
									}
								}

							}).setNeutralButton("取消", null).create().show();
		} else {
			dialog.setIcon(R.drawable.ic_launcher)
					.setTitle("系统提示：")
					.setMessage("是否从本地彻底删除该书籍？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									boolean boo = bookDao.delete(temp);
									boo = delete();
									if (boo == true) {
										Toast.makeText(getApplicationContext(),
												"删除成功!", Toast.LENGTH_SHORT)
												.show();
									} else {
										Toast.makeText(getApplicationContext(),
												"删除失败或文件不存在",
												Toast.LENGTH_SHORT).show();
									}
								}

							}).setNeutralButton("取消", null).create().show();
		}

		// new AlertDialog.Builder(BookShelfActivity.this)
		// .setIcon(R.drawable.ic_launcher).setTitle("系统提示：")
		// .setMessage("是否从书架删除该书籍？")
		// .setPositiveButton("确定", new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// boolean boo = bookDao.delete(temp);
		// if (boo == true) {
		// Toast.makeText(getApplicationContext(), "删除成功!",
		// Toast.LENGTH_SHORT).show();
		// } else {
		// Toast.makeText(getApplicationContext(),
		// "删除失败或文件不存在", Toast.LENGTH_SHORT).show();
		// }
		// }
		//
		// }).setNeutralButton("取消", null).create().show();
	}

	// 添加到收藏列表
	public void addCollect() {
		CollectDAO collect = new CollectDAO(BookShelfActivity.this);
		long l = collect.insert(bookName, filePath);
		if (l == 0) {
			Toast.makeText(this, "收藏列表中已经添加了本书！", Toast.LENGTH_SHORT).show();
		} else {

			Toast.makeText(this, "图书收藏成功！", Toast.LENGTH_SHORT).show();
		}
	}

	// 从本地删除
	public boolean delete() {
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
			bookList.remove(temp);
			Log.d("#######", "##########刷新adapter");
			adapter.notifyDataSetChanged();

		};
	};

	// 删除书架图书的对话框
	// private void deleteDialog2() {
	//
	// new AlertDialog.Builder(BookShelfActivity.this)
	// .setIcon(R.drawable.ic_launcher).setTitle("系统提示：")
	// .setMessage("是否从本地删除该书籍？")
	// .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// boolean boo = delete();
	// if (boo == true) {
	// Toast.makeText(getApplicationContext(), "删除成功!",
	// Toast.LENGTH_SHORT).show();
	// } else {
	// Toast.makeText(getApplicationContext(),
	// "删除失败或文件不存在", Toast.LENGTH_SHORT).show();
	// }
	// }
	//
	// }).setNeutralButton("取消", null).create().show();
	// }
}