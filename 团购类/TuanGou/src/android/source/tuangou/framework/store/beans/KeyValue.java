package android.source.tuangou.framework.store.beans;

import android.source.tuangou.framework.store.Bean;
import android.source.tuangou.framework.store.Database;
import android.source.tuangou.framework.util.StringUtil;


public class KeyValue extends Bean
{

	private static final String tableName = "kv";

	public KeyValue()
	{
	}

	public void createTable()
	{
		Object aobj[] = new Object[1];
		aobj[0] = "kv";
		String s = StringUtil.simpleFormat("CREATE TABLE if not exists %s (key TEXT PRIMARY KEY, value TEXT, expire_time INTEGER);", aobj);
		boolean flag = db.execSql(s);
	}

	public String load(String s)
	{
		StringBuilder stringbuilder = (new StringBuilder()).append("SELECT value from %s WHERE key=? AND (expire_time=-1 OR expire_time>");
		long l = System.currentTimeMillis() / 1000L;
		String s1 = stringbuilder.append(l).append(")").toString();
		Object aobj[] = new Object[1];
		aobj[0] = "kv";
		String s2 = StringUtil.simpleFormat(s1, aobj);
		Database database = db;
		String as[] = new String[1];
		as[0] = s;
		return database.getSingleString(s2, as);
	}

	public void save(String s, String s1)
	{
		save(s, s1, 65535L);
	}

	public void save(String s, String s1, long l)
	{
		Object aobj[] = new Object[1];
		aobj[0] = "kv";
		String s2 = StringUtil.simpleFormat("REPLACE INTO %s (key, value, expire_time) VALUES(?, ?, ?)", aobj);
		Database database = db;
		Object aobj1[] = new Object[3];
		aobj1[0] = s;
		aobj1[1] = s1;
		Long long1 = Long.valueOf(l);
		aobj1[2] = long1;
		boolean flag = database.execSql(s2, aobj1);
	}
}
