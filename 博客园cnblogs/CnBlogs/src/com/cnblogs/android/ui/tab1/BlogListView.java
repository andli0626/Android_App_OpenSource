package com.cnblogs.android.ui.tab1;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cnblogs.android.AuthorBlogActivity;
import com.cnblogs.android.CommentActivity;
import com.cnblogs.android.R;
import com.cnblogs.android.SuperMainView;
import com.cnblogs.android.controls.PullToRefreshListView;
import com.cnblogs.android.controls.PullToRefreshListView.OnRefreshListener;
import com.cnblogs.android.core.BlogHelper;
import com.cnblogs.android.core.Config;
import com.cnblogs.android.dal.BlogDBHelp;
import com.cnblogs.android.entity.Blog;
import com.cnblogs.android.utility.NetHelper;

/**
 * 博客列表
 * 
 * @author walkingp
 * @date:2011-12
 * 
 */
public class BlogListView extends SuperMainView {
	List<Blog> listBlog = new ArrayList<Blog>();

	ProgressBar blogBody_progressBar;// 主题ListView加载框
	ImageButton blog_refresh_btn;// 刷新按钮
	ProgressBar blog_progress_bar;// 加载按钮

	// ListView
	int pageIndex = 1;// 页码
	ListView mListView;
	private BlogListAdp dataAdp;// 数据源
	int loadMoreMode = 0;// 加载更多模式——0:点击底部加载更多，1：滑动到底部加载更多

	// ListView底部控件
	LinearLayout mListFootLayout;// footer view
	// ProgressBar foot_progress;// 底部进度条
	TextView foot_TextView;

