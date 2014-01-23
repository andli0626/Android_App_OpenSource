package android.source.tuangou.framework.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.source.tuangou.framework.file.FileHelper;
import android.source.tuangou.framework.util.LogUtil;

import java.io.*;
import java.net.*;

public class FileDownload
{
	Context context;
	public FileDownload()
	{
	}

	public FileDownload(Context context1)
	{
		context = context1;
	}

	//文件下载函数
	private void doDownload(String s, String s1, Handler handler){
		int i;
		FileOutputStream fileoutputstream;
		int j;
		byte abyte0[];
		int k;
		
		try {
			URLConnection urlconnection = (new URL(s)).openConnection();
			urlconnection.connect();
			i = urlconnection.getContentLength();
			InputStream inputstream = urlconnection.getInputStream();
			int l;
			if (context == null)
				fileoutputstream = new FileOutputStream(s1);
			else
				fileoutputstream = context.openFileOutput(s1, 3);
			
			j = 0;

			sendProgressMessage(handler, 0, 0);
			while(true){
				abyte0 = new byte[1024];
				k = inputstream.read(abyte0);
				
				if (k >= 0) {
					fileoutputstream.write(abyte0, 0, k);
					j += k;
					int i1 = (j * 100) / i;
					sendProgressMessage(handler, i1, 1);
				} else{
					inputstream.close();
					fileoutputstream.close();
					l = (j * 100) / i;
					sendProgressMessage(handler, l, 2);
					break;
				}
			}
		
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void sendProgressMessage(Handler handler, int i, int j)
	{
		if (handler != null)
		{
			Message message = new Message();
			message.what = j;
			message.arg1 = i;
			boolean flag = handler.sendMessage(message);
		}
	}

	//下载文件
	public void download(final String remoteFile, final String localFile, final Handler progressHandler){
		boolean flag;
		
		//判断本地此文件是否存在，如果存在的话则删除
		if ((new File(localFile)).exists()){
			flag = FileHelper.delete(localFile);
		}
		
		FileRunnable mRunnable = new FileRunnable(remoteFile,localFile,progressHandler);
		(new Thread(mRunnable)).start();
	}

	public void setContext(Context context1)
	{
		context = context1;
	}


	//文件下载的线程函数
	private class FileRunnable
		implements Runnable{

		final FileDownload this$0;
		final String localFile;
		final Handler progressHandler;
		final String remoteFile;

		//文件下载
		public void run(){
			FileDownload filedownload = FileDownload.this;
			String s = remoteFile;
			String s1 = localFile;
			Handler handler = progressHandler;
			filedownload.doDownload(s, s1, handler);
		}
		
		FileRunnable(final String remoteFile, final String localFile, final Handler progressHandler)
		{
			this$0 = FileDownload.this;
			this.remoteFile = remoteFile;
			this.localFile = localFile;
			this.progressHandler = progressHandler;
		}
	}

}
