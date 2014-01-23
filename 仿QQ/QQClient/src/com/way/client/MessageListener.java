package com.way.client;

import com.way.chat.common.tran.bean.TranObject;
/**
 * 消息监听接口，随时监听读线程收到的消息对象
 * @author way
 *
 */
public interface MessageListener {
	public void Message(TranObject msg);
}
