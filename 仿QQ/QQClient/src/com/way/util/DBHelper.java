package com.way.util;

import com.way.Constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		super(context, Constants.DBNAME, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE table IF NOT EXISTS user"
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,id TEXT,name TEXT, img TEXT, isOnline TEXT)");
//		db.execSQL("CREATE table IF NOT EXISTS msg"
//				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,id TEXT,name TEXT, img TEXT,date TEXT,isCome TEXT,message TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("ALTER TABLE user ADD COLUMN other TEXT");
	}

}
