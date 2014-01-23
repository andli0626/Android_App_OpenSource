package android.source.tuangou.framework.store.beans;

import android.source.tuangou.framework.store.Bean;
import android.source.tuangou.framework.store.Database;
import android.source.tuangou.framework.util.StringUtil;


public class UserPreferences extends Bean
{

	private static UserPreferences instance = null;
	private static final String tableName = "user_preferences";

	public UserPreferences()
	{
		instance = this;
	}

	public static UserPreferences getInstance()
	{
		if (instance == null)
			instance = new UserPreferences();
		return instance;
	}

	public void createTable()
	{
		Database database = db;
		Object aobj[] = new Object[1];
		aobj[0] = "user_preferences";
		String s = StringUtil.simpleFormat("create table if not exists %s (user_id text, key text, value text)", aobj);
		boolean flag = database.execSql(s);
	}

	public String get(String s)
	{
		Object aobj[] = new Object[1];
		aobj[0] = "user_preferences";
		String s1 = StringUtil.simpleFormat("select value from %s where key=?", aobj);
		Database database = db;
		String as[] = new String[1];
		as[0] = s;
		return database.getSingleString(s1, as);
	}

	public String get(String s, String s1)
	{
		Object aobj[] = new Object[1];
		aobj[0] = "user_preferences";
		String s2 = StringUtil.simpleFormat("select value from %s where user_id=? and key=?", aobj);
		Database database = db;
		String as[] = new String[2];
		as[0] = s;
		as[1] = s1;
		return database.getSingleString(s2, as);
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

	public void save(String s, String s1)
	{
		save("", s, s1);
	}

	public void save(String s, String s1, String s2)
	{
		Object aobj[] = new Object[1];
		aobj[0] = "user_preferences";
		String s3 = StringUtil.simpleFormat("replace into %s (user_id, key, value) values (?,?,?)", aobj);
		Database database = db;
		Object aobj1[] = new Object[3];
		aobj1[0] = s;
		aobj1[1] = s1;
		aobj1[2] = s2;
		boolean flag = database.execSql(s3, aobj1);
	}
}
