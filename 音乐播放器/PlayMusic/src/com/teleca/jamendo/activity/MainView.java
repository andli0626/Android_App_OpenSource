package com.teleca.jamendo.activity;

import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;

import com.lilin.util.SDCardHelp;
import com.teleca.jamendo.MyApplication;
import com.teleca.jamendo.R;
import com.teleca.jamendo.activity.album.StarredAlbumsView;
import com.teleca.jamendo.activity.download.DownloadView;
import com.teleca.jamendo.activity.playlist.PlaylistView;
import com.teleca.jamendo.activity.playlist.PlaylistView.Mode;
import com.teleca.jamendo.activity.playview.PlayMethod;
import com.teleca.jamendo.activity.playview.PlayView;
import com.teleca.jamendo.activity.search.SearchView;
import com.teleca.jamendo.activity.setting.SetView;
import com.teleca.jamendo.adapter.ImgAdp;
import com.teleca.jamendo.adapter.ItemListViewAdp;
import com.teleca.jamendo.adapter.SplitListViewAdp;
import com.teleca.jamendo.api.IClickAlbumListener;
import com.teleca.jamendo.api.IListViewItemClickListener;
import com.teleca.jamendo.api.IServerApi;
import com.teleca.jamendo.api_impl.ServerApiImpl;
import com.teleca.jamendo.model.Album;
import com.teleca.jamendo.model.ErrorMsg;
import com.teleca.jamendo.model.MainItem;
import com.teleca.jamendo.ui.dialog.AboutDialog;
import com.teleca.jamendo.ui.loadDialog.HotSongLoadDialog;
import com.teleca.jamendo.widget.FailureBar;
import com.teleca.jamendo.widget.ProgressBar;

/**
 * 主界面
 * 
 * @author lilin
 * @date 2011-12-12 下午11:13:20
 * @ClassName: MainView
 */
public class MainView extends Activity implements IClickAlbumListener {// 继承点击相册的接口
	// 控制界面的轮流显示
	private ViewFlipper viewFlipper;
	// 相册
	private Gallery gallery;
	private ProgressBar progressBar;
	private FailureBar failureBar;

