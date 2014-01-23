package cn.com.karl.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BookCollectOpenHelper extends SQLiteOpenHelper {
	static String DBNAME = "collect.db";

	public BookCollectOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, DBNAME, factory, 1);
	}

	public BookCollectOpenHelper(Context context) {
		super(context, DBNAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table collect (_id Integer primary key,co_name varchar(50),"
				+ "co_path varchar(500))";
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table if exists book";
		db.execSQL(sql);
		this.onCreate(db);
	}

}
