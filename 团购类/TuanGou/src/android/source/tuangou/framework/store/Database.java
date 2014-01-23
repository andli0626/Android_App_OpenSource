package android.source.tuangou.framework.store;

import java.lang.reflect.Array;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.source.tuangou.framework.Application;
import android.source.tuangou.framework.file.FileHelper;
import android.source.tuangou.framework.util.LogUtil;


/*
 * 数据库类
 * */
public class Database
{
	/*
	 * 数据库打开帮助类
	 * */
	class DatabaseOpenHelper extends SQLiteOpenHelper{

		final Database this$0;

		public void onCreate(SQLiteDatabase sqlitedatabase){
			
		}

		public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j){
			if (j > i){
				StringBuilder stringbuilder = (new StringBuilder()).append("Database path: ");
				String s = sqlitedatabase.getPath();
				LogUtil.d(stringbuilder.append(s).toString());
				boolean flag = FileHelper.delete(sqlitedatabase.getPath());
			}
		}

		DatabaseOpenHelper(Context context, String s, 
				android.database.sqlite.SQLiteDatabase.CursorFactory cursorfactory, 
				int i){
			super(context, s, cursorfactory, i);
			this$0 = Database.this;
		}
	}


	private SQLiteDatabase db;
	private DatabaseOpenHelper dbOpenHelper;
	private String name;

	
	//构造函数，创建数据库
	public Database(Context context, String s){
		//获取版本值
		int i = Application.getInstance().getVersionCode();
		
		Database database = this;
		Context context1 = context;
		//创建数据库帮助类对象
		 dbOpenHelper = database. new 
			DatabaseOpenHelper(context1, s, null, i);
		
		 //获取写数据库的对象
		 db = dbOpenHelper.getWritableDatabase();
		 //数据库名称
		 name = s;
	}

	//创建数据库函数
	public Database(String s){
		this(((Context) (Application.getInstance())), s);
	}

	/**
	 * @deprecated Method execSql is deprecated
	 */
	//执行相应sql
	public boolean execSql(String s){
		boolean flag = false;
		try {
			db.execSQL(s);
			flag = true;
		} catch (SQLException sqlE) {
			String s1 = (new StringBuilder()).append("Failed to exec sql: ").append(s).toString();
			LogUtil.e(sqlE, s1);
			// TODO: handle exception
		}catch(Exception e){
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @deprecated Method execSql is deprecated
	 */

	public  boolean execSql(String s, Object aobj[]){
		boolean flag = false;
		try {
			db.execSQL(s, aobj);
			flag = true;
		} catch (SQLException sqlE) {
			String s1 = (new StringBuilder()).append("Failed to exec sql: ").append(s).toString();
			LogUtil.e(sqlE, s1);
			
			return false;
			// TODO: handle exception
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return flag;
	}

	//获取数据库函数
	public SQLiteDatabase getDb(){
		return db;
	}

	//从数据库中获取单个值
	public  String getSingleString(String sql, String selectionArgs[]){
		Object obj = getSingleValue(sql, selectionArgs);
		String s1;
		if (obj != null)
			s1 = obj.toString();
		else
			s1 = "";
		return s1;
	}

	//从数据库中获取单个值
	public  Object getSingleValue(String sql, String selectionArgs[]){
		Object aobj[][] = query(sql, selectionArgs);
		
		Object obj = null;
		if (aobj.length > 0 && aobj[0].length > 0)
			obj = aobj[0][0];
		return obj;
	}

	/**
	 * @deprecated Method query is deprecated
	 */

	public Object[][] query(String s){
		
		Object aobj1[][];
		try {
			Object aobj[][];
			String as[] = new String[0];
			aobj = query(s, as);
			aobj1 = aobj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			// TODO: handle exception
		}
		return aobj1;
	}

	/**
	 * @deprecated Method query is deprecated
	 */

	/*
	 * 数据库查询
	 * 
	 * */
	public Object[][] query(String sql, String selectionArgs[]){
		//保存查询结果
		Cursor cursor;
		Object aobj[][] = null;
		try {
			int i = 0;
			cursor = null;
			if (selectionArgs == null) {
				selectionArgs = new String[i];
			}
			
			
			
			/*
			 * rawQuery()方法用于执行select语句
			 * 参数1--select语句
			 * 参数2--select语句中占位符参数的值
			 * 
			 * */
			cursor = db.rawQuery(sql, selectionArgs);
			if (cursor == null) {
				return aobj;
			}
			int j;
			j = cursor.getColumnCount();
			int k = cursor.getCount();
			
			int ai[] = { k, j };
			Class<?> classType = Class.forName("java.lang.Object");
			aobj = (Object[][]) Array.newInstance(classType, ai);
			
			int l = 0;
			int i1 = 0;
			while(cursor.moveToNext()){
				i1 = 0;
				for(; i1 <  j; i1++){
					String s1 = cursor.getString(i1);
					aobj[l][i1] = s1;
				}
				l++;
			}
			cursor.close();
		} catch (Exception e) {
			String s2 = (new StringBuilder()).append("Failed to query sql: ").append(sql).toString();
			LogUtil.e(e, s2);
			// TODO: handle exception
		}
		return aobj;
	}
}
