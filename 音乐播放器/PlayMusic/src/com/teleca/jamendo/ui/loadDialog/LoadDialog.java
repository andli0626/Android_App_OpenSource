package com.teleca.jamendo.ui.loadDialog;

import com.teleca.jamendo.model.ErrorMsg;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * 异步加载对话框
 * 
 * @author lilin
 * @date 2012-1-6 下午09:41:25
 * @ClassName: LoadingDialog
 */
public abstract class LoadDialog<Input, Result> extends
		AsyncTask<Input, ErrorMsg, Result> {
	// 滚动条
	private ProgressDialog mProgressDialog;
	protected Activity mActivity;
	// 加载的提示信息
	private int id_loadMsg = 0;
	private int id_failMsg = 0;
	private String loadmsg = "";
	private String failmsg = "";

	// 采用ID来获取String的值
	public LoadDialog(Activity activity, int loadingMsg, int failMsg) {
		this.mActivity = activity;
		this.id_loadMsg = loadingMsg;
		this.id_failMsg = failMsg;
	}

	// 直接赋值String
	public LoadDialog(Activity activity, String loadingMsg, String failMsg) {
		this.mActivity = activity;
		loadmsg = loadingMsg;
		failmsg = failMsg;
	}

	public abstract void doStuffWithResult(Result result);

	public abstract Result doInBackground(Input... params);

	@Override
	public void onCancelled() {
		failLoadMsg();
		super.onCancelled();
	}

	@Override
	public void onPreExecute() {
		String title = "请耐心等待";
		String msg = "";
		if (!loadmsg.equals("")) {
			msg = loadmsg;
		}
		if (id_loadMsg != 0) {
			msg = mActivity.getString(id_loadMsg);
		}
		mProgressDialog = ProgressDialog.show(mActivity, title, msg, true,
				true, new OnCancelListener() {
					public void onCancel(DialogInterface dialogInterface) {
						LoadDialog.this.cancel(true);
					}
				});
		super.onPreExecute();
	}

	@Override
	public void onPostExecute(Result result) {
		super.onPostExecute(result);
		mProgressDialog.dismiss();
		if (result != null) {
			doStuffWithResult(result);
		} else {
			failLoadMsg();
		}
	}

	@Override
	protected void onProgressUpdate(ErrorMsg... values) {
		Toast.makeText(mActivity, values[0].getMessage(), 3000).show();
		this.cancel(true);
		mProgressDialog.dismiss();
		super.onProgressUpdate(values);
	}

	protected void failLoadMsg() {
		String msg = "";
		if (!failmsg.equals("")) {
			msg = failmsg;
		}
		if (id_failMsg != 0) {
			msg = mActivity.getString(id_failMsg);
		}
		Toast.makeText(mActivity, msg, 2000).show();
	}

}