	// 主界面的ListView：分为2部分
	private ListView mainListView;
	private ItemListViewAdp item1Adp;
	private ItemListViewAdp item2Adp;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainview);
		// 实例化控件
		initUI();
		// 初始化Gallery相册
		new AsyncLoadImg().execute((Void) null);
		// 相册的点击事件
		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				Album album = (Album) adapterView.getItemAtPosition(position);
				// 跳转到播放界面
				PlayView.launch(MainView.this, album);
			}
		});
	}

	private void initUI() {
		mainListView = (ListView) findViewById(R.id.HomeListView);
		gallery = (Gallery) findViewById(R.id.Gallery);
		progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
		failureBar = (FailureBar) findViewById(R.id.FailureBar);
		viewFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper);
	}

	@Override
	// 实现点击相册接口里的方法
	public void onAlbumClicked(Album album) {
		PlayView.launch(this, album);
	}

	// 用可点击的项填充主界面上的ListView
	private void fillMainListView() {
		item1Adp = new ItemListViewAdp(this);
		item2Adp = new ItemListViewAdp(this);
		ArrayList<MainItem> item1List = new ArrayList<MainItem>();
		ArrayList<MainItem> item2List = new ArrayList<MainItem>();

		// 热门歌曲项
		item1List.add(new MainItem(R.drawable.list_top, "热门歌曲",
				new IListViewItemClickListener() {
					public void itemClick() {
						// 异步加载热门歌曲
						new HotSongLoadDialog(MainView.this, "正在努力加载热门歌曲...",
								"加载失败!").execute();
					}
				}));
		// 歌曲列表项
		item1List.add(new MainItem(R.drawable.list_playlist, "歌曲列表",
				new IListViewItemClickListener() {
					public void itemClick() {
						PlaylistView.launch(MainView.this, Mode.Normal);
					}
				}));

		// 搜索项
		item2List.add(new MainItem(R.drawable.list_search, "搜索",
				new IListViewItemClickListener() {
					public void itemClick() {
						SearchView.launch(MainView.this);
					}
				}));

		// 获得配置参数：用户名
		final String userName = PreferenceManager.getDefaultSharedPreferences(
				this).getString("user_name", null);
		// 如果用户名不空，添加相册项
		if (userName != null && userName.length() > 0) {
			item2List.add(new MainItem(R.drawable.list_cd, "相册",
					new IListViewItemClickListener() {
						public void itemClick() {
							StarredAlbumsView.launch(MainView.this, userName);
						}
					}));
		}

		// 如果内存卡存在，添加相册项
		if (SDCardHelp.hasSDcard()) {
			item2List.add(new MainItem(R.drawable.list_download, "下载",
					new IListViewItemClickListener() {
						public void itemClick() {
							DownloadView.launch(MainView.this);
						}
					}));
		}
		// 电台项
		// item1List.add(new MainItemModel(R.drawable.list_radio, "电台",
		// new IListViewItemClickListener() {
		// public void itemClick() {
		// RadioView.launch(MainView.this);
		// }
		// }));
		item1Adp.setList(item1List);
		item2Adp.setList(item2List);

		// MainListView的适配器
		SplitListViewAdp splitAdp = new SplitListViewAdp(this);
		splitAdp.addSection("我的歌曲", item1Adp);
		splitAdp.addSection("常用工具", item2Adp);

		mainListView.setAdapter(splitAdp);
		mainListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view,
					int index, long time) {
				IListViewItemClickListener listener = ((MainItem) adapterView
						.getAdapter().getItem(index)).getListener();
				if (listener != null) {
					listener.itemClick();
				}
			}
		});
	}

	// 异步加载图片到gallery
	private class AsyncLoadImg extends AsyncTask<Void, ErrorMsg, Album[]> {
		public void onPreExecute() {
			viewFlipper.setDisplayedChild(0);
			progressBar.setText("正在努力加载...");
			super.onPreExecute();
		}

		public Album[] doInBackground(Void... params) {
			IServerApi server = new ServerApiImpl();
			Album[] albums = null;
			try {
				albums = server.getPopularAlbumsWeek();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ErrorMsg e) {
				publishProgress(e);
			}
			return albums;
		}

		public void onPostExecute(Album[] albums) {

			if (albums != null && albums.length > 0) {
				viewFlipper.setDisplayedChild(1);
				ImgAdp albumsAdapter = new ImgAdp(MainView.this);
				albumsAdapter.setList(albums);
				gallery.setAdapter(albumsAdapter);
				// animate to center
				gallery.setSelection(albums.length / 2, true);
			} else {
				viewFlipper.setDisplayedChild(2);
				failureBar.setOnRetryListener(new OnClickListener() {
					public void onClick(View v) {
						new AsyncLoadImg().execute((Void) null);
					}

				});
				failureBar.setText("服务器连接失败！");
			}
			super.onPostExecute(albums);
		}

		protected void onProgressUpdate(ErrorMsg... values) {
			Toast.makeText(MainView.this, values[0].getMessage(), 5000).show();
			super.onProgressUpdate(values);
		}

	}

	// 菜单-----------------------------------------------------------

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		if (MyApplication.getInstance().getPlayerEngineInterface() == null
				|| MyApplication.getInstance().getPlayerEngineInterface()
						.getPlaylist() == null) {
			menu.findItem(R.id.player_menu_item).setVisible(false);
		} else {
			menu.findItem(R.id.player_menu_item).setVisible(true);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.player_menu_item:
			// 播放界面
		{
			PlayView.launch(this, (PlayMethod) null);
		}
			break;
		case R.id.about_menu_item:
			// 关于
		{
			new AboutDialog(this).show();
		}
			break;

		case R.id.settings_menu_item:
			// 设置
		{
			SetView.launch(this);
		}
			break;
		default:
		}
		return super.onOptionsItemSelected(item);
	}

	protected void onResume() {
		fillMainListView();
		super.onResume();
	}
}