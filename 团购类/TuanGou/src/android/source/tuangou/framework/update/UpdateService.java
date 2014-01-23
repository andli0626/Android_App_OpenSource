package android.source.tuangou.framework.update;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.source.tuangou.framework.Application;
import android.source.tuangou.framework.Config;
import android.source.tuangou.framework.net.FileDownload;
import android.source.tuangou.framework.util.LogUtil;

public class UpdateService
{

	public UpdateService(){

	}

	//客户端更新函数
	public  static void checkClientUpdate(final Handler progressHandler){
		//检查客户端是否需要更新
		if (VersionManager.checkClientUpdate()){
			StringBuilder stringbuilder = (new StringBuilder()).append("New apk found: ");

			String s = VersionManager.getRemoteVersion().currentStableClientVersion;
			StringBuilder stringbuilder1 = stringbuilder.append(s).append(". Current apk version: ");
			String s1 = Config.CLIENT_VERSION;
			LogUtil.d(stringbuilder1.append(s1).toString());
			
			Application application = Application.getInstance();
			
			//文件下载类
			FileDownload filedownload = new FileDownload(application);
			
			StringBuilder stringbuilder2 = new StringBuilder();
			String s2 = Config.REMOTE_UPDATE_PATH;
			StringBuilder stringbuilder3 = stringbuilder2.append(s2);
			String s3 = VersionManager.getRemoteVersion().getCurrentStableClientVersionInfo().fileName;
			String s4 = stringbuilder3.append(s3).toString();
			StringBuilder stringbuilder4 = new StringBuilder();
			String s5 = Application.getAppFilesPath();
			
			String localFilePath = stringbuilder4.append(s5).append("/").append("to_install_tmp.apk").toString();
			
			System.out.println("localFilePath = "+localFilePath);
			
			Handler1 mHandler1 = new Handler1(progressHandler,localFilePath);
			
			System.out.println("s4 = "+s4);
			filedownload.download(s4, "to_install_tmp.apk", mHandler1);
		}
	}

	public static void checkWebFileUpdate(Handler handler)
	{
		checkWebFileUpdate(handler, false);
	}

	//检测客户端版本是否需要更新
	public  static void checkWebFileUpdate(final Handler progressHandler, final boolean stopLooper)
	{
		if (!VersionManager.checkWebFileUpdate()){
			if (stopLooper)
				Looper.myLooper().quit();
			return;
		} else {
			StringBuilder stringbuilder = (new StringBuilder()).append("New web files found: ");
			String s = VersionManager.getRemoteVersion().getCurrentWebVersion();
			StringBuilder stringbuilder1 = stringbuilder.append(s).append(". Current version: ");
			
			String s1 = VersionManager.getLocalVersion().webFileVersion;
			
			LogUtil.d(stringbuilder1.append(s1).toString());
			FileDownload filedownload = new FileDownload();
			StringBuilder stringbuilder2 = new StringBuilder();
			String s2 = Config.REMOTE_UPDATE_PATH;
			StringBuilder stringbuilder3 = stringbuilder2.append(s2);
			String s3 = VersionManager.getRemoteVersion().getCurrentClientVersionInfo().currentWebFile;
			String s4 = stringbuilder3.append(s3).toString();
			StringBuilder stringbuilder4 = new StringBuilder();
			String s5 = Application.getAppFilesPath();
			StringBuilder stringbuilder5 = stringbuilder4.append(s5).append("/");
			String s6 = Config.UPDATE_FILE_FOLDER;
			final String localFilePath = stringbuilder5.append(s6).append("/web_tmp.zip").toString();
			
			Handler2 mHander2 = new Handler2(progressHandler,stopLooper,localFilePath);
			filedownload.download(s4, localFilePath, mHander2);
		}
	}

	private static class Handler1 extends Handler
	{

		final String localFilePath;
		final Handler progressHandler;

		public void handleMessage(Message message){
			System.out.println("message.what = "+message.what);
			
			if (progressHandler != null){
				Message message1 = new Message();
				message1.what = 4;
				
				int i = message.arg1;
				message1.arg1 = i;
				
				boolean flag = progressHandler.sendMessage(message1);
			}
			
			boolean flag1;
			if (message.what == 3 && progressHandler != null)
				flag1 = progressHandler.sendEmptyMessage(6);
			if (message.what == 2){
				ApkInstaller.install(localFilePath);
			}
		}

		Handler1(Handler handler, String localFilePath){
			super();
			progressHandler = handler;
			this.localFilePath = localFilePath;
		}
	}


	private static class Handler2 extends Handler
	{

		final String localFilePath;
		final Handler progressHandler;
		final boolean stopLooper;

		public void handleMessage(Message message){
			try {
				if (progressHandler != null) {
					Message message1 = new Message();
					message1.what = 4;
					int i = message.arg1;
					message1.arg1 = i;
					boolean flag = progressHandler.sendMessage(message1);
				}
				if (stopLooper && (message.what == 2 || message.what == 3)) {
					Looper.myLooper().quit();
				}
				if (message.what != 2) {
					return;
				} else {
					String s = localFilePath;
					StringBuilder stringbuilder = new StringBuilder();
					String s1 = Application.getAppFilesPath();
					StringBuilder stringbuilder1 = stringbuilder.append(s1)
							.append("/");
					String s2 = Config.WEB_FILE_FOLDER;
					String s3 = stringbuilder1.append(s2).toString();
					ZipHelper.unZipFolder(s, s3);
				}
				VersionManager.updateLocalWebFileVersion();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}

		Handler2(Handler handler, boolean flag, String localFilePath){
			super();
			progressHandler = handler;
			stopLooper = flag;
			this.localFilePath = localFilePath;
		}
	}

}
