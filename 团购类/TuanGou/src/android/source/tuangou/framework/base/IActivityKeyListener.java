package android.source.tuangou.framework.base;

import android.view.KeyEvent;

/*
 * activity按键事件的监听接口类
 * */
public interface IActivityKeyListener{

	public abstract boolean onKeyDown(int i, KeyEvent keyevent);
}
