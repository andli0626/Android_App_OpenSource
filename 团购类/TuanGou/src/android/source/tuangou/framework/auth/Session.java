package android.source.tuangou.framework.auth;

import android.source.tuangou.framework.net.JsonParser;
import android.source.tuangou.framework.net.NetworkService;
import android.source.tuangou.framework.store.beans.Preferences;
import android.source.tuangou.framework.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * Session数据类
 * */
public class Session{
	public class User
	{

		public String id;
		public String name;//用户名
		public String password;//密码
		final Session this$0;//

		public User(String s, String s1, String s2)
		{
			super();
			this$0 = Session.this;
			id = s;
			name = s1;
			password = s2;
		}
	}


	public static final String USER_ID_LABEL = "login_user_id";
	public static final String USER_NAME_LABEL = "login_user_name";
	public static final String USER_PASSWORD_LABEL = "login_user_password";
	User currentLoginUser;

	public Session(){
		
	}

	public User getCurrentLoginUser()
	{
		if (currentLoginUser == null)
		{
			String s = Preferences.getInstance().get("login_user_id");
			String s1 = Preferences.getInstance().get("login_user_name");
			String s2 = Preferences.getInstance().get("login_user_password");
			if (!StringUtil.isEmpty(s).booleanValue() && !StringUtil.isEmpty(s1).booleanValue() && !StringUtil.isEmpty(s2).booleanValue())
			{
				User user = new User(s, s1, s2);
				currentLoginUser = user;
			}
		}
		return currentLoginUser;
	}

	public boolean isLogin()
	{
		boolean flag;
		if (getCurrentLoginUser() != null)
			flag = true;
		else
			flag = false;
		return flag;
	}

	//登录函数
	public int login(String s, String s1){
		int i = -1;
		HashMap hashmap = new HashMap();
		Object obj = hashmap.put("username", s);
		Object obj1 = hashmap.put("password", s1);
		
		//获取网络服务
		NetworkService networkservice = NetworkService.sharedInstance();
		
		final String final_as[] = new String[0];
		
		JsonParserLogin mJsonParserLogin = new JsonParserLogin(final_as,s1);
		Map map = networkservice.postSync("http://api.tuan800.com/mobile_api/android/login", hashmap, mJsonParserLogin);
		
		if (map != null && map.containsKey("status")){
			//获取状态
			i = ((Integer)map.get("status")).intValue();
		}
		return i;
	}

	//注销函数
	public int logout(){
		NetworkService networkservice = NetworkService.sharedInstance();
		String as[] = new String[0];
		JsonParser2 mJsonParser2 = new JsonParser2(as);
		Map map = networkservice.postSync("http://api.tuan800.com/mobile_api/android/logout", null, mJsonParser2);
		return 0;
	}

	private class JsonParserLogin extends JsonParser{

		final Session this$0;
		final String password;

		public void parseJson(JSONObject jsonobject, Map map)
			throws JSONException{
			int i = jsonobject.getInt("status");
			Integer integer = Integer.valueOf(i);
			Object obj = map.put("status", integer);
			
			if (i == 0){
				JSONObject jsonobject1 = jsonobject.getJSONObject("user");
				String s = jsonobject1.getString("id");
				String s1 = jsonobject1.getString("name");
				Session session = Session.this;
				Session session1 = Session.this;
				String s2 = password;
				User user = session1. new User(s, s1, s2);
				session.currentLoginUser = user;
				Preferences.getInstance().save("login_user_id", s);
				Preferences.getInstance().save("login_user_name", s1);
				Preferences preferences = Preferences.getInstance();
				String s3 = password;
				preferences.save("login_user_password", s3);
			}
		}

		public JsonParserLogin(String as[],String s){
			super(as);
			this$0 = Session.this;
			password = s;
		}
	}


	private class JsonParser2 extends JsonParser
	{

		final Session this$0;

		public void parseJson(JSONObject jsonobject, Map map)
			throws JSONException
		{
			currentLoginUser = null;
			Preferences.getInstance().save("login_user_id", "");
			Preferences.getInstance().save("login_user_name", "");
			Preferences.getInstance().save("login_user_password", "");
		}

		public JsonParser2(String as[])
		{
			super(as);
			this$0 = Session.this;
		}
	}

}
