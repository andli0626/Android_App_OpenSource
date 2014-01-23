package android.source.tuangou.beans;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.source.tuangou.framework.ServiceManager;
import android.source.tuangou.framework.lbs.City;
import android.source.tuangou.framework.net.NetworkService;
import android.source.tuangou.framework.store.Bean;
import android.source.tuangou.framework.store.Database;
import android.source.tuangou.framework.util.StringUtil;

import java.util.*;
import org.json.*;

public class CityList extends Bean
{

	private static final String TB_CITY = "citylist";
	private static final String TB_PREFS = "preferences";
	private static final long WEEK = 0x240c8400L;
	private static CityList instance;
	private SQLiteDatabase dataBase;

	public CityList(){
		init();
	}

	//获取city的list字符串
	private String getCityList(){
		return ServiceManager.getNetworkService().getSync("http://api.tuan800.com/mobile_api/android/get_cities");
	}

	public static CityList getInstance()
	{
		if (instance == null)
			instance = new CityList();
		return instance;
	}

	
	//初始化函数
	private void init(){
		//获取数据库
		dataBase = db.getDb();
		
		String as[] = new String[1];
		as[0] = "update_city";
		
		Cursor cursor = dataBase.rawQuery("SELECT value FROM preferences WHERE key=?", as);
		
		if (cursor.getCount() != 0){
			if (cursor.moveToFirst()){
				int i = cursor.getColumnIndex("value");
				String s = cursor.getString(i);
				long l = (new Long(s)).longValue();
				cursor.close();
				
				if (System.currentTimeMillis() - l >= 604800000L){
					dataBase.execSQL("DELETE FROM citylist");
					SQLiteDatabase sqlitedatabase2 = dataBase;
					String as1[] = new String[1];
					as1[0] = "update_city";
					sqlitedatabase2.execSQL("DELETE FROM preferences WHERE key=?", as1);
					saveCityList();
				}
			}
		} else{
			saveCityList();
		}
	}

	//保存city列表
	private void saveCityList(){
		ArrayList arraylist;
		String s;
		arraylist = new ArrayList();
		//获取city的list
		s = getCityList();
		if (s == null){
			return;
		}
		
		try {
			//获取city的JSONArray数组
			JSONArray jsonarray = (new JSONObject(s)).getJSONArray("cities");
			int j = jsonarray.length();
			System.out.println("jsonarray = "+jsonarray);
			
			for (int i = 0; i < j; i++) {
				JSONObject jsonobject = jsonarray.getJSONObject(i);
				//创建city对象
				City city = new City();
				String s1 = jsonobject.getString("id");
				city.id = s1;
				String s2 = jsonobject.getString("name");
				city.name = s2;
				String s3 = jsonobject.getString("pinyin");
				city.pinyin = s3;
				
				//list中添加city对象
				boolean flag = arraylist.add(city);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		String citySQL;
		String preSQL;
		try {
			
			citySQL = "INSERT INTO citylist(cityId, name, pinyin, json) values(?, ?, ?, ?)";
			preSQL = "INSERT INTO preferences(key, value, expire_time) values(?, ?, ?)";
			
			//开启一个事物
			dataBase.beginTransaction();
			
			Object aobj[] = new Object[3];
			aobj[0] = "update_city";
			
			Long long1 = Long.valueOf(System.currentTimeMillis());
			aobj[1] = long1;
			
			Integer integer = Integer.valueOf(65535);
			aobj[2] = integer;
			
			//执行sql遇见
			dataBase.execSQL(preSQL, aobj);

			Object aobj1[] = new Object[4];
			aobj1[0] = 0;
			aobj1[1] = 0;
			aobj1[2] = 0;
			aobj1[3] = s;
			//执行sql遇见
			dataBase.execSQL(citySQL, aobj1);
			
			SQLiteDatabase sqlitedatabase2;
			Object aobj2[];
			
			Iterator iterator = arraylist.iterator();
			while(iterator.hasNext()){
				City city = (City) iterator.next();
				
				aobj2 = new Object[4];
				
				String s6 = city.id;
				aobj2[0] = s6;
				
				String s7 = city.name;
				aobj2[1] = s7;
				
				String s8 = city.pinyin;
				aobj2[2] = s8;
				
				aobj2[3] = 0;
				
				System.out.println("execSQL SQL = "+citySQL+aobj2);
				dataBase.execSQL(citySQL, aobj2);
				
			}
			//提交事务
			dataBase.setTransactionSuccessful();
		}
		catch(Exception e){
			e.printStackTrace();
			
			dataBase.endTransaction();
		}
		finally {
			 /*
			  * 如果上面SQL语句发生了异常，没有执行db.setTransactionSuccessful()方法，
			  * db.endTransaction()即回滚
			  * */
			dataBase.endTransaction();
			// TODO: handle exception
		}
	}

	public void createTable()
	{
		Object aobj[] = new Object[1];
		aobj[0] = "citylist";
		String s = StringUtil.simpleFormat("CREATE TABLE if not exists %s (cityId INTEGER, name TEXT, pinyin TEXT, json TEXT);", aobj);
		boolean flag = db.execSql(s);
	}

	//获取所有的Json数据
	public String getAllJson(){
		Object aobj[] = new Object[1];
		aobj[0] = "citylist";
		String s = StringUtil.simpleFormat("select json from %s where json <> '';", aobj);
		Object aobj1[][] = db.query(s);
		String s1;
		if (aobj1.length > 0 && aobj1[0].length > 0)
			s1 = aobj1[0][0].toString();
		else
			s1 = "";
		return s1;
	}

	public City getCityById(String s)
	{
		Object aobj[] = new Object[1];
		aobj[0] = "citylist";
		String s1 = StringUtil.simpleFormat("select * from %s where cityId=?;", aobj);
		Database database = db;
		String as[] = new String[1];
		as[0] = s;
		Object aobj1[][] = database.query(s1, as);
		City city = null;
		if (aobj1.length > 0 && aobj1[0].length > 0)
		{
			Object aobj2[] = aobj1[0];
			city = new City();
			String s2 = aobj2[0].toString();
			city.id = s2;
			String s3 = aobj2[1].toString();
			city.name = s3;
			String s4 = aobj2[2].toString();
			city.pinyin = s4;
		}
		return city;
	}
}
