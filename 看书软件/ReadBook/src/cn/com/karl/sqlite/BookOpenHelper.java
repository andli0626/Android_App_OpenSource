package cn.com.karl.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BookOpenHelper extends SQLiteOpenHelper {
	
	static String DBNAME = "book.db";

	public BookOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DBNAME, factory, 1);

	}

	public BookOpenHelper(Context context) {
		super(context, DBNAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table book (_id Integer primary key,name varchar(50),"
				+ "path varchar(500),iscollect Integer)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table if exists book";
		db.execSQL(sql);
		this.onCreate(db);
	}

	
}