	Resources res;// 资源
	private int lastItem;
	BlogDBHelp dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.blog_layout);

		res = this.getResources();

		InitUI();

		new PageTask(0, true).execute();

		// 注册广播
		UpdateListViewReceiver receiver = new UpdateListViewReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.cnblogs.com.update_bloglist");
		registerReceiver(receiver, filter);
	}

	/**
	 * 初始化列表
	 */
	private void InitUI() {

		mListView = (ListView) findViewById(R.id.blog_list);

		blogBody_progressBar = (ProgressBar) findViewById(R.id.blogList_progressBar);
		blogBody_progressBar.setVisibility(View.VISIBLE);

		blog_refresh_btn = (ImageButton) findViewById(R.id.blog_refresh_btn);
		blog_progress_bar = (ProgressBar) findViewById(R.id.blog_progressBar);
		// 底部view
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mListFootLayout = (LinearLayout) mInflater.inflate(
				R.layout.listview_footer, null, false);
		if (loadMoreMode == 0) {
			mListFootLayout.setClickable(true);
			mListFootLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Toast.makeText(BlogListView.this, "点击更多",
					// Toast.LENGTH_LONG)
					// .show();
					pageIndex = pageIndex + 1;
					new PageTask(pageIndex, false).execute();
				}
			});
		}

		dbHelper = new BlogDBHelp(getApplicationContext());

		// 刷新
		blog_refresh_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new PageTask(1, true).execute();
			}
		});
		// 上拉刷新
		((PullToRefreshListView) mListView)
				.setOnRefreshListener(new OnRefreshListener() {
					@Override
					public void onRefresh() {
						new PageTask(-1, true).execute();
					}
				});
		// 下拉刷新
		mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 下拉到最后一行
				if (lastItem == dataAdp.getCount()
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (loadMoreMode == 1) {
						pageIndex = pageIndex + 1;
						new PageTask(pageIndex, false).execute();
					}

				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastItem = firstVisibleItem - 2 + visibleItemCount;
			}
		});
		// 点击跳转
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				RedirectDetailActivity(v);
			}
		});
		// 长按事件
		mListView
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						MenuInflater inflater = getMenuInflater();
						inflater.inflate(R.menu.blog_list_contextmenu, menu);
						menu.setHeaderTitle(R.string.menu_bar_title);
					}
				});

	}

	/**
	 * 多线程启动（用于上拉加载、初始化、下拉加载、刷新）
	 * 
	 */
	public class PageTask extends AsyncTask<String, Integer, List<Blog>> {
		boolean isRefresh = false;
		int curPage = 0;// 当前页数
		boolean isLocalData = false;// 是否是从本地读取的数据

		public PageTask(int page, boolean isRefresh) {
			curPage = page;
			this.isRefresh = isRefresh;
		}

		protected List<Blog> doInBackground(String... params) {
			boolean isNetworkAvailable = NetHelper
					.networkIsAvailable(getApplicationContext());

			int _pageIndex = curPage;

			if (_pageIndex <= 0) {
				_pageIndex = 1;
			}

			// 优先读取本地数据
			List<Blog> listBlogLocal = dbHelper.GetBlogListByPage(_pageIndex,
					Config.BLOG_PAGE_SIZE);

			if (isNetworkAvailable) {// 有网络情况

				List<Blog> listBlogNew = BlogHelper.GetBlogList(_pageIndex);// 联网获取数据列表

				switch (curPage) {
				case -1:// 上拉
					List<Blog> listTmp = new ArrayList<Blog>();
					if (listBlog != null && listBlog.size() > 0) {// 避免首页无数据时
						if (listBlogNew != null && listBlogNew.size() > 0) {
							int size = listBlogNew.size();
							for (int i = 0; i < size; i++) {
								if (!listBlog.contains(listBlogNew.get(i))) {// 避免出现重复
									listTmp.add(listBlogNew.get(i));
								}
							}
						}
					}
					return listTmp;
				case 0:// 首次加载
				case 1:// 刷新
					if (listBlogNew != null && listBlogNew.size() > 0) {
						return listBlogNew;
					}
					break;
				default:// 下拉
					List<Blog> listT = new ArrayList<Blog>();
					if (listBlog != null && listBlog.size() > 0) {// 避免首页无数据时
						if (listBlogNew != null && listBlogNew.size() > 0) {
							int size = listBlogNew.size();
							for (int i = 0; i < size; i++) {
								if (!listBlog.contains(listBlogNew.get(i))) {// 避免出现重复
									listT.add(listBlogNew.get(i));
								}
							}
						}
					}
					return listT;
				}
			} else {// 无网络情况
				isLocalData = true;
				if (curPage == -1) {// 上拉不加载数据
					return null;
				}
				return listBlogLocal;
			}

			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		/**
		 * 加载内容
		 */
		@Override
		protected void onPostExecute(List<Blog> result) {
			// 右上角
			blog_progress_bar.setVisibility(View.GONE);
			blog_refresh_btn.setVisibility(View.VISIBLE);

			// 网络不可用并且本地没有保存数据
			if (result == null || result.size() == 0) {// 没有新数据
				((PullToRefreshListView) mListView).onRefreshComplete();
				if (!NetHelper.networkIsAvailable(getApplicationContext())
						&& curPage > 1) {// 下拉并且没有网络
					Toast.makeText(getApplicationContext(),
							R.string.sys_network_error, Toast.LENGTH_SHORT)
							.show();
					// listView.removeFooterView(viewFooter);
				}
				return;
			}

			int size = result.size();

			if (size >= Config.BLOG_PAGE_SIZE
					&& mListView.getFooterViewsCount() == 0) {
				mListView.addFooterView(mListFootLayout);// 添加底部View
			}

			// 保存到数据库
			if (!isLocalData) {
				dbHelper.SynchronyData2DB(result);
			}

			// 上拉刷新
			if (curPage == -1) {
				dataAdp.InsertData(result);
			}
			// 首次加载
			else if (curPage == 0) {
				listBlog = result;// dbHelper.GetTopBlogList();

				blogBody_progressBar.setVisibility(View.GONE);
				dataAdp = new BlogListAdp(getApplicationContext(), listBlog,
						mListView);
				mListView.setAdapter(dataAdp);

				// 传递参数
				((PullToRefreshListView) mListView).SetDataRow(listBlog.size());
				((PullToRefreshListView) mListView)
						.SetPageSize(Config.BLOG_PAGE_SIZE);
			}
			// 刷新
			else if (curPage == 1) {
				try {// 避免首页无网络加载，按刷新按钮
					listBlog = result;
					if (dataAdp != null && dataAdp.GetData() != null) {
						dataAdp.GetData().clear();
						dataAdp.AddMoreData(listBlog);
					} else if (result != null) {
						dataAdp = new BlogListAdp(getApplicationContext(),
								listBlog, mListView);
						mListView.setAdapter(dataAdp);
					}
					// 传递参数
					((PullToRefreshListView) mListView).SetDataRow(listBlog
							.size());
					((PullToRefreshListView) mListView)
							.SetPageSize(Config.BLOG_PAGE_SIZE);
				} catch (Exception ex) {
					// Log.e("BlogActivity", ex.getMessage());
				}
			}
			// 下拉
			else {
				dataAdp.AddMoreData(result);
			}
			// 刷新时处理
			if (isRefresh) {
				((PullToRefreshListView) mListView).onRefreshComplete();
			}
		}

		@Override
		protected void onPreExecute() {
			// 主体进度条
			if (mListView.getCount() == 0) {
				blogBody_progressBar.setVisibility(View.VISIBLE);
			}
			// 右上角
			blog_progress_bar.setVisibility(View.VISIBLE);
			blog_refresh_btn.setVisibility(View.GONE);

			if (!isRefresh) {// 底部控件，刷新时不做处理

				// foot_TextView = (TextView) findViewById(R.id.foot_TextView);
				// foot_TextView.setText("加载更多...");
				// foot_TextView.setTextSize(20);
				// foot_TextView.setGravity(Gravity.CENTER);
				// foot_TextView.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// pageIndex = pageIndex + 1;
				// new PageTask(pageIndex, false).execute();
				//
				// }
				// });
				// foot_TextView.setVisibility(View.VISIBLE);

				// foot_progress = (ProgressBar) footLayout
				// .findViewById(R.id.list_footer_progress);
				// foot_progress.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
		}
	}

	// ****************************************以下为菜单操作
	/**
	 * 跳转到评论
	 * 
	 * @param v
	 */
	private void RedirectCommentActivity(View v) {
		TextView tvBlogCommentCount = (TextView) (v
				.findViewById(R.id.recommend_text_comments));
		TextView tvBlogId = (TextView) (v.findViewById(R.id.recommend_text_id));
		TextView tvBlogTitle = (TextView) (v
				.findViewById(R.id.recommend_text_title));
		TextView tvBlogUrl = (TextView) (v
				.findViewById(R.id.recommend_text_url));
		int blogId = Integer.parseInt(tvBlogId.getText().toString());
		int commentCount = Integer.parseInt(tvBlogCommentCount.getText()
				.toString());
		String blogTitle = tvBlogTitle.getText().toString();
		String blogUrl = tvBlogUrl.getText().toString();
		// 还没有评论
		if (commentCount == 0) {
			Toast.makeText(getApplicationContext(), R.string.sys_empty_comment,
					Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent();
		intent.setClass(BlogListView.this, CommentActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("contentId", blogId);
		bundle.putInt("commentType", 0);// Comment.EnumCommentType.News.ordinal());
		bundle.putString("title", blogTitle);
		bundle.putString("url", blogUrl);

		intent.putExtras(bundle);

		startActivity(intent);
	}

	/**
	 * 跳转到详情
	 * 
	 * @param v
	 */
	private void RedirectDetailActivity(View v) {

		Intent intent = new Intent();
		try {
			// 传递参数
			intent.setClass(BlogListView.this, BlogDetailView.class);
			Bundle bundle = new Bundle();
			TextView tvBlogId = (TextView) (v
					.findViewById(R.id.recommend_text_id));
			TextView tvBlogTitle = (TextView) (v
					.findViewById(R.id.recommend_text_title));
			TextView tvBlogAuthor = (TextView) (v
					.findViewById(R.id.recommend_text_author));
			TextView tvBlogDate = (TextView) (v
					.findViewById(R.id.recommend_text_date));
			TextView tvBlogUrl = (TextView) (v
					.findViewById(R.id.recommend_text_url));
			TextView tvBlogViewCount = (TextView) (v
					.findViewById(R.id.recommend_text_view));
			TextView tvBlogCommentCount = (TextView) (v
					.findViewById(R.id.recommend_text_comments));
			TextView tvBlogDomain = (TextView) (v
					.findViewById(R.id.recommend_text_domain));

			int blogId = Integer.parseInt(tvBlogId.getText().toString());
			String blogTitle = tvBlogTitle.getText().toString();
			String blogAuthor = tvBlogAuthor.getText().toString();
			String blogDate = tvBlogDate.getText().toString();
			String blogUrl = tvBlogUrl.getText().toString();
			String blogDomain = tvBlogDomain.getText().toString();
			int viewsCount = Integer.parseInt(tvBlogViewCount.getText()
					.toString());
			int commentCount = Integer.parseInt(tvBlogCommentCount.getText()
					.toString());

			bundle.putInt("blogId", blogId);
			bundle.putString("blogTitle", blogTitle);
			bundle.putString("author", blogAuthor);
			bundle.putString("date", blogDate);
			bundle.putString("blogUrl", blogUrl);
			bundle.putInt("view", viewsCount);
			bundle.putInt("comment", commentCount);
			bundle.putString("blogDomain", blogDomain);

			Log.d("blogId", String.valueOf(blogId));
			intent.putExtras(bundle);

			startActivity(intent);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 在浏览器中查看
	 * 
	 * @param v
	 */
	private void ViewInBrowser(View v) {
		TextView tvBlogUrl = (TextView) (v
				.findViewById(R.id.recommend_text_url));
		String blogUrl = tvBlogUrl.getText().toString();
		Uri blogUri = Uri.parse(blogUrl);
		Intent it = new Intent(Intent.ACTION_VIEW, blogUri);
		startActivity(it);
	}

	/**
	 * 跳转到博主所有随笔
	 * 
	 * @param v
	 */
	private void RedirectAuthorActivity(View v) {
		TextView tvUserName = (TextView) v
				.findViewById(R.id.recommend_user_name);// 用户名
		String userName = tvUserName.getText().toString();
		if (userName.equals("")) {
			Toast.makeText(getApplicationContext(), R.string.sys_no_author,
					Toast.LENGTH_SHORT).show();
			return;
		}
		TextView tvBlogAuthor = (TextView) (v
				.findViewById(R.id.recommend_text_author));
		String blogAuthor = tvBlogAuthor.getText().toString();

		Intent intent = new Intent();
		intent.setClass(BlogListView.this, AuthorBlogActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("author", userName);// 用户名
		bundle.putString("blogName", blogAuthor);// 博客标题

		intent.putExtras(bundle);

		startActivity(intent);
	}

	/**
	 * 分享到
	 * 
	 * @param v
	 */
	private void ShareTo(View v) {
		TextView tvBlogTitle = (TextView) (v
				.findViewById(R.id.recommend_text_title));
		String blogTitle = tvBlogTitle.getText().toString();
		TextView tvBlogAuthor = (TextView) (v
				.findViewById(R.id.recommend_text_author));
		String blogAuthor = tvBlogAuthor.getText().toString();
		TextView tvBlogUrl = (TextView) (v
				.findViewById(R.id.recommend_text_url));
		String blogUrl = tvBlogUrl.getText().toString();
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "请选择分享到…");
		String shareContent = "《" + blogTitle + "》,作者：" + blogAuthor + "，原文链接："
				+ blogUrl + " 分享自：" + res.getString(R.string.app_name)
				+ "Android客户端(" + res.getString(R.string.app_homepage) + ")";
		intent.putExtra(Intent.EXTRA_TEXT, shareContent);
		startActivity(Intent.createChooser(intent, blogTitle));
	}

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int itemIndex = item.getItemId();
		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		View v = menuInfo.targetView;
		switch (itemIndex) {
		case R.id.menu_blog_view:// 详细
			RedirectDetailActivity(v);
			break;
		case R.id.menu_blog_comment:// 评论
			RedirectCommentActivity(v);
			break;
		case R.id.menu_blog_author:// 博主所有随笔
			RedirectAuthorActivity(v);
			break;
		case R.id.menu_blog_browser:// 在浏览器中查看
			ViewInBrowser(v);
			break;
		case R.id.menu_blog_share:// 分享到
			ShareTo(v);
			break;
		}

		return super.onContextItemSelected(item);
	}

	/**
	 * 更新ListView为已读状态 此广播同时从BlogDeatail和DownloadServices
	 * 
	 * @author walkingp
	 * 
	 */
	public class UpdateListViewReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context content, Intent intent) {

			Bundle bundle = intent.getExtras();
			int[] blogIdArr = bundle.getIntArray("blogIdArray");
			for (int i = 0, len = mListView.getChildCount(); i < len; i++) {
				View view = mListView.getChildAt(i);
				TextView tvId = (TextView) view
						.findViewById(R.id.recommend_text_id);
				if (tvId != null) {
					/*
					 * int blogId=Integer.parseInt(tvId.getText().toString());
					 * 
					 * ImageView
					 * icoDown=(ImageView)view.findViewById(R.id.icon_downloaded
					 * ); TextView
					 * tvTitle=(TextView)view.findViewById(R.id.recommend_text_title
					 * );
					 * 
					 * for(int j=0,size=blogIdArr.length;j<size;j++){
					 * if(blogId==blogIdArr[j]){
					 * icoDown.setVisibility(View.VISIBLE);//已经离线
					 * tvTitle.setTextColor(R.color.gray);//已读 } }
					 */
				}
			}
			for (int i = 0, len = blogIdArr.length; i < len; i++) {
				for (int j = 0, size = listBlog.size(); j < size; j++) {
					if (blogIdArr[i] == listBlog.get(j).GetBlogId()) {
						listBlog.get(i).SetIsFullText(true);
						listBlog.get(i).SetIsReaded(true);
					}
				}
			}
		}
	}
}
