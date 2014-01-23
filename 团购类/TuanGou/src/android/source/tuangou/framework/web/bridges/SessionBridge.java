package android.source.tuangou.framework.web.bridges;

import android.source.tuangou.framework.Application;
import android.source.tuangou.framework.auth.Session;
import android.source.tuangou.framework.web.ScriptBridge;


public class SessionBridge extends ScriptBridge
{

	public SessionBridge()
	{
	}

	public boolean isLogin()
	{
		return Application.getSession().isLogin();
	}

	//登录函数
	public int login(String userName, String password){
		System.out.println("javaScript call java SessionBridge login()");
		
		return Application.getSession().login(userName, password);
	}

	public int logout(){
		return Application.getSession().logout();
	}
}
