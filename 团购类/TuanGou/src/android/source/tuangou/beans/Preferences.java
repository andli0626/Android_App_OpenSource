package android.source.tuangou.beans;

import android.source.tuangou.framework.store.Bean;
import android.source.tuangou.framework.store.Database;
import android.source.tuangou.framework.util.StringUtil;


public class Preferences extends Bean
{

	private static Preferences instance = null;
	private static final String tableName = "temp_preferences";

	public Preferences(){

	}

	//获取TempPreferences对象
	public static Preferences getInstance(){
		if (instance == null)
			instance = new Preferences();
		return instance;
	}

	public void createTable()
	{
		Object aobj[] = new Object[1];
		aobj[0] = "temp_preferences";
		String s = StringUtil.simpleFormat("CREATE TABLE if not exists %s (key TEXT PRIMARY KEY, value TEXT);", aobj);
		boolean flag = db.execSql(s);
	}

	public String get(String s)
	{
		Object aobj[] = new Object[1];
		aobj[0] = "temp_preferences";
		String s1 = StringUtil.simpleFormat("SELECT value from %s WHERE key=?;", aobj);
		Database database = db;
		String as[] = new String[1];
		as[0] = s;
		return database.getSingleString(s1, as);
	}

	//删除temp_preferences表
	public void removeAll(){
		Object aobj[] = new Object[1];
		aobj[0] = "temp_preferences";
		String s = StringUtil.simpleFormat("delete from %s;", aobj);
		
		boolean flag = db.execSql(s);
	}

	public void save(String s, String s1)
	{
		Object aobj[] = new Object[1];
		aobj[0] = "temp_preferences";
		String s2 = StringUtil.simpleFormat("REPLACE INTO %s (key, value) VALUES(?, ?)", aobj);
		Database database = db;
		Object aobj1[] = new Object[2];
		aobj1[0] = s;
		aobj1[1] = s1;
		boolean flag = database.execSql(s2, aobj1);
	}
}
