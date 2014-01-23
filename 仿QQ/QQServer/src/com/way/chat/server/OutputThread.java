package com.way.chat.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.way.chat.common.tran.bean.TranObject;

public class OutputThread extends Thread {
	private int threadId;
	private OutputThreadMap map;
	private ObjectOutputStream oos;
	private TranObject<?> object;
	private boolean isStart = true;
	private Socket socket;

	public OutputThread(Socket socket, OutputThreadMap map) {
		try {
			this.socket = socket;
			this.map = map;
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public void removeThread() {
		map.remove(threadId);
	}

	// 设置了消息之后，唤醒写线程
	public void setMessage(TranObject<?> object) {
		this.object = object;
		synchronized (this) {
			notify();
		}
	}

	@Override
	public void run() {
		try {
			while (isStart) {
				// 没有消息写出的时候，线程等待
				synchronized (this) {
					wait();
				}
				if (object != null) {
					oos.writeObject(object);
					oos.flush();
				}
			}
			if (oos != null)
				oos.close();
			if (socket != null)
				socket.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
//			e.printStackTrace();
//			isStart = false;
		}
	}
}
