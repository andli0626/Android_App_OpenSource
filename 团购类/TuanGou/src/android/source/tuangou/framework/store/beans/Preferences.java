package android.source.tuangou.framework.store.beans;

import android.source.tuangou.framework.ServiceManager;
import android.source.tuangou.framework.net.JsonParser;
import android.source.tuangou.framework.net.NetworkService;
import android.source.tuangou.framework.store.Bean;
import android.source.tuangou.framework.store.Database;
import android.source.tuangou.framework.util.LogUtil;
import android.source.tuangou.framework.util.StringUtil;
import android.source.tuangou.util.Base64;
import android.telephony.TelephonyManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class Preferences extends Bean{

	private static Preferences instance;
	public static String requestKeyName = "";
	private static final String tableName = "preferences";
	private String requestKey;

	public Preferences(){

	}

	//创建Preferences对象
	public static Preferences getInstance(){
		if (instance == null)
			instance = new Preferences();
		return instance;
	}

	//数据库中创建表
	public void createTable(){
		String aobj[] = new String[1];
		aobj[0] = "preferences";
		
		//创建preferences表
		String sql = StringUtil.simpleFormat("CREATE TABLE if not exists %s (key TEXT PRIMARY KEY, value TEXT, expire_time INTEGER);", aobj);
		if(db != null){
			boolean flag = db.execSql(sql);
		}
	}

	
	public String get(String s){
		StringBuilder stringbuilder = (new StringBuilder()).
			append("SELECT value from %s WHERE key=? AND (expire_time=-1 OR expire_time>");
		//获取系统当前毫秒数，再转换成分钟数
		long l = System.currentTimeMillis() / 1000L;
		String s1 = stringbuilder.append(l).append(")").toString();
		Object aobj[] = new Object[1];
		aobj[0] = "preferences";
		
		//String s2 = String.format(s1, aobj);
		String sql = StringUtil.simpleFormat(s1, aobj);
		
		String asselectArgs[] = new String[1];
		asselectArgs[0] = s;
		
		System.out.println("getCity sql = "+sql+" asselectArgs = "+asselectArgs);
		return db.getSingleString(sql, asselectArgs);
	}

	public String getDefault(String s, String s1)
	{
		String s2 = get(s);
		String s3;
		if (StringUtil.isEmpty(s2).booleanValue())
			s3 = s1;
		else
			s3 = s2;
		return s3;
	}

	public String getRequestKey()
	{
		if (StringUtil.isEmpty(requestKey).booleanValue())
		{
			String s = requestKeyName;
			String s1 = get(s);
			requestKey = s1;
		}
		return requestKey;
	}

	public void initRequestKey(TelephonyManager telephonymanager, String s, String s1)
	{
		if (!StringUtil.isEmpty(getRequestKey()).booleanValue()){
			return;
		} else {
			String s2 = telephonymanager.getDeviceId();
			if (!StringUtil.isEmpty(s2).booleanValue()){
				try {
					String s3;
					String s4;
					String s5;
					String s6;
					MessageDigest messagedigest;
					MessageDigest messagedigest1;
					s3 = Base64.encodeToString(s2.getBytes(), 0);
					s4 = telephonymanager.getLine1Number();
					if (s4 == null)
						s4 = "";
					s5 = Base64.encodeToString(s4.getBytes(), 0);
					messagedigest = null;
					messagedigest1 = null;
					MessageDigest messagedigest2 = MessageDigest
							.getInstance("SHA-1");
					messagedigest1 = messagedigest2;
					//
					messagedigest2 = MessageDigest.getInstance("MD5");
					messagedigest = messagedigest2;
					HashMap hashmap = new HashMap();
					HashMap hashmap1 = hashmap;
					String s8 = "deviceId";
					String s9 = s3;
					Object obj = hashmap1.put(s8, s9);
					HashMap hashmap2 = hashmap;
					String s10 = "tel";
					String s11 = s5;
					Object obj1 = hashmap2.put(s10, s11);
					String s12 = Base64.encodeToString(s1.getBytes(), 0);
					HashMap hashmap3 = hashmap;
					String s13 = s;
					String s14 = s12;
					Object obj2 = hashmap3.put(s13, s14);
					StringBuilder stringbuilder = new StringBuilder();
					String s15 = s4;
					StringBuilder stringbuilder1 = stringbuilder.append(s15)
							.append("5jHJ@^9B32!");
					StringBuilder stringbuilder2 = (new StringBuilder())
							.append("`sFn+?ss00");
					String s16 = s4;
					StringBuilder stringbuilder3 = stringbuilder2.append(s16);
					String s17 = s3;
					byte abyte0[] = stringbuilder3.append(s17).toString()
							.getBytes();
					MessageDigest messagedigest3 = messagedigest;
					byte abyte1[] = abyte0;
					String s18 = StringUtil.fromBytes(messagedigest3
							.digest(abyte1));
					StringBuilder stringbuilder4 = stringbuilder1.append(s18);
					String s19 = s2;
					StringBuilder stringbuilder5 = stringbuilder4.append(s19);
					String s20 = s1;
					byte abyte2[] = stringbuilder5.append(s20).toString()
							.getBytes();
					MessageDigest messagedigest4 = messagedigest1;
					byte abyte3[] = abyte2;
					String s21 = StringUtil.fromBytes(messagedigest4
							.digest(abyte3));
					StringBuilder stringbuilder6 = new StringBuilder();
					String s22 = s4;
					StringBuilder stringbuilder7 = stringbuilder6.append(s22)
							.append("5jHJ@^9B32!");
					StringBuilder stringbuilder8 = (new StringBuilder())
							.append("`sFn+?ss00");
					String s23 = s4;
					StringBuilder stringbuilder9 = stringbuilder8.append(s23);
					String s24 = s3;
					byte abyte4[] = stringbuilder9.append(s24).toString()
							.getBytes();
					MessageDigest messagedigest5 = messagedigest;
					byte abyte5[] = abyte4;
					String s25 = StringUtil.fromBytes(messagedigest5
							.digest(abyte5));
					StringBuilder stringbuilder10 = stringbuilder7.append(s25);
					String s26 = s2;
					StringBuilder stringbuilder11 = stringbuilder10.append(s26);
					String s27 = s1;
					byte abyte6[] = stringbuilder11.append(s27).toString()
							.getBytes();
					MessageDigest messagedigest6 = messagedigest1;
					byte abyte7[] = abyte6;
					String s28 = StringUtil.fromBytes(messagedigest6
							.digest(abyte7));
					HashMap hashmap4 = hashmap;
					String s29 = "sign";
					String s30 = s28;
					Object obj3 = hashmap4.put(s29, s30);
					NetworkService networkservice = ServiceManager
							.getNetworkService();
					String as[] = new String[0];
					Preferences preferences = this;
					String as1[] = as;
					JsonParser1 mJsonParser1 = new JsonParser1(as);
					NetworkService networkservice1 = networkservice;
					String s31 = "http://api.tuan800.com/mobile_api/android/get_request_key";
					HashMap hashmap5 = hashmap;
					Map map = networkservice1.postSync(s31, hashmap5,
							mJsonParser1);
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
			} 
			else{
				return;
			}
		}
	}

	public void save(String key, String value){
		save(key, value, 65535L);
	}

	public void save(String key, String value, long time){
		Object aobj[] = new Object[1];
		aobj[0] = "preferences";
		
		String sql = StringUtil.simpleFormat("REPLACE INTO %s (key, value, expire_time) VALUES(?, ?, ?)", aobj);
		
		Object aobj1[] = new Object[3];
		aobj1[0] = key;
		aobj1[1] = value;
		Long long1 = Long.valueOf(time);
		aobj1[2] = long1;

		
		System.out.println("sql = "+sql);
		for(int i = 0; i < aobj1.length; i++){
			System.out.println("aobj1["+i+"] = "+aobj1[i]);
		}
		
		boolean flag = db.execSql(sql, aobj1);
	}

	static 
	{
		requestKeyName = "requestKey";
	}


	private class JsonParser1 extends JsonParser
	{

		final Preferences this$0;
		
		public JsonParser1(String as[]){
			super(as);

			this$0 = Preferences.this;
		}

		public void parseJson(JSONObject jsonobject, Map map)
			throws JSONException{
			if (jsonobject.getInt("status") == 0){
				String s = jsonobject.getString("requestKey");
				if (s != null && s.length() != 0)
				{
					Preferences preferences = Preferences.this;
					String s1 = Preferences.requestKeyName;
					preferences.save(s1, s);
					String s2 = requestKey = s;
					
				}
			}
		}
		
	}

}
