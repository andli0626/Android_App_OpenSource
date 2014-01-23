package com.ch_linghu.fanfoudroid.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.ch_linghu.fanfoudroid.AppParam;
import com.ch_linghu.fanfoudroid.R;
import com.ch_linghu.fanfoudroid.data.Tweet;
import com.ch_linghu.fanfoudroid.db.StatusTable;
import com.ch_linghu.fanfoudroid.fanfou.Paging;
import com.ch_linghu.fanfoudroid.fanfou.Status;
import com.ch_linghu.fanfoudroid.http.HttpException;
import com.ch_linghu.fanfoudroid.task.GenericTask;
import com.ch_linghu.fanfoudroid.task.AbstractTaskAdapter;
import com.ch_linghu.fanfoudroid.task.ITaskListener;
import com.ch_linghu.fanfoudroid.task.TaskParams;
import com.ch_linghu.fanfoudroid.task.TaskResult;
import com.ch_linghu.fanfoudroid.task.TweetCommonTask;
import com.ch_linghu.fanfoudroid.ui.base.CourseBaseView;

public class MainView extends CourseBaseView {
	private static final String TAG = "andli";

	private static final String LAUNCH_ACTION = "com.ch_linghu.fanfoudroid.TWEETS";
	protected GenericTask mDeleteTask;

	private ITaskListener mDeleteTaskListener = new AbstractTaskAdapter() {

		@Override
		public String getName() {
			return "DeleteTask";
		}

		@Override
		public void onPostExecute(GenericTask task, TaskResult result) {
			if (result == TaskResult.AUTH_ERROR) {
				logout();
			} else if (result == TaskResult.OK) {
				onDeleteSuccess();
			} else if (result == TaskResult.IO_ERROR) {
				onDeleteFailure();
			}
		}
	};

	static final int DIALOG_WRITE_ID = 0;

	public static Intent createIntent(Context context) {
		Intent intent = new Intent(LAUNCH_ACTION);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		return intent;
	}

	public static Intent createNewTaskIntent(Context context) {
		Intent intent = createIntent(context);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		return intent;
	}

	@Override
	protected boolean _onCreate(Bundle savedInstanceState) {
		if (super._onCreate(savedInstanceState)) {
			mNavbar.setHeaderTitle("饭否fanfou.com");
			// 仅在这个页面进行schedule的处理
			manageUpdateChecks();

			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (mDeleteTask != null
				&& mDeleteTask.getStatus() == GenericTask.Status.RUNNING) {
			mDeleteTask.cancel(true);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mDeleteTask != null
				&& mDeleteTask.getStatus() == GenericTask.Status.RUNNING) {
			outState.putBoolean(SIS_RUNNING_KEY, true);
		}
	}

	// Menu.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	private int CONTEXT_DELETE_ID = getLastContextMenuId() + 1;

	@Override
	protected int getLastContextMenuId() {
		return CONTEXT_DELETE_ID;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		Tweet tweet = getContextItemTweet(info.position);
		if (null != tweet) {// 当按钮为 刷新/更多的时候为空

			if (tweet.userId.equals(AppParam.getMyselfId(false))) {
				menu.add(0, CONTEXT_DELETE_ID, 0, R.string.cmenu_delete);
			}

		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Tweet tweet = getContextItemTweet(info.position);

		if (tweet == null) {
			Log.w(TAG, "Selected item not available.");
			return super.onContextItemSelected(item);
		}

		if (item.getItemId() == CONTEXT_DELETE_ID) {
			doDelete(tweet.id);
			return true;
		} else {
			return super.onContextItemSelected(item);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Cursor fetchMessages() {
		return getDb().fetchAllTweets(getUserId(), StatusTable.TYPE_HOME);
	}

	@Override
	protected String getActivityTitle() {
		return getResources().getString(R.string.page_title_home);
	}

	@Override
	protected void markAllRead() {
		getDb().markAllTweetsRead(getUserId(), StatusTable.TYPE_HOME);
	}

	// hasRetrieveListTask interface
	@Override
	public int addMessages(ArrayList<Tweet> tweets, boolean isUnread) {
		// 获取消息的时候，将status里获取的user也存储到数据库

		// ::MARK::
		for (Tweet t : tweets) {
			getDb().createWeiboUserInfo(t.user);
		}
		return getDb().putTweets(tweets, getUserId(), StatusTable.TYPE_HOME,
				isUnread);
	}

	@Override
	public String fetchMaxId() {
		return getDb().fetchMaxTweetId(getUserId(), StatusTable.TYPE_HOME);
	}

	@Override
	public List<Status> getMessageSinceId(String maxId) throws HttpException {
		if (maxId != null) {
			return getApi().getFriendsTimeline(new Paging(maxId));
		} else {
			return getApi().getFriendsTimeline();
		}
	}

	public void onDeleteFailure() {
		Log.e(TAG, "Delete failed");
	}

	public void onDeleteSuccess() {
		mTweetAdapter.refresh();
	}

	private void doDelete(String id) {

		if (mDeleteTask != null
				&& mDeleteTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			mDeleteTask = new TweetCommonTask.DeleteTask(this);
			mDeleteTask.setListener(mDeleteTaskListener);

			TaskParams params = new TaskParams();
			params.put("id", id);
			mDeleteTask.execute(params);
		}
	}

	@Override
	public String fetchMinId() {
		return getDb().fetchMinTweetId(getUserId(), StatusTable.TYPE_HOME);
	}

	@Override
	public List<Status> getMoreMessageFromId(String minId) throws HttpException {
		Paging paging = new Paging(1, 20);
		paging.setMaxId(minId);
		return getApi().getFriendsTimeline(paging);
	}

	@Override
	public int getDatabaseType() {
		return StatusTable.TYPE_HOME;
	}

	@Override
	public String getUserId() {
		return AppParam.getMyselfId(false);
	}
}